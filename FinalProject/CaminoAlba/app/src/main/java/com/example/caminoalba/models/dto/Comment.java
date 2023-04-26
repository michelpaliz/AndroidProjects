package com.example.caminoalba.models.dto;

import com.example.caminoalba.models.Profile;
import com.example.caminoalba.models.Publication;

public class Comment {
    private String id;
    private String commentText;
    private Profile profile;
    private Publication publication;
    private String publicationId;
    private String datePublished;


    public Comment() {}

    public Comment(String id, String commentText, Profile profile, Publication publication, String publicationId, String datePublished) {
        this.id = id;
        this.commentText = commentText;
        this.profile = profile;
        this.publication = publication;
        this.publicationId = publicationId;
        this.datePublished = datePublished;
    }

    public String getId() {
        return id;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
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

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", commentText='" + commentText + '\'' +
                ", profile=" + profile +
                ", publication=" + publication +
                ", publicationId='" + publicationId + '\'' +
                ", datePublished='" + datePublished + '\'' +
                '}';
    }
}
