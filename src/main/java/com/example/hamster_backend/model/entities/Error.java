package com.example.hamster_backend.model.entities;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@AllArgsConstructor
@ToString
public class Error {

	private String error; 
	private String message; 
	private HttpStatus status; 
	
}
