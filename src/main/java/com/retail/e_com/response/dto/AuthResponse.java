package com.retail.e_com.response.dto;

import com.retail.e_com.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
	
	private int userId;
	private String username;
	private UserRole userRole;
	private long accessExpiration;
	private long refreshExpiration;

}
