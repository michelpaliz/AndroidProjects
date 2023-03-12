package com.user_manager_v1.controllers;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.repository.ProfileRepository;
import com.user_manager_v1.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProfileApiController {

    @Autowired
    private ProfileRepository profileRepository;

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

    @PostMapping("/update")
    public boolean updatePerson(@RequestBody Profile profile){
        try {
            Log.i("Update Person: ", profile.toString());
            profileRepository.save(profile);
            return true;
        } catch (Exception e) {
            Log.e("Update autor", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


}
