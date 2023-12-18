package com.example.securitytest1.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Component
public class EmailUtil {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendOtpEmail(String email, String otp) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject("OTP for My Spring boot App Registration");
			mimeMessage.setText("Your OTP for registration is:  " + otp + " \n This will be valid for 5 minutes");

			javaMailSender.send(mimeMessage);
		} catch (MessagingException | MailException e) {
			// Handle exceptions appropriately (e.g., log, throw custom exception)
			e.printStackTrace();
		}
	}

	public void sendSetPasswordEmail(String email) {
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();

		try {
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			mimeMessageHelper.setTo(email);
			mimeMessageHelper.setSubject("Set Password");
			mimeMessageHelper.setText(
					"""
							<div>
							   <h1>Below the reset link is given you can reset your password</h1>

							  <a href="http://localhost:3000/set-password?email=%s" target="_blank">Click link to set password</a>
							</div>
							"""
							.formatted(email),
					true);

			javaMailSender.send(mimeMessage);

		} catch (MessagingException | MailException e) {
			// Handle exceptions appropriately (e.g., log, throw custom exception)
			e.printStackTrace();
		}
	}
}
