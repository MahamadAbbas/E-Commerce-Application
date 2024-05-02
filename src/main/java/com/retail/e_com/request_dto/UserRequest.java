package com.retail.e_com.request_dto;

import com.retail.e_com.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class UserRequest {
	
	@NotNull
	private String name;// ^[a-zA-Z0-9+_.-]+@+[g]+[m]+[a]+[i]+[l]+.[c]+[o]+[m]
	
//	@NotBlank // ^[a-zA-Z0-9._%+-]+@gmail\\.com$//^[a-zA-Z0-9._%+-]+@gmail\.com$
	@NotBlank(message = "Email is required")
	
	// @Email(message = "Email is not valid")
	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must end with @gmail.com")
	private String email;// ^[a-zA-Z0-9_.+-]+@gmail\\.com
	
	@NotNull // [a-zA-Z0-9+_.-]+@[g][m][a][i][l]+.[c][o][m]
	@NotNull(message = "userPassword should not be null")
	@Size(min = 8, max = 16, message = "password should be length is 8>= &&<=16")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$", message = "give propere password")
	private String password;
	
	@NotNull
	private UserRole userRole;

}
