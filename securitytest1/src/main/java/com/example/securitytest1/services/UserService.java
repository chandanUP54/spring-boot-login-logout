package com.example.securitytest1.services;

import com.example.securitytest1.exception.UserException;
import com.example.securitytest1.model.User;

public interface UserService {
	
	public User findUserById(int userId) throws UserException;
	public User findUserProfileByJwt(String jwt) throws UserException;
} 

