package com.example.spring_login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.spring_login.dto.EmailDto;

@RestController
@RequestMapping("/vsi")
public class NotificationController {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@GetMapping("/hello")
	public String hello() {
		return "cbbv ";
	}
	
	@PostMapping("/send-email")
	public String sendEmail(@RequestBody EmailDto emailDto) {
		
		SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
		
		simpleMailMessage.setTo(emailDto.getTo());
		simpleMailMessage.setSubject(emailDto.getSubject());
		simpleMailMessage.setText(emailDto.getText());
		
		javaMailSender.send(simpleMailMessage);
		return "email sent successfully";
	}
}
