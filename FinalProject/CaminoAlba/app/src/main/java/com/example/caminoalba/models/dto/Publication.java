package com.example.caminoalba.models.dto;

import com.example.caminoalba.models.Blog;

import java.io.Serializable;
import java.util.List;

public class Publication implements Serializable {

    private String title;
    private String description;
    private String datePublished;
    private List<String> photos;
    private Blog blog;

    public Publication() {
    }

    public Publication(String title, String description, String datePublished, List<String> photos, Blog blog) {
        this.title = title;
        this.description = description;
        this.datePublished = datePublished;
        this.photos = photos;
        this.blog = blog;
    }


    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    //    public LocalDateTime getDatePublished() {
//        return datePublished;
//    }
//
//    public void setDatePublished(LocalDateTime datePublished) {
//        this.datePublished = datePublished;
//    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", datePublished=" + datePublished +
                ", photos=" + photos +
                '}';
    }
}
