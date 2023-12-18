package com.example.securitytest1.response;



import lombok.Data;

@Data
public class AuthResponse {

	private String jwt;
	private String message;
//	public AuthResponse(String jwt, String message) {
//		super();
//		this.jwt = jwt;
//		this.message = message;
//	}
//	
	
	
}