package com.retail.e_com.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.retail.e_com.enums.AddressRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int addressId;
	private String streetAddress;
	private String streetAdressAditional;
	private String city;
	private String state;
	private String country;
	private int pincode;
	private AddressRole addressType;

	@JsonIgnore
	@OneToMany
	private List<Contact> contact;

	@ManyToOne
	private Customer customer;

}
