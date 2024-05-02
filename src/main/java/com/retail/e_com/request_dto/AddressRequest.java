package com.retail.e_com.request_dto;

import com.retail.e_com.enums.AddressRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class AddressRequest {

	@NotNull
	@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "streetAddress countains onle alpha numeric charecters")
	private String streetAddress;
	
	@Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "streetAddress countains onle alpha numeric charecters")
	private String streetAdressAditional;
	
	@Pattern(regexp = "^[a-zA-Z]+$", message = "only alphabates can accept")
	private String city;
	
	@Pattern(regexp = "^[a-zA-Z]+$", message = "only alphabates can accept")
	private String state;
	
	@Pattern(regexp = "^[a-zA-Z]+$", message = "only alphabates can accept")
	private String country;
	
	private int pincode;
	
	@NotNull
	private AddressRole addressType;
}
