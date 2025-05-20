package com.rencontrR.config;

import com.rencontrR.repository.UserRepository;
import com.rencontrR.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  public SecurityConfig(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtService, userRepository);

    return http
      .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ configuration CORS dans Spring Security
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/accueil", "/api/profile","/api/likes").authenticated().anyRequest()
        .permitAll() // ✅ accès public à /login et /register.authenticated()                      // 🔒 autres routes nécessitent authentification
      )
      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .exceptionHandling(ex -> ex.authenticationEntryPoint(
        (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
      ))
      .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
      .build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOrigins(List.of("http://localhost:4200")); // ✅ autorise Angular
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ toutes les méthodes
    config.setAllowedHeaders(List.of("*")); // ✅ tous les headers
    config.setAllowCredentials(true); // ✅ pour les cookies (ex : JWT)
    config.setMaxAge(3600L); // cache CORS pendant 1h

    org.springframework.web.cors.UrlBasedCorsConfigurationSource source =
      new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }
}
