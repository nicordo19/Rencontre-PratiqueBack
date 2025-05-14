package com.rencontrR.controllers;
import com.rencontrR.model.User;
import com.rencontrR.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/register")
public class RestControlleur {

  private final UserRepository userRepository;

public RestControlleur(UserRepository userRepository) {
  this.userRepository = userRepository;
}
@PostMapping
public ResponseEntity<?> registerUser(@RequestBody User user) {
  if (userRepository.existsByEmail(user.getEmail())) {
    return ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
  try {
    User saved = userRepository.save(user);
    return ResponseEntity.ok(saved);
  }
  catch (Exception e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("erreur lors de l'enregistrement");
  }

}

}


