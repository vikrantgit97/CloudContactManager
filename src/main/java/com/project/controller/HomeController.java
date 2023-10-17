package com.project.controller;

import com.project.entities.User;
import com.project.helper.MyMessage;
import com.project.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepo userRepo;


    @RequestMapping(value = {"/", "/home"})
    public String home(Model model) {
        model.addAttribute("title", "Home -smart contact manager");
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About -smart contact manager");
        return "about";
    }

    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Register -smart contact manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    //handler for registering user
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "agreement", defaultValue = "false")
    boolean agreement, Model model, HttpSession session) {
        try {
            if (!agreement) {
                System.out.println("you have not agreed the terms and condition ");
                throw new IllegalArgumentException("you have not agreed the terms and condition");
            }
            if (bindingResult.hasErrors()) {
                System.out.println("ERROR " + bindingResult);
                model.addAttribute("user", user);
                return "signup";
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            System.out.println("Agreement: " + agreement + "  USER :" + user);
            User result = userRepo.save(user);
            // after successfully registered,  form data must return empty
            model.addAttribute("user ", new User());
            session.setAttribute("message", new MyMessage("Successfully Registered", "alert-success"));

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new MyMessage("Something went wrong " + e.getMessage(), "alert-danger"));
            return "signup";
        }

        return "signup";
    }

    //handler for custom login
    @RequestMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "login -smart contact manager");
        return "login";
    }

}
