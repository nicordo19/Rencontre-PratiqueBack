package com.rencontrR.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")

public class User implements UserDetails {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
private String firstname;
private String lastname;
private String password;
private String email;



  public User() {
  }

public User(String firstname, String lastname, String password, String email) {
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

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(()->"ROLE_USER");
  }

  public String getPassword() {
        return password;
    }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true ;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true ;
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




