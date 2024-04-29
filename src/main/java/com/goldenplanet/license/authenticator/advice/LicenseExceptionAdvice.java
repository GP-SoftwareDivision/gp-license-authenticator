package com.goldenplanet.license.authenticator.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.goldenplanet.license.authenticator.advice.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.advice.exception.MacAddressCannotReadException;

@RestControllerAdvice
public class LicenseExceptionAdvice {
	@ExceptionHandler(InvalidLicenseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleInvalidLicenseException(InvalidLicenseException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(MacAddressCannotReadException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleMacAddressCannotReadException(MacAddressCannotReadException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

}
