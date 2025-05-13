package com.rencontrR.controllers;
import com.rencontrR.model.User;
import com.rencontrR.controllers.repository.UserRepository;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/register")
public class RestControlleur {

  private final UserRepository userRepository;


public RestControlleur(UserRepository userRepository) {
  this.userRepository = userRepository;
}
@PostMapping
  public User registerUser(@RequestBody User user) {
  return userRepository.save(user);
}
}
