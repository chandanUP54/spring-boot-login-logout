package com.example.spring_login.service;

import java.time.Duration;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.spring_login.dto.LoginDto;
import com.example.spring_login.dto.RegisterDto;
import com.example.spring_login.entity.User;
import com.example.spring_login.repository.UserRepository;
import com.example.spring_login.util.EmailUtil;
import com.example.spring_login.util.OtpUtil;
import jakarta.mail.MessagingException;

@Service
public class UserService {

	@Autowired
	private OtpUtil otpUtil;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private UserRepository userRepository;

	public String register(RegisterDto registerDto) throws MessagingException {
		String otp = otpUtil.generateOtp();
		emailUtil.sendOtpEmail(registerDto.getEmail(), otp);
		User user = new User();
		user.setName(registerDto.getName());
		user.setEmail(registerDto.getEmail());
		user.setPassword(registerDto.getPassword());
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		return "User registration successful";
	}

	public String verifyAccount(String email, String otp) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		System.out.println(user.getOtp()); // form database
		System.out.println(otp); // from link or user
		if (user.getOtp().equals(otp)
				&& Duration.between(user.getOtpGeneratedTime(), LocalDateTime.now()).getSeconds() < 300) {
			user.setActive(true);
			userRepository.save(user);
			return "OTP verified you can login";
		}

		return "Please regenerate otp and try again";
	}

	public String regenerateOtp(String email) throws MessagingException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
		String otp = otpUtil.generateOtp();
		emailUtil.sendOtpEmail(email, otp);
		user.setOtp(otp);
		user.setOtpGeneratedTime(LocalDateTime.now());
		userRepository.save(user);
		return "Email sent... please verify account within 1 minute";
	}

	public String login(LoginDto loginDto) {
		User user = userRepository.findByEmail(loginDto.getEmail())
				.orElseThrow(() ->
				new RuntimeException("User not found with this email: " + loginDto.getEmail()));
		if (!loginDto.getPassword().equals(user.getPassword())) {
			return "Password is incorrect";
		} else if (!user.isActive()) {
			return "your account is not verified";
		}
		return "Login successful";
	}

	public String forgotPassword(String email) throws MessagingException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("user not found this email " + email));

		emailUtil.sendSetPasswordEmail(email);
		return "please check your email to set new password of your account";
	}

	public String setPassword(String email, String newPassword) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new RuntimeException("user not found this email " + email));
		user.setPassword(newPassword);
		userRepository.save(user);
		return "new password set successfully";
	}
}
