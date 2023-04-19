package com.example.caminoalba.models.dto;

import com.example.caminoalba.models.Profile;

public class Comment {
    private String commentText;
    private Profile profile;

    public Comment() {}

    public Comment(String commentText, Profile user) {
        this.commentText = commentText;
        this.profile = user;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
