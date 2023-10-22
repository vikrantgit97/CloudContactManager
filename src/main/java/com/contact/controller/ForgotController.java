package com.contact.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.dao.UserRepository;
import com.contact.model.Users;
import com.contact.service.EmailService;

@Controller
public class ForgotController {

    Random random = new Random(1000);

    private final EmailService emailService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ForgotController(EmailService emailService, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    //Email id form open handler
    @RequestMapping("/forgot")
    public String openEmailForm() {
        return "forgot_email_form";
    }

    @PostMapping("/send-otp")
    public String openSendOtpForm(@RequestParam("email") String email, HttpSession session) {
        System.out.println("EMAIL : " + email);

        int otp = random.nextInt(9999);
        System.out.println("OTP : " + otp);

        //write code for send OTP to email...

        String subject = "OTP From SCM";
        String message = ""
                + "<div style='border:1px solid #2e2e2e; padding:20px'>"
                + "<h1>"
                + "OTP is : "
                + "<b>" + otp
                + "</b>"
                + "</h1>"
                + "</div>";
        String to = email;

        boolean flag = this.emailService.sendEmail(subject, message, to);

        if (flag) {
            session.setAttribute("myotp", otp);
            session.setAttribute("email", email);
            return "verify_otp";
        } else {
            session.setAttribute("message", "Check your email id !!");

            return "forgot_email_form";
        }

    }

    //verify otp
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") int otp, HttpSession session) {
        int myOtp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");
        if (myOtp == otp) {
            //Password change form
            Users users = this.userRepository.getUsersByUserName(email);

            if (users == null) {
                session.setAttribute("message", "User does not exist with this email !!");

                return "forgot_email_form";
            } else {
                //send change password form
            }

            return "password_change_form";
        } else {
            session.setAttribute("message", "You have entered wrong otp !!");
            return "verify_otp";
        }
    }

    //Change Password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
        String email = (String) session.getAttribute("email");
        Users users = this.userRepository.getUsersByUserName(email);
        users.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
        this.userRepository.save(users);

        return "redirect:/signin?change=password changed successfully...";
    }
}