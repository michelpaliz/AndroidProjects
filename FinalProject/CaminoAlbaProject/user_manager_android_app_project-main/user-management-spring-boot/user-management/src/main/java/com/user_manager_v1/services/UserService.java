package com.user_manager_v1.services;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.User;
import com.user_manager_v1.models.dto.UserAndProfileRequest;
import com.user_manager_v1.repository.ProfileRepository;
import com.user_manager_v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@Transactional
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;


    public int registerNewUserServiceMethod(String fname, String lname, String email, String password){
        return userRepository.registerNewUser(fname,lname,email,password);
    }
    // End Of Register New User Service Method.

    public List<String> checkUserEmail(String email){
        return userRepository.checkUserEmail(email);
    }
    // End Of Check User Email Services Method.

    public String checkUserPasswordByEmail(String email){
        return userRepository.checkUserPasswordByEmail(email);
    }
    // End Of Check User Password Services Method.

    public User getUserDetailsByEmail(String email){
        return userRepository.GetUserDetailsByEmail(email);
    }
    // End Of Get User Details By Email.

    public List<User> getUserList(){
        return userRepository.getAllUsers();
    }


    public UserAndProfileRequest createUserWithProfile(@RequestBody UserAndProfileRequest request) {

        User user = request.getUser();
        Profile profile = request.getProfile();

        String hashed_password = BCrypt.hashpw(request.getUser().getPassword(), BCrypt.gensalt());
        user.setPassword(hashed_password);

        // Associate the user and profile objects using the @OneToOne attribute
        user.setPerson(profile);
        profile.setUser(user);

//        List<User>users = (List<User>) userRepository.findAll();

//        for (User value : users) {
//            if (user.getEmail().equalsIgnoreCase(value.getEmail())) {
//                return null;
//            }
//        }


        // Insert the user and profile data into the respective tables
        User savedUser = userRepository.save(user);
        Profile profileSaved = profileRepository.save(profile);

        // Return a successful response
        return new UserAndProfileRequest(savedUser, profileSaved);
    }

}
