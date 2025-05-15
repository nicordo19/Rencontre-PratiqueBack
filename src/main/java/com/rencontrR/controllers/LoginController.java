package com.rencontrR.controllers;

import com.rencontrR.LoginRequest;
import com.rencontrR.model.User;
import com.rencontrR.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

  private final UserRepository userRepository;

  public LoginController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @PostMapping
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    try {
        System.out.println("Email recu"+loginRequest.getEmail());
      Optional<User>userOptional =userRepository.findByEmail(loginRequest.getEmail());
      System.out.println("Utilisateur trouvé " + userOptional.isPresent());
   if(userOptional.isEmpty()){

     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email ");
   }
   User user = userOptional.get();

    if(!user.getPassword().equals(loginRequest.getPassword())){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password ");
    }
    return ResponseEntity.ok(Map.of("message","Connexion success"));
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error ");
    }
  }

}
