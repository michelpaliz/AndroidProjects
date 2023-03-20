package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.User;
import com.user_manager_v1.models.dto.UserAndProfileRequest;
import com.user_manager_v1.repository.UserRepository;
import com.user_manager_v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public UserAndProfileRequest createUserWithProfile(@RequestBody UserAndProfileRequest userWithProfile) {
        return userService.createUserWithProfile(userWithProfile);
    }

    @PutMapping("/update")
    public void updateProfile(@RequestBody User user) {
        userRepository.save(user);
    }

}
