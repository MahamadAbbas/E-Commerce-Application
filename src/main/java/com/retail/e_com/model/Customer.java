package com.retail.e_com.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends User {

	@JsonIgnore
	@OneToMany(mappedBy = "customer")
	private List<Address> addresses;
}
