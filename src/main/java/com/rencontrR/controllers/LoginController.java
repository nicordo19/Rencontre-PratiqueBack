package com.rencontrR.controllers;

import com.rencontrR.service.JwtService;
import com.rencontrR.LoginRequest;
import com.rencontrR.model.User;
import com.rencontrR.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public LoginController(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            System.out.println("Email reçu : " + loginRequest.getEmail());

            Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
            }

            User user = userOptional.get();

            if (!user.getPassword().equals(loginRequest.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
            }

            String token = jwtService.generateToken(user.getEmail());

            if (token == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Token generation failed");
            }

            // ✅ Création d’un cookie sécurisé avec les bons attributs
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(false) // ❗ true si HTTPS, false en local
                    .path("/")
                    .sameSite("Lax") // ✅ Autorise le cookie dans les appels cross-origin entre ports en local
                    .maxAge(24 * 60 * 60)
                    .build();

            // ✅ Ajout du cookie dans la réponse
            response.setHeader("Set-Cookie", cookie.toString());

            System.out.println("Token envoyé : " + token);

            return ResponseEntity.ok(Map.of(
                    "message", "Connexion réussie",
                    "token", token
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne : " + e.getMessage());
        }
    }
}
