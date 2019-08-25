package com.manageyourtools.toolroom.config;

import com.manageyourtools.toolroom.domains.Employee;
import com.manageyourtools.toolroom.domains.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Slf4j
@Component
public class JwtTokenUtil implements Serializable {

    private static final long ACCESS_TOKEN_VALIDITY_SECONDS = 5*60*60;
    private static final String SIGNING_KEY = "toolroom";

    String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(Employee employee) {
        Claims claims = Jwts.claims().setSubject(employee.getUserName());

        ArrayList<RoleEnum> roles = new ArrayList<>();
        employee.getRoles().forEach(role ->  roles.add(role.getRoleType()));

        claims.put("scopes", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://toolroom.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_SECONDS*1000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username.equals(userDetails.getUsername())
                        && !isTokenExpired(token));
    }

}
