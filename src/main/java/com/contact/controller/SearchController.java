package com.contact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.contact.dao.ContactRepository;
import com.contact.dao.UserRepository;
import com.contact.model.Contact;
import com.contact.model.Users;

@RestController
public class SearchController {
	
	private final UserRepository userRepository;
	
	private final ContactRepository contactRepository;

	public SearchController(UserRepository userRepository, ContactRepository contactRepository) {
		this.userRepository = userRepository;
		this.contactRepository = contactRepository;
	}

	//Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchHandler(@PathVariable("query") String query, Principal principal)
	{
		Users users = this.userRepository.getUsersByUserName(principal.getName());

		List<Contact> contacts = this.contactRepository.findByNameContainingAndUsers(query, users);
		
		return ResponseEntity.ok(contacts);
	}

}