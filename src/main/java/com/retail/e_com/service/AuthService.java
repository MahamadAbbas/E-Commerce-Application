package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.request_dto.AuthRequest;
import com.retail.e_com.request_dto.OtpRequest;
import com.retail.e_com.request_dto.UserRequest;
import com.retail.e_com.response.dto.AuthResponse;
import com.retail.e_com.response.dto.userResponse;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

public interface AuthService {

	ResponseEntity<SimpleResponseStructure> registerUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<userResponse>> verifyOTP(OtpRequest otpRequest);

	ResponseEntity<ResponseStructure<AuthResponse>> userLogin(AuthRequest authRequest);

	ResponseEntity<SimpleResponseStructure> userLogout(String accessToken, String refreshToken);

	ResponseEntity<ResponseStructure<AuthResponse>> refreshLoginAndTokenRotation(String accessToken,
			String refreshToken);

}
