package com.retail.e_com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.Address;

public interface AddressRepo extends JpaRepository<Address, Integer>  {

}
