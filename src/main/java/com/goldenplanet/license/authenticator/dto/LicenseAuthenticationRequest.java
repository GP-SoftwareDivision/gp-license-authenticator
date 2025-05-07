package com.goldenplanet.license.authenticator.dto;

/**
 * LicenseAuthenticationRequest 클래스
 *
 * 이 클래스는 사용자가 입력한 라이선스 키를 보관하는 DTO(데이터 전송 객체)입니다.
 * 주로 인증 요청의 데이터를 캡슐화하여 전달하는 역할을 합니다.
 *
 * @param licenseKey 사용자 요청의 라이선스 키
 */
public record LicenseAuthenticationRequest(String licenseKey) {
}