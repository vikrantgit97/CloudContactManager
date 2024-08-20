package com.project.controller;

import com.project.entities.User;
import com.project.repo.UserRepository;
import com.project.service.EmailService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Slf4j
@Controller
public class ForgotController {

	private final EmailService emailService;
	
	private final UserRepository userRepository;
	
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public ForgotController(EmailService emailService, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.emailService = emailService;
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	Random random = new Random(1000);

	//Email id form handler
	@RequestMapping("/forgot")
	public String openEmailForm()
	{
		return "forgot_email_form";
	}

	@PostMapping("/send-otp")
	public String openSendOtpForm(@RequestParam("email") String email, HttpSession session)
	{

		int otp = random.nextInt(9999);
		log.info("OTP : {}", otp);

		String subject = "OTP From Cloud Contact Manager";
		String message = "<div style='border:1px solid #2e2e2e; padding:20px'>"
				+ "<h1>"
				+ "OTP is : "
				+ "<b>"+otp
				+ "</b>"
				+ "</h1>"
				+ "</div>";

        boolean flag = this.emailService.sendEmail(subject, message, email);
		
		if(flag)
		{
			session.setAttribute("myotp", otp);
			session.setAttribute("email", email);
			return "verify_otp";
		}
		else {
			session.setAttribute("message", "Check your email id !!");
			
			return "forgot_email_form";
		}
		
	}
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp") int otp, HttpSession session)
	{
		int myOtp =(int) session.getAttribute("myotp");
		String email =(String) session.getAttribute("email");
		if(myOtp == otp)
		{
			//Password change form
			User user = this.userRepository.getUserByUserName(email);
			
			if(user == null)
			{
				session.setAttribute("message", "User does not exist with this email !!");
				
				return "forgot_email_form";
			}
			return "password_change_form";
		}
		else {
			session.setAttribute("message", "You have entered wrong otp !!");
			return "verify_otp";
		}
	}
	
	//Change Password
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newPassword") String newPassword, HttpSession session)
	{
		String email =(String) session.getAttribute("email");
		User user = this.userRepository.getUserByUserName(email);
		user.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
		this.userRepository.save(user);
		
		return "redirect:/signin?change=password changed successfully...";
	}
}
