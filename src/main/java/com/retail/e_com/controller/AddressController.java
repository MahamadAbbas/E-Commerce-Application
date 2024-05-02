package com.retail.e_com.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.request_dto.AddressRequest;
import com.retail.e_com.response.dto.AddressContactsResponse;
import com.retail.e_com.response.dto.AddressResponse;
import com.retail.e_com.response.dto.AddressUpdateResponse;
import com.retail.e_com.service.AdderssService;
import com.retail.e_com.utility.ResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AddressController {

	private AdderssService addressservice;

	@PostMapping("/addAddress")
	public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(
			@Valid @RequestBody AddressRequest addressRequest,
			@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken) {
		return addressservice.addAddress(addressRequest, accessToken, refreshToken);

	}
	@GetMapping("/findAddress")
	public ResponseEntity<ResponseStructure<AddressContactsResponse>> findAddressByUser(@CookieValue(name = "at", required = false) String accessToken,
			@CookieValue(name = "rt", required = false) String refreshToken){
		return addressservice.findAddress(accessToken,refreshToken);
	}
	@PostMapping("/{addressId}/updateAddress")
	public ResponseEntity<ResponseStructure<AddressUpdateResponse>> updateAddress(@Valid @RequestBody AddressRequest addressRequest,@PathVariable int addressId){
		return addressservice.updateAddress(addressRequest,addressId);
	}
}
