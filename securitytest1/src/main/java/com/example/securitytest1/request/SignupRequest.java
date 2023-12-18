package com.example.securitytest1.request;

import lombok.Data;

@Data
public class SignupRequest {

	private String firstName;
	private String lastName;
	private String mobile;
	private String email;
	private String password;
}
