package com.goldenplanet.license.authenticator.exception;

import lombok.Getter;

/**
 * ErrorCode 열거형
 *
 * 이 열거형은 라이선스 검증 과정에서 발생할 수 있는 오류 코드와
 * 이에 상응하는 오류 메시지를 정의합니다.
 */
@Getter
public enum ErrorCode {

	/**
	 * 유효하지 않은 키 상태를 나타냅니다.
	 */
	INVALID("It is a invalid key"),

	/**
	 * MAC 주소 불일치를 나타냅니다.
	 */
	MAC_ADDRESS_MISMATCH("MAC address does not match."),

	/**
	 * 라이선스 만료를 나타냅니다.
	 */
	EXPIRED("The license has expired."),

	/**
	 * 솔루션 코드 불일치를 나타냅니다.
	 */
	SOLUTION_CODE_MISMATCH("Solution code does not match."),

	/**
	 * MAC 주소를 읽을 수 없는 경우를 나타냅니다.
	 */
	MAC_ADDRESS_CANNOT_READ("Mac address can't read");

	/**
	 * 오류 메시지
	 *
	 * 각 오류 코드에 연관된 상세 메시지를 포함합니다.
	 */
	private final String errorMessage;

	/**
	 * ErrorCode 생성자
	 *
	 * @param errorMessage 오류 메시지 문자열
	 */
	ErrorCode(String errorMessage) {
		this.errorMessage = errorMessage; // 생성 시 오류 메시지를 필드에 저장
	}

}