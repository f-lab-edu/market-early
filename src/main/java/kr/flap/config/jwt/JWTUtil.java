package kr.flap.config.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

  private final SecretKey secretKey;

  public JWTUtil(@Value("${SPRING_JWT_SECRET_KEY}") String secret) {
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getUserEmail(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("email", String.class);
  }

  public String getRole(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
  }

  public Boolean isExpired(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
  }

  public String createJwt(String email, String role, Long expiredMs) {
    return Jwts.builder()
            .claim("email", email)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
  }

  public String getTokenFromCookie(HttpServletRequest request) throws Exception {
    Cookie[] cookies = request.getCookies();
    if(cookies != null) {
      for (Cookie cookie : cookies) {
        if(cookie.getName().equals("Authorization")) {
          return cookie.getValue();
        }
      }
    }
    throw new Exception("토큰이 없습니다.");
  }

  public boolean validateTokenFromCookie(HttpServletRequest request) throws Exception {
    String token = getTokenFromCookie(request);
    return token != null && !isExpired(token);
  }
}
