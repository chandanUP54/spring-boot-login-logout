package com.example.spring_login.dto;

import lombok.Data;

@Data
public class EmailDto {

	private String to;
	private String subject;
	private String text;
}
