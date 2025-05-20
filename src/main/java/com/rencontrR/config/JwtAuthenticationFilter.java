package com.rencontrR.config;

import com.rencontrR.service.JwtService;
import com.rencontrR.model.User;
import com.rencontrR.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  private final JwtService jwtService;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain)
          throws ServletException, IOException {

    String jwt = null;

    // 1. Essayer dans le header Authorization
    String authHeader = request.getHeader("Authorization");
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwt = authHeader.substring(7);
      logger.info("✅ Token trouvé dans le header Authorization");
    } else {
      // 2. Sinon chercher dans les cookies
      if (request.getCookies() != null) {
        for (Cookie cookie : request.getCookies()) {
          if ("jwt".equals(cookie.getName())) {
            jwt = cookie.getValue();
            logger.info("✅ Token trouvé dans le cookie JWT");
            break;
          }
        }
      }
    }

    // 3. Si aucun token trouvé → on laisse passer la requête sans authentification
    if (jwt == null) {
      logger.warn("❌ Aucun token trouvé ni dans le header ni dans les cookies");
      filterChain.doFilter(request, response);
      return;
    }

    // 4. Extraire l'email et vérifier si on est déjà authentifié
    String userEmail = jwtService.extractUsername(jwt);
    logger.info("🧠 Email extrait du token : {}", userEmail);

    if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      User user = userRepository.findByEmail(userEmail).orElse(null);

      if (user != null && jwtService.isTokenValid(jwt, user.getEmail())) {
        logger.info("✅ Token valide, authentification réussie pour {}", userEmail);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      } else {
        logger.warn("❌ Token invalide ou utilisateur introuvable");
      }
    }

    // 5. Poursuivre la requête
    filterChain.doFilter(request, response);
  }
}
