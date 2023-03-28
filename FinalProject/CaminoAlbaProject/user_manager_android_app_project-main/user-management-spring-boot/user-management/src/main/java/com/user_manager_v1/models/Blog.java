package com.user_manager_v1.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;

@Entity
@Table(name = "blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int blog_id;
    private String description;
    private boolean enableInfo;
    private double kmlRunned;
    private int points;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Blog> followers;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "followers")
    private List<Blog> following;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "blog_id")
//    @Fetch(value = FetchMode.JOIN)
    private List<Publication> publications;
    @OneToOne
    @PrimaryKeyJoinColumn
    @JsonBackReference
    private Profile profile;

    public Blog() {
    }

    public Blog(int blog_id, String description, boolean enableInfo, double kmlRunned, int points, List<Blog> followers, List<Blog> following, List<Publication> publications, Profile profile) {
        this.blog_id = blog_id;
        this.description = description;
        this.enableInfo = enableInfo;
        this.kmlRunned = kmlRunned;
        this.points = points;
        this.followers = followers;
        this.following = following;
        this.publications = publications;
        this.profile = profile;
    }


    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnableInfo() {
        return enableInfo;
    }

    public void setEnableInfo(boolean enableInfo) {
        this.enableInfo = enableInfo;
    }

    public double getKmlRunned() {
        return kmlRunned;
    }

    public void setKmlRunned(double kmlRunned) {
        this.kmlRunned = kmlRunned;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<Blog> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Blog> followers) {
        this.followers = followers;
    }

    public List<Blog> getFollowing() {
        return following;
    }

    public void setFollowing(List<Blog> following) {
        this.following = following;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
