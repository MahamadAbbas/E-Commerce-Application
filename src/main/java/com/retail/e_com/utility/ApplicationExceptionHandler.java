package com.retail.e_com.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.retail.e_com.exception.AccessTokenExpireException;
import com.retail.e_com.exception.AddressAllreadyAddedException;
import com.retail.e_com.exception.AddressLimitException;
import com.retail.e_com.exception.AddressNotFoundException;
import com.retail.e_com.exception.AddressTypeIsNullException;
import com.retail.e_com.exception.AddressnotFoundByIdException;
import com.retail.e_com.exception.AuthenticationException;
import com.retail.e_com.exception.ContactNotFoundByIdException;
import com.retail.e_com.exception.ContactsFulledException;
import com.retail.e_com.exception.OtpExpiredException;
import com.retail.e_com.exception.OtpIncorrectException;
import com.retail.e_com.exception.PriorityNotSetException;
import com.retail.e_com.exception.RegistrationSessionExpired;
import com.retail.e_com.exception.UserAlreadyExistByEmailException;
import com.retail.e_com.exception.UserIsNotLoginException;

import lombok.AllArgsConstructor;

@RestControllerAdvice
@AllArgsConstructor
public class ApplicationExceptionHandler extends ResponseEntityExceptionHandler {

	private ErrorStructure<String> Structure;

	private ResponseEntity<ErrorStructure<String>> errorResponse(
			HttpStatus status, String message, String rootCause)
	{
		return new ResponseEntity<ErrorStructure<String>> (Structure 
				.setStatus(status.value())
				.setMessage(message)
				.setRootCause(rootCause), status);
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserAlreadyExistByEmail(
			UserAlreadyExistByEmailException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"User already exists with the given email ID");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleOtpExpired(
			OtpExpiredException ex){
		return errorResponse(HttpStatus.GONE, ex.getMessage(), 
				"OTP time is expired");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleOtpIncorrect(
			OtpIncorrectException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Given OTP is WRONG");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleRegistrationSession(
			RegistrationSessionExpired ex){
		return errorResponse(HttpStatus.GONE, ex.getMessage(), 
				"Registration given time is Expired");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAuthentication(
			AuthenticationException ex){
		return errorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), 
				"Authentication is failed");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleUserIsNotLogin(
			UserIsNotLoginException ex){
		return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), 
				"User is NOT LOGGED IN");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAccessTokenExpire(
			AccessTokenExpireException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"AccessToken Is expired Please Re Generate AccessToken..");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAddressTypeIsNull(
			AddressTypeIsNullException ex){
		return errorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), 
				"Address Type Is not Present ..");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAddressAllreadyAdded(
			AddressAllreadyAddedException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Address Allready Present");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAddressLimit(
			AddressLimitException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Address is Limit");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAddressnotFoundById(
			AddressnotFoundByIdException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Address not Found By Id.....");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleContactsFulled(
			ContactsFulledException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Contacts Fulled ");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleAddressNotFound(
			AddressNotFoundException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Given Address Not Found");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handlePriorityNotSet(
			PriorityNotSetException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Given Priority Not Set");
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> handleContactNotFoundById(
			ContactNotFoundByIdException ex){
		return errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), 
				"Given Contact Not Found By Id..");
	}
}
