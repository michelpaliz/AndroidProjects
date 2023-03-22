package com.example.caminoalba.models.dto;

import java.time.LocalDate;
import java.util.List;

public class Publication {

    private String title;
    private String description;
    private LocalDate datePublished;
    private List<String> photos;

    public Publication(String title, String description, LocalDate datePublished, List<String> photos) {
        this.title = title;
        this.description = description;
        this.datePublished = datePublished;
        this.photos = photos;
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

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public LocalDate getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(LocalDate datePublished) {
        this.datePublished = datePublished;
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
