package com.retail.e_com.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retail.e_com.model.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer> {

}
