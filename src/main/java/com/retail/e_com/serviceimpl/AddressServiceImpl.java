package com.retail.e_com.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.retail.e_com.enums.AddressRole;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.AccessTokenExpireException;
import com.retail.e_com.exception.AddressAllreadyAddedException;
import com.retail.e_com.exception.AddressLimitException;
import com.retail.e_com.exception.AddressTypeIsNullException;
import com.retail.e_com.exception.AddressnotFoundByIdException;
import com.retail.e_com.exception.UserIsNotLoginException;
import com.retail.e_com.jwt.io.JwtService;
import com.retail.e_com.model.Address;
import com.retail.e_com.model.Contact;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.Seller;
import com.retail.e_com.model.User;
import com.retail.e_com.repository.AddressRepo;
import com.retail.e_com.repository.CustomerRepository;
import com.retail.e_com.repository.SellerRepository;
import com.retail.e_com.repository.UserRepo;
import com.retail.e_com.request_dto.AddressRequest;
import com.retail.e_com.response.dto.AddressContactResponse;
import com.retail.e_com.response.dto.AddressContactsResponse;
import com.retail.e_com.response.dto.AddressResponse;
import com.retail.e_com.response.dto.AddressUpdateResponse;
import com.retail.e_com.service.AdderssService;
import com.retail.e_com.utility.ResponseStructure;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AdderssService {

	private AddressRepo addressRepo;
	private JwtService jwtService;
	private UserRepo userRepo;
	private SellerRepository sellerRepo;
	private CustomerRepository customerRepo;
	private ResponseStructure<AddressResponse> responseStructure;
	private ResponseStructure<AddressContactsResponse> addressContactStructure;
	private ResponseStructure<AddressUpdateResponse> updateresponse;

	@Override
	public ResponseEntity<ResponseStructure<AddressResponse>> addAddress(AddressRequest addressRequest,
			String accessToken, String refreshToken) {

		if (accessToken == null || refreshToken == null)
			throw new UserIsNotLoginException("user is not Login...........");
		if (!addressRequest.getAddressType().equals(AddressRole.PRIMARY)
				&& !addressRequest.getAddressType().equals(AddressRole.ADDITIONAL))
			throw new AddressTypeIsNullException("AddressType Is Not Specified");
		String userName = jwtService.getUserName(refreshToken);
		User user = userRepo.findByUserName(userName).get();
		Address address = null;
		if (user.getUserRole().equals(UserRole.SELLER)) {
			Seller seller = (Seller) user;
			if (seller.getAddress() != null)
				throw new AddressAllreadyAddedException("Address Is Allready Added...");
			address = addressRepo.save(mapToAddress(addressRequest));
			seller.setAddress(address);
			sellerRepo.save(seller);
		} else {

			Customer customer = (Customer) user;

			if (customer.getAddresses().size() >= 5)
				throw new AddressLimitException("You Allready Reached Limit Of Adding Addresses...");
			address = addressRepo.save(mapToAddress(addressRequest));
			address.setCustomer(customer);
			customerRepo.save(customer);
			addressRepo.save(address);

		}

		return ResponseEntity.ok(responseStructure.setStatus(HttpStatus.OK.value()).setMessage("address is saved")
				.setBody(mapToAddressResponse(address)));
	}

	private AddressResponse mapToAddressResponse(Address address) {
		return AddressResponse.builder().addressId(address.getAddressId()).build();
	}

	private Address mapToAddress(AddressRequest addressRequest) {
		return Address.builder().streetAddress(addressRequest.getStreetAddress())
				.streetAdressAditional(addressRequest.getStreetAdressAditional()).city(addressRequest.getCity())
				.state(addressRequest.getState()).country(addressRequest.getCountry())
				.pincode(addressRequest.getPincode()).addressType(addressRequest.getAddressType()).build();

	}

	@Override
	public ResponseEntity<ResponseStructure<AddressContactsResponse>> findAddress(String accessToken,
			String refreshToken) {

		if (accessToken == null && refreshToken != null)
			throw new AccessTokenExpireException ("Invalid token");
		if (accessToken == null && refreshToken == null)
			throw new UserIsNotLoginException("user is not Login...........");
		String userName = jwtService.getUserName(accessToken);
		User user = userRepo.findByUserName(userName).get();
		if (user.getUserRole().equals(UserRole.SELLER)) {
			Seller seller = (Seller) user;
			Address address = seller.getAddress();
			List<Contact> contact = address.getContact();
			return ResponseEntity.ok(addressContactStructure.setStatus(HttpStatus.OK.value())
					.setMessage("Data Found").setBody(mapToAddressContactResponse(address, contact)));
		} else {
			Customer customer = (Customer) user;
			List<Address> addresses = customer.getAddresses();
			return ResponseEntity.ok(addressContactStructure.setStatus(HttpStatus.OK.value())
					.setMessage("Data Found").setBody(mapToAddressContactResponse(addresses)));
		}

	}

	// if it is seller
	private AddressContactsResponse mapToAddressContactResponse(Address address, List<Contact> contact) {

		return AddressContactsResponse.builder().address(mapToAddressContacts(address, contact)).build();

	}

	private List<AddressContactResponse> mapToAddressContacts(Address address, List<Contact> contact) {

		List<AddressContactResponse> list = new ArrayList<>();
		AddressContactResponse build = AddressContactResponse.builder().addressId(address.getAddressId())
				.streetAddress(address.getStreetAddress()).streetAdressAditional(address.getStreetAdressAditional())
				.city(address.getCity()).state(address.getState()).country(address.getCountry())
				.pincode(address.getPincode()).contact(contact).addressType(address.getAddressType()).build();
		list.add(build);

		return list;
	}

	private AddressContactsResponse mapToAddressContactResponse(List<Address> addresses) {

		return AddressContactsResponse.builder().address(mapToAddresContact(addresses)).build();
	}

	private List<AddressContactResponse> mapToAddresContact(List<Address> addresses) {

		List<AddressContactResponse> list = new ArrayList<>();
		for (Address address : addresses) {
			AddressContactResponse build = AddressContactResponse.builder().addressId(address.getAddressId())
					.streetAddress(address.getStreetAddress()).streetAdressAditional(address.getStreetAdressAditional())
					.city(address.getCity()).state(address.getState()).country(address.getCountry())
					.pincode(address.getPincode()).contact(address.getContact()).addressType(address.getAddressType())
					.build();
			list.add(build);

		}
		return list;
	}

	@Override
	public ResponseEntity<ResponseStructure<AddressUpdateResponse>> updateAddress(AddressRequest addressRequest,
			int addressId) {
		return addressRepo.findById(addressId).map(address -> {
			address = mapToAddress(address, addressRequest);
			addressRepo.save(address);
			return ResponseEntity.ok(updateresponse.setStatus(HttpStatus.OK.value())
					.setMessage("Address Is Updated").setBody(mapToAddressUpdateResponse(address)));
		}).orElseThrow(() -> new AddressnotFoundByIdException("address not found by given id....."));

	}

	private AddressUpdateResponse mapToAddressUpdateResponse(Address address) {

		return AddressUpdateResponse.builder().addressId(address.getAddressId())
				.streetAddress(address.getStreetAddress()).streetAdressAditional(address.getStreetAdressAditional())
				.city(address.getCity()).state(address.getState()).country(address.getCountry())
				.pincode(address.getPincode()).addressType(address.getAddressType()).build();
	}

	private Address mapToAddress(Address address, AddressRequest addressRequest) {
		address.setAddressId(address.getAddressId());
		address.setStreetAddress(addressRequest.getStreetAddress());
		address.setStreetAdressAditional(addressRequest.getStreetAdressAditional());
		address.setCity(addressRequest.getCity());
		address.setState(addressRequest.getState());
		address.setCountry(addressRequest.getCountry());
		address.setPincode(addressRequest.getPincode());
		address.setContact(address.getContact());
		address.setCustomer(address.getCustomer());
		return address;

	}

}
