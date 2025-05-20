package com.rencontrR.controllers;

import com.rencontrR.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likes")
public class likeControlor {
  @GetMapping
  public ResponseEntity<String> getProfile(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return ResponseEntity.ok("Bonjour" + user.getFirstname() + user.getFirstname());
  }
}
