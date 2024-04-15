package com.retail.e_com.request_dto;

import com.retail.e_com.enums.UserRole;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
	
	private String name;
	private String email;
	private String password;
	private UserRole userRole;

}
