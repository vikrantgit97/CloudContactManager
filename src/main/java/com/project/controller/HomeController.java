package com.project.controller;

import com.project.entities.User;
import com.project.helper.Message;
import com.project.repo.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class HomeController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userRepo;

    public HomeController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepo) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepo = userRepo;
    }

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

    //Registering user
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult bindingResult, @RequestParam(value = "agreement", defaultValue = "false")
    boolean agreement, Model model, HttpSession session) {
        try {
            if (!agreement) {
                log.info("you have not agreed the terms and condition ");
                throw new IllegalArgumentException("you have not agreed the terms and condition");
            }
            if (bindingResult.hasErrors()) {
                log.error("bindingResult error {}", bindingResult);
                model.addAttribute("user", user);
                return "signup";
            }
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

            log.info("Agreement: {} USER : {}", agreement, user);
            User result = userRepo.save(user);

            // after successfully registered,  form data must return empty
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully Registered", "success"));

            return "redirect:/signin?change=Successfully Registered...please login to continue";
        } catch (Exception e) {
            log.error("registration error {}", e.getMessage());
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong " + e.getMessage(), "alert-danger"));
            return "signup";
        }


    }

    @RequestMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "login -smart contact manager");
        return "login";
    }

}
