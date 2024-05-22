package com.pirul.springjwt.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.pirul.springjwt.security.services.UserDetailsImpl;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	@Value("${bezkoder.app.jwtSecret}")
	private String jwtSecret;

	@Value("${bezkoder.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	private Key key;

    private static final long JWT_REFRESH_THRESHOLD = 300000; // Example: 5 minutes

	@PostConstruct
	public void init() {
		this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

		return Jwts.builder().setSubject(userPrincipal.getUsername()).setIssuedAt(now).setExpiration(expiryDate)
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}
	public boolean isTokenNearExpiration(String token) {
        try {
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            Date expiration = claims.getExpiration();
            Date now = new Date();
            long timeUntilExpiration = expiration.getTime() - now.getTime();
            logger.info("Time until token expiration: {} milliseconds", timeUntilExpiration);
            return timeUntilExpiration <= JWT_REFRESH_THRESHOLD;
        } catch (Exception e) {
            logger.error("Error checking token expiration: {}", e.getMessage());
            return false;
        }
}
}