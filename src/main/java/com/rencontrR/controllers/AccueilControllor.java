package com.rencontrR.controllers;

import com.rencontrR.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/accueil")
public class AccueilControllor {

  @GetMapping
  public ResponseEntity<String> getAccueil(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    System.out.println("je suis ici" + user.getUsername());
    return ResponseEntity.ok("Bienvenue " + user.getUsername());
  }

  }
