package com.user_manager_v1.controllers;

import com.user_manager_v1.models.User;
import com.user_manager_v1.models.dto.UserAndProfileBlogRequest;
import com.user_manager_v1.repository.UserRepository;
import com.user_manager_v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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


    @GetMapping("/find/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(Math.toIntExact(id));
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
