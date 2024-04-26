package com.retail.e_com.jwt.io;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.retail.e_com.model.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.lang.Maps;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${myapp.jwt.secret}")
	private String secret;

	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiry;

	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiry;

	public String generateAccessToken(User user, String role) {
		return generateToken(user, role, accessExpiry);
	}

	public String generateRefreshToken(User user, String role) {
		return generateToken(user, role, refreshExpiry);
	}

	private String generateToken(User user,String role, long expiration) {
		return Jwts.builder()
				.setClaims(Maps.of("Role", role).build())						
				.setSubject(user.getUserName())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS256)
				.compact();
	}	

	private Key getSignatureKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
	
	public String getUserRole(String token) {
		return claims(token).get("role",String.class);
	}

	public String getUserName(String token) {
		return claims(token).getSubject();
	}
	
	public Date getIssuedDate(String token) {
		return claims(token).getIssuedAt();
	}
	
	private Claims claims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
	}
}
