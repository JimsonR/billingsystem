package com.example.billingsystem.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.xml.crypto.Data;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${sprong.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);

        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getUsernameFromJwt(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

    private Key key(){

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));

    }

    public boolean validateJwtToken(String authToken) {
        try {
            System.out.println("validate");
            Jwts.parser().verifyWith((SecretKey) key())
                    .build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {

        }
        catch (ExpiredJwtException e) {

        } catch (UnsupportedJwtException e) {

        }

        return false;
    }

}