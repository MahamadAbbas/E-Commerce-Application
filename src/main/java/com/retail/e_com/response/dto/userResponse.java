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
public class userResponse {
	
	private int userId;
	private String displayName;
	private String userName;
	private String email;
	private UserRole userRole;
	private boolean isEmailVerified;

}
