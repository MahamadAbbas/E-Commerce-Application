package com.retail.e_com.response.dto;

import java.util.List;

import com.retail.e_com.enums.AddressRole;
import com.retail.e_com.model.Contact;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddressContactResponse {

	private int addressId;
	private String streetAddress;
	private String streetAdressAditional;
	private String city;
	private String state;
	private String country;
	private int pincode;
	private AddressRole addressType;

	private List<Contact> contact;
}
