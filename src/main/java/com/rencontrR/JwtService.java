package com.rencontrR;

import io.jsonwebtoken.*; // Importe toutes les classes utiles de la librairie JWT
import io.jsonwebtoken.security.Keys; // Permet de générer une clé secrète avec HMAC (utilisée pour signer le token
import org.springframework.stereotype.Service; // 	Dit à Spring : “cette classe peut être injectée comme un service dans les contrôleurs”

import java.security.Key;
import java.util.Date; // Sert à gérer les dates d’expiration
import java.util.function.Function; // Utilisé pour extraire dynamiquement des informations du token (comme l’email)

import static javax.crypto.Cipher.SECRET_KEY;




  @Service
  public class JwtService {
// créer notre Clé secret pour notre signature et pui on lui donne une durée de vie de 24h
    private static final String SECRET_KEY = "TunNeTrouveraPasMonPswTuPeuxToujoursChercher@Tainul:D";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24h

    private Key getSignInKey() {
      return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    // 🔍 Récupère l'email (subject) à partir du token
    public String generateToken(String email) {
  try {
    return Jwts.builder() // 	Commence la création du token (comme si tu disais replis ta recette )
      // et les ingredients sont tout le reste pour la faire
      .setSubject(email) // Met l'email de l'utilisateur dans le token (c'est l'identifiant principal)
      .setIssuedAt(new Date(System.currentTimeMillis())) //Date de création du token
      .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) //	Date d’expiration calculée automatiquement
      .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Signe le token avec ta clé secrète et l’algorithme HS256
      .compact(); // 	Termine la construction → retourne une String : c’est le token JWT

  }catch (Exception e){
    System.out.println("Erreur generation token : "+e.getMessage());
    return null;
  }
     }


    // 🔍 Récupère l'email (subject) à partir du token
    public String extractToken(String token) {

      return extractClaim(token,Claims::getSubject);

  }
  //decoder le token puis sortir le email
    public String extractUsername(String token) {
      return extractClaim(token, Claims::getSubject);
    }
//Verification d
  public boolean isTokenValid(String token, String userEmail) {
    String email = extractUsername(token);
      return (email.equals(userEmail) && !isTokenExpired(token));
    }
//    //verifi si le token est expiré
    private boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
      }

    private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
    }


    //    // on declar un type T pour recuperer ce qu'on le veux qui peu  avoir de different Type (date;string ,number...)
    private  <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
      final Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSignInKey())
        .build()
       .parseClaimsJws(token)
        .getBody();
      return claimsResolver.apply(claims);
    }

}
//Connexion réussie → le backend génère un JWT :
//"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
//
//→ Ce token contient :
//- l’email (subject)
//- la date de création
//- la date d’expiration
//- une signature secrète
//
//→ On peut ensuite :
//- l’envoyer au frontend
//- le vérifier à chaque requête protégée
