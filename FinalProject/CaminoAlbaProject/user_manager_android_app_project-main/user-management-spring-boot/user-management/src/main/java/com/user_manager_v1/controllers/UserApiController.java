package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.Publication;
import com.user_manager_v1.models.User;
import com.user_manager_v1.models.dto.UserAndProfileBlogRequest;
import com.user_manager_v1.models.dto.UserAndProfileRequest;
import com.user_manager_v1.repository.UserRepository;
import com.user_manager_v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserApiController {


    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/all")
    public List<User> getAutores() {
        return userService.getUserList();
    }


    @GetMapping("/test")
    public String testEndpoint() {
        return "Test end point is working";
    }


    @PostMapping("/register")
    public UserAndProfileBlogRequest createUserWithProfileAndBlog(@RequestBody UserAndProfileBlogRequest userAndProfileBlogRequest) {
        return userService.createUserWithProfileAndBlog(userAndProfileBlogRequest);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateProfile(@RequestBody User user) {
        User existingUser = userRepository.findById(user.getUser_id()).orElse(null);
        if (existingUser == null) {
            return ResponseEntity.notFound().build();
        }
        existingUser = user;
        User userUpdated = userRepository.save(existingUser);
        return ResponseEntity.ok(userUpdated);
    }

}
