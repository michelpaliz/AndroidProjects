package com.user_manager_v1.controllers;

import com.user_manager_v1.models.User;
import com.user_manager_v1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserApiController {

    @Autowired
    UserService userService;

    @GetMapping("/user/all")
    public List<User> getAutores() {
        return userService.getUserList();
    }


    @GetMapping("/test")
    public String testEndpoint() {
        return "Test end point is working";
    }

//    @GetMapping("/all")
//    public List<Autor> getAutores() {
//        return userService.;
//    }


}
