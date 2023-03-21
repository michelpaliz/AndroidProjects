package com.example.caminoalba.models.dto;

import com.example.caminoalba.models.Blog;
import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.User;

public class UserAndProfileBlogRequest {
    private User user;
    private Profile profile;
    private Blog blog;

    public UserAndProfileBlogRequest(User user, Profile profile, Blog blog) {
        this.user = user;
        this.profile = profile;
        this.blog = blog;
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

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
}
