package com.example.caminoalba.models;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Profile implements Serializable {

    private String profile_id;
    private String firstName;
    private String lastName;
    private String birthDate;
    private String gender;
    private String photo;
    private User user;
    //*** ================== ***//
    private List<String> pathList;
    private String currentPath;


    public Profile() {
    }

    public Profile(String profile_id, String firstName, String lastName, String birthDate, String gender, String photo, User user) {
        this.profile_id = profile_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photo = photo;
        this.user = user;
    }

    public String getCurrentPath() {
        return currentPath;
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }

    public void addPath(String path) {
        // First, we check if the profile object already has a list of paths
        List<String> paths = this.getPathList();
        if (paths == null) {
            // If the profile object does not have a list of paths, we create a new one
            paths = new ArrayList<>();
            this.setPathList(paths);
        }
        // Add the new path to the list of paths for this profile
        paths.add(path);
    }
    public List<String> getPathList() {
        return pathList;
    }

    public void setPathList(List<String> pathList) {
        this.pathList = pathList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profile_id=" + profile_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                ", user=" + user +
                '}';
    }
}
