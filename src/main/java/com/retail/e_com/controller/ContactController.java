package com.retail.e_com.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retail.e_com.request_dto.ContactsRequest;
import com.retail.e_com.response.dto.UpdateContact;
import com.retail.e_com.service.ContactService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ContactController {

	private ContactService contactService;

	@PostMapping("/{addressId}/addContacts")
	public ResponseEntity<SimpleResponseStructure> addContact(@Valid @RequestBody List<ContactsRequest> contactRequests,
			@PathVariable int addressId) {
		return contactService.addContact(contactRequests, addressId);
	}

	@PostMapping("/{contactId}/updateContact")
	public ResponseEntity<ResponseStructure<UpdateContact>> updateContact(
			@Valid @RequestBody ContactsRequest contactsRequest, @PathVariable int contactId) {
		return contactService.updateContact(contactsRequest, contactId);
	}
}

