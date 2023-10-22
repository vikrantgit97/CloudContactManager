package com.acks.controller;

import com.acks.dao.ContactRepository;
import com.acks.dao.UserRepository;
import com.acks.model.Contact;
import com.acks.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class SearchController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    //Search Handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchHandler(@PathVariable("query") String query, Principal principal) {
        Users users = this.userRepository.getUsersByUserName(principal.getName());

        List<Contact> contacts = this.contactRepository.findByNameContainingAndUsers(query, users);

        return ResponseEntity.ok(contacts);
    }

}
