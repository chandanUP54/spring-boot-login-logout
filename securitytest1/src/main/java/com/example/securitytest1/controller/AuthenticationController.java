package com.example.securitytest1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.securitytest1.config.JwtProvider;
import com.example.securitytest1.exception.UserException;
import com.example.securitytest1.model.User;
import com.example.securitytest1.repository.UserRepository;
import com.example.securitytest1.request.LoginRequest;
import com.example.securitytest1.response.AuthResponse;
import com.example.securitytest1.services.CustomeUserServiceImplementation;

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

//	public AuthController(UserRepository userRepository,
//			CustomeUserServiceImplementation customeUserServiceImplementation, PasswordEncoder passwordEncoder) {
//		this.userRepository = userRepository;
//		this.customeUserServiceImplementation = customeUserServiceImplementation;
//		this.passwordEncoder = passwordEncoder;
//
//	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {
		String email = user.getEmail();
		String password = user.getPassword();
		String firstString = user.getFirstName();
		String lastNString = user.getLastName();
        String mobile=user.getMobile();
		User isEmailExist = userRepository.findByEmail(email);

		if (isEmailExist != null) {
			throw new UserException("Email is already used with another account");
		}

		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setFirstName(firstString);
		createdUser.setLastName(lastNString);
        createdUser.setMobile(mobile);
		User savedUser = userRepository.save(createdUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(),
				savedUser.getPassword());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Signup success");
//		AuthResponse authResponse = new AuthResponse(token,"signup success");
	

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
