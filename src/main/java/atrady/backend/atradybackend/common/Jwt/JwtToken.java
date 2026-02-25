package atrady.backend.atradybackend.common.Jwt;

import atrady.backend.atradybackend.common.exception.globalExceptionHandler.AuthenticationException;
import atrady.backend.atradybackend.common.exception.globalExceptionHandler.InvalidJwtToken;
import atrady.backend.atradybackend.usermanagement.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class JwtToken {

    @Value("${jwt.secret}")
    private String secretString;
    private static final int TOKEN_EXPIRATION = 1000*60*60;

    private SecretKey getSigningKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String createToken(Map<String, Object> userDetails, String secret){
        try{
        return Jwts.builder()
                .claims(userDetails)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ JwtToken.TOKEN_EXPIRATION))
                .signWith(getSigningKey(secret), Jwts.SIG.HS512)
                .compact();}
        catch(JwtException e){
            log.error("token creation exception : {}", e.getMessage());
            throw new AuthenticationException("token creation exception : " + e.getMessage());
        }
    }

    public String generateAccessToken(UserEntity user){
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("role", user.getRole());
        claims.put("email",user.getEmail());
        return createToken(claims, secretString
        );

    }

    public boolean validateToken(String token, String secret){
       try{
           log.info("token received for validation");
           token = token.startsWith("Bearer")?token.substring(7).trim():token.trim();
           Jwts.parser()
                   .verifyWith(getSigningKey(secret))
                   .build()
                   .parseSignedClaims(token);
           log.info("token validated successfully");
           return true;
       }catch(JwtException e){
           log.error("Invalid Jwt :{}", e.getMessage());
           throw new InvalidJwtToken(e.getMessage());
       }
    }

    public Claims extractClaims(String token, String secret){

        token = token.startsWith("Bearer ")? token.substring(7).trim():token.trim();
        return Jwts.parser()
                .verifyWith(getSigningKey(secret))
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

    public String extractId(String token, String secret){
        return extractClaims(token, secret).get("id", String.class
        );
    }

    public String extractEmail(String token, String secret){
        return extractClaims(token, secret).get("email", String.class
        );
    }

}
