package com.vietjoke.vn.util;

import com.vietjoke.vn.entity.user.UserEntity;
import com.vietjoke.vn.exception.InvalidParamException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    public String generateToken(UserEntity userEntity) {
        Map<String , Object> claims = new HashMap<>();
        claims.put("username", userEntity.getUsername());
        try{
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userEntity.getUsername())
                    .setExpiration(new Date(System.currentTimeMillis()  + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new InvalidParamException("Cannot create jwt token, error: "+e.getMessage());
        }
    }

    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        //Keys.hmacShaKeyFor(Decoders.BASE64.decode("TaqlmGv1iEDMRiFp/pHuID1+T84IABfuA0xXh4GhiUI="));
        return Keys.hmacShaKeyFor(bytes);
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExpired(String token) {
        Date expirationDate = this.extractClaim(token, Claims::getExpiration);
        return expirationDate.before(new Date());
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }

}