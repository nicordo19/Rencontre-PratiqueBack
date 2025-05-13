package com.rencontrR.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "users")

public class User {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String firstname;
private String lastname;
private String password;
private String email;



  public User() {
  }

public User(String firstname, String lastname, String password, String email, int age, LocalDate birthday, String orientation, LocalDate date_inscription) {
    this.firstname = firstname;
    this.lastname = lastname;
    this.password = password;
    this.email = email;
;

}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    }




