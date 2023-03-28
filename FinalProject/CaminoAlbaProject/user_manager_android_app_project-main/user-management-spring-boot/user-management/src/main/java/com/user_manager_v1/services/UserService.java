package com.user_manager_v1.services;

import com.user_manager_v1.models.Blog;
import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.User;
import com.user_manager_v1.models.dto.BlogDTO;
import com.user_manager_v1.models.dto.UserAndProfileBlogRequest;
import com.user_manager_v1.models.dto.UserAndProfileRequest;
import com.user_manager_v1.repository.BlogRepository;
import com.user_manager_v1.repository.ProfileRepository;
import com.user_manager_v1.repository.UserRepository;
import org.apache.commons.codec.CharEncoding;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.lang.NonNull;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    @Autowired
    UserRepository userRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BlogRepository blogRepository;


    public int registerNewUserServiceMethod(String fname, String lname, String email, String password) {
        return userRepository.registerNewUser(fname, lname, email, password);
    }
    // End Of Register New User Service Method.

    public List<String> checkUserEmail(String email) {
        return userRepository.checkUserEmail(email);
    }
    // End Of Check User Email Services Method.

    public String checkUserPasswordByEmail(String email) {
        return userRepository.checkUserPasswordByEmail(email);
    }
    // End Of Check User Password Services Method.

    public User getUserDetailsByEmail(String email) {
        return userRepository.GetUserDetailsByEmail(email);
    }
    // End Of Get User Details By Email.

    public List<User> getUserList() {
        return userRepository.getAllUsers();
    }


    public User getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(Math.toIntExact(userId));
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("User not found with id " + userId);
        }
    }


    public UserAndProfileBlogRequest createUserWithProfileAndBlog(@RequestBody UserAndProfileBlogRequest request) {

        User user = request.getUser();
        Profile profile = request.getProfile();
        BlogDTO blogDTO = request.getBlog();

        String hashed_password = BCrypt.hashpw(request.getUser().getPassword(), BCrypt.gensalt());
        user.setPassword(hashed_password);

        // Associate the user and profile objects using the @OneToOne attribute
        user.setPerson(profile);
        if (user.getVerificationCode() == null) {
            user.setVerificationCode(generateVerificationCode());
        }

        profile.setUser(user);

        ModelMapper modelMapper = new ModelMapper();
        Blog blog = modelMapper.map(blogDTO, Blog.class);
        blog.setProfile(profile);

        // Insert the user and profile data into the respective tables
        User savedUser = userRepository.save(user);
        Profile profileSaved = profileRepository.save(profile);
        Blog blogSaved = blogRepository.save(blog);

        //send verification email
        sendEmail(user);

        // Return a successful response
        return new UserAndProfileBlogRequest(savedUser, profileSaved, modelMapper.map(blogSaved, BlogDTO.class));
    }


    public String generateVerificationCode() {
        int count = 6; // number of characters in the verification code
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }


    public void sendEmail(User user) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
            String mailSubject = "Camino del Alba" + " has sent a message";
            String mailContent = "<p><b>Sender name:</b> " + user.getFirst_name() + " </p>";
            mailContent += "<p><b>Sender email: </b> " + user.getEmail() + "</p>";
            String subject = "Please confirm your email";
            String message = "Hi " + user.getFirst_name() + ", hope you are enjoying the app, please verify your email with this code ";
            String verificationCode = user.getVerificationCode();

            mailContent += "<p><b>Subject</b>: " + subject + "</p>";
            mailContent += "<p><b>Message</b>: <br> " + message + "</p>" +
                    "<br><p><b>" + verificationCode + "</b></p>";
            //**** adding inline image ******//
            mailContent += "<hr><img src='cid:logoImage'/>";
            try {
                mimeMessageHelper.setFrom("michelpaliz@gmail.com", "Message");
                mimeMessageHelper.setTo(user.getEmail());
                mimeMessageHelper.setSubject(mailSubject);
                //******* ADDING INLINE IMAGE *****//
                mimeMessageHelper.setText(mailContent, true);
                ClassPathResource resource = new ClassPathResource("/static/logo.png");
                mimeMessageHelper.addInline("logoImage", resource);

                javaMailSender.send(mimeMessage);

            } catch (MessagingException m) {
                m.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }


}
