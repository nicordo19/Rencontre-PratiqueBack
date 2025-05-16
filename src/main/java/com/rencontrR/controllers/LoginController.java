package com.rencontrR.controllers;

import com.rencontrR.JwtService;
import com.rencontrR.LoginRequest;
import com.rencontrR.model.User;
import com.rencontrR.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

import static javax.crypto.Cipher.SECRET_KEY;

@RestController
@RequestMapping("/login")
public class LoginController {

  private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginController(UserRepository userRepository, JwtService jwtService) {

    this.userRepository = userRepository;
        this.jwtService = jwtService;
    }
//cett metode va reagir avec la requette poste
  @PostMapping
  //on personalise le cod HTTP avec entity et renvois n'import qielle type <?>
  // on averti de plus angular que le json doit etre converti en java
  // de plus on recherceh l'utilisateur envoyer pas le front
  public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        //on controle les error
    try {
        System.out.println("Email recu"+loginRequest.getEmail());
        //ici on demande de chercher dans tout les mail de la base de donné  l'utilsateur
      Optional<User>userOptional =userRepository.findByEmail(loginRequest.getEmail());

      System.out.println("Utilisateur trouvé " + userOptional.isPresent());
      // on verifi si le clien met un mauvais mail
   if(userOptional.isEmpty()){

     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email ");
   }
   // si non on le recuper
   User user = userOptional.get();
// on verifi le mot d passe si il est incorecte
    if(!user.getPassword().equals(loginRequest.getPassword())){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password ");
    }
    //on créer si non un token  et on l'envois pour le securiser solement si le token est bien genéré
    String token = jwtService.generateToken(user.getEmail());
        Cookie cookie = new Cookie("jwt", token);
        cookie.setMaxAge(24 * 60 * 60);         // durée en secondes
        cookie.setHttpOnly(true);              // sécurisé contre accès JS
        cookie.setSecure(false);               // true en HTTPS
        cookie.setPath("/");                   // valable pour toutes les routes

        response.addCookie(cookie);            // envoie le cookie dans la réponse
        System.out.println("mon token " + token );
    if (token == null) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error generation token ");

    } else  {


        return ResponseEntity.ok(Map.of(
                "token", token,
                "message","Connexion success"));


    }
    // on verifi si rien ne fontionne et renvois une errors generale
    }catch (Exception e){
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error ");
    }
  }

}
