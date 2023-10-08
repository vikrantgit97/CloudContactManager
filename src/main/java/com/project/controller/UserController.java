package com.project.controller;

import com.project.entities.User;
import com.project.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("username: "+userName);
        User userByUserName = userRepo.getUserByUserName(userName);
        System.out.println("user: "+userByUserName);
        model.addAttribute("user ",userByUserName);
        return "normal/user_dashboard";
    }
}
