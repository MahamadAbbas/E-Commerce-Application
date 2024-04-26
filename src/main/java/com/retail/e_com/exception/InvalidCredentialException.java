package com.retail.e_com.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("serial")
public class InvalidCredentialException extends RuntimeException {
	
	private String message;

}
