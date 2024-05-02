package com.retail.e_com.service;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.request_dto.AddressRequest;
import com.retail.e_com.response.dto.AddressContactsResponse;
import com.retail.e_com.response.dto.AddressResponse;
import com.retail.e_com.response.dto.AddressUpdateResponse;
import com.retail.e_com.utility.ResponseStructure;

import jakarta.validation.Valid;

public interface AdderssService {

	ResponseEntity<ResponseStructure<AddressResponse>> addAddress(@Valid AddressRequest addressRequest,
			String accessToken, String refreshToken);

	ResponseEntity<ResponseStructure<AddressContactsResponse>> findAddress(String accessToken, String refreshToken);

	ResponseEntity<ResponseStructure<AddressUpdateResponse>> updateAddress(@Valid AddressRequest addressRequest,
			int addressId);

}
