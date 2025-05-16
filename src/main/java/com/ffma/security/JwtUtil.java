package com.ffma.security;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String SECRET = "jndfjkfdkjkdsklkekopkop";

	// generate token

	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
				.signWith(SignatureAlgorithm.HS256, SECRET)
				.compact();
	}
	
	//Extract Username from token
	
	public String extractUsername(String token) {
		return Jwts.parser()
				.setSigningKey(SECRET)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
		
		
		
	}
	
	
	//check if token is expired or not 
	
	public boolean isTokenExpired(String token) {
		Date expiration = Jwts.parser()
		.setSigningKey(SECRET)
		.parseClaimsJws(token)
		.getBody()
		.getExpiration();
		return expiration.before(new Date());
	}
	
	//validate token by checking username and exp
	
	public boolean validateToken(String token,UserDetails userDetails) {
		
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername())  && !isTokenExpired(token);
		
	}

}
