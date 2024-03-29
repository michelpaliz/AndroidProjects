package com.user_manager_v1.models.dto;

import com.user_manager_v1.models.Blog;
import com.user_manager_v1.models.Profile;
import com.user_manager_v1.models.User;

public class UserAndProfileBlogRequest {

    private User user;
    private Profile profile;
    private BlogDTO blog;

    public UserAndProfileBlogRequest(User user, Profile profile, BlogDTO blog) {
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

    public BlogDTO getBlog() {
        return blog;
    }

    public void setBlog(BlogDTO blog) {
        this.blog = blog;
    }
}
