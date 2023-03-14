package com.user_manager_v1.services;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    public void updateProfile(Profile profile) {
        profileRepository.save(profile);
    }

}
