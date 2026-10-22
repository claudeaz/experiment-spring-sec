package com.example.experiment1_spring_security.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.lang.Function;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.KeyAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.zip.DeflaterInputStream;

@Service
public class JwtService {

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    public String generateToken(Map<String, Object> claims, UserDetails userDetails){
        return buildToken(claims, userDetails);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails) {
        var authorities = userDetails.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).toList();
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(claims)
                .claim("authorities", authorities)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token){
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        String username = userDetails.getUsername();
        return extractUsername(token).equals(username) && isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

//    public String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//    public Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//    private Claims extractAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//    private Boolean isTokenExpired(String token) {
//            return extractExpiration(token).before(new Date());
//    }
//    public Boolean validateToken(String token, UserDetails userDetails) {
//                final String username = extractUsername(token);
//                return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
}
