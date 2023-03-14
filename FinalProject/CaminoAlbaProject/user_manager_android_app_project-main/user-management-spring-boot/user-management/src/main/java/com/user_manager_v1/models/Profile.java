package com.user_manager_v1.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.user_manager_v1.helpers.ProfileDateDeserializer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profile_id;
    private String firstName;
    private String lastName;
    @JsonDeserialize(using = ProfileDateDeserializer.class)
    private Date birthDate;
    private String gender;
    private String photo;


    //Empezamos a declarar las relaciones que existen

//    @OneToOne(cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    public Profile(int profile_id, String firstName, String lastName, Date birthDate, String gender, String photo, User user) {
        this.profile_id = profile_id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photo = photo;
        this.user = user;
    }

    public Profile(String firstName, String lastName, Date birthDate, String gender, String photo, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photo = photo;
        this.user = user;
    }

    public Profile() {

    }


    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Person{" +
                "person_id=" + profile_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                ", user=" + user +
                '}';
    }
}
