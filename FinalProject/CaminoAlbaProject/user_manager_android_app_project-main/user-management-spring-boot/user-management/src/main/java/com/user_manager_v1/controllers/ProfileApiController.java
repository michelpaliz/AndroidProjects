package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.repository.ProfileRepository;
import com.user_manager_v1.services.ProfileService;
import com.user_manager_v1.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileApiController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private ProfileService profileService;

    @PostMapping("/add")
    public boolean addPerson(@RequestBody Profile profile) {
        try {
            Log.i("Nuevo Person: ", profile.toString());
            profileRepository.save(profile);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @GetMapping("/all")
    public List<Profile> getAutores() {
        return profileRepository.findAll();
    }


    @PutMapping("/update")
    public void updateProfile(@RequestBody Profile profileDetails) {
        profileRepository.save(profileDetails);

    }


}
