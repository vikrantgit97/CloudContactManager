package com.project.controller;

import com.project.entities.Contact;
import com.project.entities.User;
import com.project.repo.ContactRepo;
import com.project.repo.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {
	
	private  final UserRepo userRepository;
	
	private final ContactRepo contactRepository;

	public SearchController(UserRepo userRepository, ContactRepo contactRepository) {
		this.userRepository = userRepository;
		this.contactRepository = contactRepository;
	}

	//Search Handler
	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchHandler(@PathVariable("query") String query, Principal principal)
	{
		User user = this.userRepository.getUserByUserName(principal.getName());

		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);
		
		return ResponseEntity.ok(contacts);
	}

}
