package com.project.controller;

import com.project.entities.Contact;
import com.project.entities.User;
import com.project.repo.ContactRepository;
import com.project.repo.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {

    private final UserRepository userRepository;

    private final ContactRepository contactRepository;

    public SearchController(UserRepository userRepository, ContactRepository contactRepository) {
        this.userRepository = userRepository;
        this.contactRepository = contactRepository;
    }

    //Search Handler
    @GetMapping("/search/{query}")
    public ResponseEntity<?> searchHandler(@PathVariable("query") String query, Principal principal) {
        User user = this.userRepository.getUserByUserName(principal.getName());

        List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, user);

        return ResponseEntity.ok(contacts);
    }

    @GetMapping("/search-user/{query}")
    public ResponseEntity<?> searchUserHandler(@PathVariable("query") String query, Principal principal){
        System.out.println(query);
        User user = this.userRepository.getUserByUserName(principal.getName());
        List<User> users = this.userRepository.findByNameContaining(query);
        return ResponseEntity.ok(users);
    }
}
