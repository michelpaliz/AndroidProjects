package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.Publication;
import com.user_manager_v1.models.User;
import com.user_manager_v1.repository.ProfileRepository;
import com.user_manager_v1.services.ProfileService;
import com.user_manager_v1.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileApiController {

    @Autowired
    private ProfileRepository profileRepository;

    @GetMapping("/all")
    public List<Profile> getAutores() {
        return profileRepository.findAll();
    }


    @PutMapping("/update")
    public ResponseEntity<Profile> updateProfile(@RequestBody Profile profileDetails) {
        System.out.println("esto es id " + profileDetails.getProfile_id());
        Profile existingProfile = profileRepository.findById(profileDetails.getProfile_id()).orElse(null);
        if (existingProfile == null) {
            return ResponseEntity.notFound().build();
        }
        existingProfile = profileDetails;
        Profile profileUpdated = profileRepository.save(existingProfile);
        return ResponseEntity.ok(profileUpdated);
    }

}


