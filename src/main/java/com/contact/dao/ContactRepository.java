package com.contact.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.model.Contact;
import com.contact.model.Users;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	
	//Pagination
	
	@Query("from Contact as c where c.users.id =:userId")
	//currentPage - page
	//Contact Per page - 5
	Page<Contact> findContactByUser(@Param("userId") int userid, Pageable pageable);
	
	//search
	List<Contact> findByNameContainingAndUsers(String name, Users users);
}