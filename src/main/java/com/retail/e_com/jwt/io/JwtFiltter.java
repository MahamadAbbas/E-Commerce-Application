package com.retail.e_com.jwt.io;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.retail.e_com.exception.InvalidCredentialException;
import com.retail.e_com.repository.AccessTokenRepo;
import com.retail.e_com.repository.RefreshTokenRepo;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtFiltter extends OncePerRequestFilter{

	private AccessTokenRepo accessRepo;
	private RefreshTokenRepo refreshRepo;
	private JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String at = null;
		String rt = null;

		if(request.getCookies() != null) {
			for (Cookie cookie : request.getCookies()) {
				if(cookie.getName().equals(at)) at=cookie.getValue();
				if(cookie.getName().equals(rt)) rt=cookie.getValue();
			}
		}

		if(at!=null && rt!=null) {
			if(accessRepo.existsByTokenAndIsBlocked(at,true) && 
					refreshRepo.existsByTokenAndIsBlocked(rt,true)) throw new InvalidCredentialException("Invalid Credential");

			String userName =jwtService.getUserName(at);
			String role = jwtService.getUserRole(at);

			if(userName!=null && SecurityContextHolder.getContext().getAuthentication()==null && role != null) {
				UsernamePasswordAuthenticationToken token =
						new UsernamePasswordAuthenticationToken(userName,null, 
								Collections.singleton(new SimpleGrantedAuthority(role)));
				token.setDetails(new WebAuthenticationDetails(request));
				SecurityContextHolder.getContext().setAuthentication(token);

			}
		}
		filterChain.doFilter(request, response);
	}
}
