package com.retail.e_com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.e_com.enums.PriorityRole;
import com.retail.e_com.exception.AddressNotFoundException;
import com.retail.e_com.exception.ContactNotFoundByIdException;
import com.retail.e_com.exception.ContactsFulledException;
import com.retail.e_com.exception.PriorityNotSetException;
import com.retail.e_com.model.Contact;
import com.retail.e_com.repository.AddressRepo;
import com.retail.e_com.repository.ContactRepo;
import com.retail.e_com.request_dto.ContactsRequest;
import com.retail.e_com.response.dto.UpdateContact;
import com.retail.e_com.service.ContactService;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContactServiceImpl implements ContactService {

	private AddressRepo addressRepo;
	private ContactRepo contactRepo;
	private SimpleResponseStructure responseStructure;
	private ResponseStructure<UpdateContact> updateContactStructure;

	@Override	
	public ResponseEntity<SimpleResponseStructure> addContact(List<ContactsRequest> contactRequests, int addressId) {

		return addressRepo.findById(addressId).map(address -> {

			if (contactRequests.size() > 2)
				throw new ContactsFulledException("Cannot add more than two contacts at once.");

			List<Contact> contacts = new ArrayList<>();
			for (ContactsRequest request : contactRequests) {
				contacts.add(mappToContact(request));
			}
			contactRepo.saveAll(contacts);
			System.out.println("*******************************************************************");
			address.setContact(contacts);
			
			addressRepo.save(address);

		return ResponseEntity.ok(responseStructure.setStatus(HttpStatus.OK.value())
					.setMessage("Contacs Is created..SuccessFully"));
		}).orElseThrow(() -> new AddressNotFoundException("address is not present"));

	}

	private Contact mappToContact(ContactsRequest request) {
		if (request.getPriority().equals(PriorityRole.ADDITIONAL) || request.getPriority().equals(PriorityRole.PRIMARY))
			return Contact.builder().name(request.getName()).phoneNumber(request.getPhoneNumber())
					.email(request.getEmail()).priority(request.getPriority()).build();
		throw new PriorityNotSetException(" priority not set..");
	}

	@Override
	public ResponseEntity<ResponseStructure<UpdateContact>> updateContact(ContactsRequest contactsRequest,
			int contactId) {
		return contactRepo.findById(contactId).map(contact -> {

			contact = mapToContact(contact, contactsRequest);
			contactRepo.save(contact);
			 return ResponseEntity.ok(updateContactStructure.setStatus(HttpStatus.OK.value())
					.setMessage("contact is Updated...").setBody(mapToUpdateContact(contact)));
		}).orElseThrow(() -> new ContactNotFoundByIdException("contact Not Found By Given Id"));

	}

	private UpdateContact mapToUpdateContact(Contact contact) {
		return UpdateContact.builder().contactId(contact.getContactId()).name(contact.getName())
				.email(contact.getEmail()).phoneNumber(contact.getPhoneNumber()).priority(contact.getPriority())
				.build();
	}

	private Contact mapToContact(Contact contact, ContactsRequest contactsRequest) {
		contact.setContactId(contact.getContactId());
		contact.setName(contactsRequest.getName());
		contact.setPhoneNumber(contactsRequest.getPhoneNumber());
		contact.setEmail(contactsRequest.getEmail());
		contact.setPriority(contactsRequest.getPriority());

		return contact;

	}

}
