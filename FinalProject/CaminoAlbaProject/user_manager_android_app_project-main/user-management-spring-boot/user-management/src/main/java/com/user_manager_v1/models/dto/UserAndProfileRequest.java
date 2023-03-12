package com.user_manager_v1.models.dto;

import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.User;

import java.util.Date;

public class UserAndProfileRequest {
    private User user;
    private Profile profile;

    public UserAndProfileRequest(User user, Profile profile) {
        this.user = user;
        this.profile = profile;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
