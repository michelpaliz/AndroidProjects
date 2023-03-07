package com.user_manager_v1.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int person_id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String gender;
    private String photo;


    //Empezamos a declarar las relaciones que existen

//    @OneToOne(cascade = CascadeType.ALL)
//    @PrimaryKeyJoinColumn

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;


    public Person(String firstName, String lastName, Date birthDate, String gender, String photo, User user) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.photo = photo;
        this.user = user;
    }

    public Person() {

    }

    public int getPerson_id() {
        return person_id;
    }

    private void setPerson_id(int person_id) {
        this.person_id = person_id;
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

    @Override
    public String toString() {
        return "Person{" +
                "person_id=" + person_id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", photo='" + photo + '\'' +
                ", user=" + user +
                '}';
    }
}
