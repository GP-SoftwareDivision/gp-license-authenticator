package com.goldenplanet.license.authenticator.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.exception.MacAddressCannotReadException;

/**
 * LicenseExceptionAdvice 클래스
 *
 * 이 클래스는 애플리케이션 전역에서 발생하는 라이선스 관련 예외를 처리하는
 * Spring REST 컨트롤러 예외 처리 클래스입니다.
 */
@RestControllerAdvice
public class LicenseExceptionAdvice {

	/**
	 * InvalidLicenseException 예외 처리
	 *
	 * 라이선스 키가 유효하지 않을 때 발생하는 예외를 처리합니다.
	 *
	 * @param ex InvalidLicenseException 예외 객체
	 * @return HTTP 400 응답과 예외 메시지를 포함한 ResponseEntity
	 */
	@ExceptionHandler(InvalidLicenseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleInvalidLicenseException(InvalidLicenseException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage()); // 예외 메시지를 응답 본문으로 반환
	}

	/**
	 * MacAddressCannotReadException 예외 처리
	 *
	 * MAC 주소를 읽을 수 없을 때 발생하는 예외를 처리합니다.
	 *
	 * @param ex MacAddressCannotReadException 예외 객체
	 * @return HTTP 400 응답과 예외 메시지를 포함한 ResponseEntity
	 */
	@ExceptionHandler(MacAddressCannotReadException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<?> handleMacAddressCannotReadException(MacAddressCannotReadException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage()); // 예외 메시지를 응답 본문으로 반환
	}

}