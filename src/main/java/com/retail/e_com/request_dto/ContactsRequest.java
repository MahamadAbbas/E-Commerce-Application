package com.retail.e_com.request_dto;

import org.hibernate.validator.constraints.Range;

import com.retail.e_com.enums.PriorityRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactsRequest {

	@Pattern(regexp = "^[a-zA-Z]+$", message = "only alphabates can accept")
	@NotNull
	private String name;

	@NotNull
	@Range(min = 6000000000l, max = 9999999999l)
	private long phoneNumber;

	@Email(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must end with @gmail.com")
	@NotNull
	private String email;

	@NotNull
	private PriorityRole priority;

}
