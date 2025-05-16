package com.rencontrR.config;

import com.rencontrR.JwtService;
import com.rencontrR.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// c'est la class principal qui intercepte toute les requette
// un peu comme un dounier de toute les route
public class JwtAuthenticationFilter  extends OncePerRequestFilter {
  //jwtService pour analyser le token
  //userRepository pour vérifier que l’utilisateur existe bien

  private final JwtService jwtService;

  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;

  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

  }
}
