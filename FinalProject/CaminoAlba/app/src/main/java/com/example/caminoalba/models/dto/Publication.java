package com.example.caminoalba.models.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Publication {

    private String title;
    private String description;
    private String datePublished;
    private List<String> photos;

    public Publication() {
    }

    public Publication(String title, String description, LocalDateTime datePublished, List<String> photos) {
        this.title = title;
        this.description = description;
        this.datePublished = getFormattedDate(datePublished);
        this.photos = photos;
    }

    public Publication(String title, String description, String datePublished, List<String> photos) {
        this.title = title;
        this.description = description;
        this.datePublished = datePublished;;
        this.photos = photos;
    }


    public void setDatePublished(LocalDateTime datePublished) {
        this.datePublished = getFormattedDate(datePublished);
    }

    public String getFormattedDate(LocalDateTime formattedDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return formattedDate.format(formatter);
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

    public String getDatePublished() {
        return datePublished;
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
