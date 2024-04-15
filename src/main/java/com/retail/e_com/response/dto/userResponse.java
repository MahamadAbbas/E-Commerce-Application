package com.retail.e_com.response.dto;

import com.retail.e_com.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class userResponse {
	
	private int userId;
	private String displayName;
	private String userName;
	private String email;
	private UserRole userRole;
	private boolean isEmailVerified;

}
