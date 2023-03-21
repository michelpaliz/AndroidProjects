package com.user_manager_v1.models;

import javax.persistence.*;
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
    @ManyToMany
    private List<Blog> followers;

    @ManyToMany(mappedBy = "followers")
    private List<Blog> following;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Profile profile;

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
