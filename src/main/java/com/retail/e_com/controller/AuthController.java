package com.retail.e_com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.jwt.io.JwtService;
import com.retail.e_com.request_dto.AuthRequest;
import com.retail.e_com.request_dto.OtpRequest;
import com.retail.e_com.request_dto.UserRequest;
import com.retail.e_com.response.dto.AuthResponse;
import com.retail.e_com.response.dto.userResponse;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
	
	private AuthService authService;
	private JwtService jwtService;
	private AuthenticationManager authenticationManager;
	
	@PostMapping("/register")
	public ResponseEntity<SimpleResponseStructure> registerUser(@RequestBody UserRequest userRequest){
		return authService.registerUser(userRequest);
	}
	
	@PostMapping("/verify-email")                        
	public ResponseEntity<ResponseStructure<userResponse>> verifyOTP(@RequestBody OtpRequest otpRequest){
		return authService.verifyOTP(otpRequest);
	}
	
//	@GetMapping("/test")
//	public String test() {
//		return jwtService.generateAccessToken("abbas","SELLER");
//	}
	
	@PostMapping("/login")
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(@RequestBody AuthRequest authRequest) {
		return authService.userLogin(authRequest);
	}
	
	// USER LOGOUT 
	
	@PostMapping("/logout")
	public ResponseEntity<SimpleResponseStructure> userLogout(@CookieValue("at") String accessToken,
			@CookieValue("rt") String refreshToken) {
		return authService.userLogout(accessToken,refreshToken);
	}
	
	@PostMapping("/refeshlogin/tokenrotation")
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshLoginAndTokenRotation(@CookieValue("at") String accessToken,
			@CookieValue("rt") String refreshToken) {
		return authService.refreshLoginAndTokenRotation(accessToken, refreshToken);
	}
}
