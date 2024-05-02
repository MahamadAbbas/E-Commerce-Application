package com.retail.e_com.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.retail.e_com.request_dto.ContactsRequest;
import com.retail.e_com.response.dto.UpdateContact;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import jakarta.validation.Valid;

public interface ContactService {

	ResponseEntity<SimpleResponseStructure> addContact(@Valid List<ContactsRequest> contactRequests, int addressId);

	ResponseEntity<ResponseStructure<UpdateContact>> updateContact(@Valid ContactsRequest contactsRequest,
			int contactId);

}
