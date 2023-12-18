package com.example.securitytest1.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.securitytest1.config.JwtProvider;
import com.example.securitytest1.exception.UserException;
import com.example.securitytest1.model.User;
import com.example.securitytest1.repository.UserRepository;
import com.example.securitytest1.request.LoginRequest;
import com.example.securitytest1.request.SignupRequest;
import com.example.securitytest1.response.AuthResponse;
import com.example.securitytest1.services.CustomeUserServiceImplementation;
import com.example.securitytest1.util.EmailUtil;
import com.example.securitytest1.util.OtpUtil;

import jakarta.mail.MessagingException;


@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
public class AuthenticationController {
	
	@GetMapping("/hello")
	public String Heyb() {
		return "hello controller bro";
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomeUserServiceImplementation customeUserServiceImplementation;
	
	@Autowired
	private OtpUtil otpUtil;
	@Autowired
	private EmailUtil emailUtil;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest signupRequest) throws UserException {
		
		String otp = otpUtil.generateOtp();
//		emailUtil.sendOtpEmail(signupRequest.getEmail(), otp);
		
		String email = signupRequest.getEmail();
	
		User isEmailExist = userRepository.findByEmail(email);

		if (isEmailExist != null) {
			throw new UserException("Email is already used with another account");
		}

		User createdUser = new User();
		createdUser.setEmail(signupRequest.getEmail());
		createdUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		createdUser.setFirstName(signupRequest.getFirstName());
		createdUser.setLastName(signupRequest.getLastName());
        createdUser.setMobile(signupRequest.getMobile());
        createdUser.setRole("USER");
        createdUser.setOtp(otp);
        createdUser.setOtpGeneratedTime(LocalDateTime.now());
		User savedUser = userRepository.save(createdUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signup success");
		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);
	}

//	login code starts
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginUserHandler(@RequestBody LoginRequest loginRequest) {

		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		Authentication authentication = authenticate(username, password);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signin success");
//		AuthResponse authResponse = new AuthResponse(token,"signin success");
		

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.CREATED);

	}
	
	@PostMapping("/forget-password")
	public String forgotPassword(@RequestParam String email) throws MessagingException {
      User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("user not found by email" + email);
		}
		emailUtil.sendSetPasswordEmail(email);


		return "please check your email to set new password of your account";
	}

	
	
	
	private Authentication authenticate(String username, String password) {
		UserDetails userDetails = customeUserServiceImplementation.loadUserByUsername(username);

		if (userDetails == null) {

			throw new BadCredentialsException("Invalid Username....");
		}

		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Password...");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
