package com.user_manager_v1.models.dto;

import com.user_manager_v1.models.Blog;
import com.user_manager_v1.models.Publication;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BlogDTO {
    private int blog_id;
    private String description;
    private boolean enableInfo;
    private double kmlRunned;
    private int points;
    private List<Integer> followerIds;
    private List<Integer> followingIds;
    private List<Integer> publicationIds;

    public BlogDTO() {
    }

    // Constructor
    public BlogDTO(Blog blog) {
        this.blog_id = blog.getBlog_id();
        this.description = blog.getDescription();
        this.enableInfo = blog.isEnableInfo();
        this.kmlRunned = blog.getKmlRunned();
        this.points = blog.getPoints();
        this.followerIds = blog.getFollowers().stream().map(Blog::getBlog_id).collect(Collectors.toList());
        this.followingIds = blog.getFollowing().stream().map(Blog::getBlog_id).collect(Collectors.toList());
        this.publicationIds = blog.getPublications().stream().map(Publication::getId).collect(Collectors.toList());
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

    public List<Integer> getFollowerIds() {
        return followerIds;
    }

    public void setFollowerIds(List<Integer> followerIds) {
        this.followerIds = followerIds;
    }

    public List<Integer> getFollowingIds() {
        return followingIds;
    }

    public void setFollowingIds(List<Integer> followingIds) {
        this.followingIds = followingIds;
    }

    public List<Integer> getPublicationIds() {
        return publicationIds;
    }

    public void setPublicationIds(List<Integer> publicationIds) {
        this.publicationIds = publicationIds;
    }

    // getters and setters
}