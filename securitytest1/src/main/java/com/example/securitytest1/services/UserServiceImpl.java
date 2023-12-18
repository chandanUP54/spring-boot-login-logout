package com.example.securitytest1.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.securitytest1.config.JwtProvider;
import com.example.securitytest1.exception.UserException;
import com.example.securitytest1.model.User;
import com.example.securitytest1.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtProvider jwtProvider;
	@Override
	public User findUserById(int userId) throws UserException {
		
		Optional<User> user=userRepository.findById(userId);
		if(user.isPresent()) {
			return user.get();
		}
		throw new UserException("user not found with id: "+userId);
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {
		String email=jwtProvider.getEmailFromToken(jwt);
		User user=userRepository.findByEmail(email);
		if(user==null) {
			throw new UserException("user not found with email: "+email);
		}
		return user;
	}

}
