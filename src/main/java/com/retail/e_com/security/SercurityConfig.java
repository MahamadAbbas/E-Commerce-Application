package com.retail.e_com.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.retail.e_com.jwt.io.JwtFiltter;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SercurityConfig {

	private UserDetailsService  userDetailsService;
	private JwtFiltter jwtFiltter;

	@Bean
	SecurityFilterChain  filterChain(HttpSecurity httpSecurity) throws Exception
	{
		return httpSecurity.authorizeHttpRequests( auth -> auth.requestMatchers("/**").permitAll()
				.anyRequest().authenticated())
				.csrf(csrf -> csrf.disable())
				.sessionManagement(management -> {
					management.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
				})
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtFiltter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}

	@Bean
	AuthenticationProvider authenticationProvider()
	{
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(passwordEncoder());
		provider.setUserDetailsService(userDetailsService);
		return provider;
	}

	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder(12);//hashing 12 times
	}
	
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
}
