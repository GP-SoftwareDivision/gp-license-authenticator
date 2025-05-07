package com.goldenplanet.license.authenticator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.service.AuthService;

import lombok.RequiredArgsConstructor;

/**
 * AuthController 클래스
 *
 * 이 클래스는 라이선스 인증 관련 엔드포인트를 제공하는 REST 컨트롤러입니다.
 * 클라이언트 요청을 받아 {@link AuthService}를 통해 라이선스 인증을 수행합니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/license")
public class AuthController {

	// 라이선스 인증 로직을 처리하는 서비스
	private final AuthService authService;

	/**
	 * 라이선스 키의 유효성을 확인하는 POST 엔드포인트
	 *
	 * @param licenseAuthenticationRequest 클라이언트로부터 전달받은 라이선스 인증 요청 데이터
	 * @return ResponseEntity 인증 결과를 포함하는 HTTP 응답 (true 또는 예외 발생 시 오류)
	 */
	@PostMapping("/auth")
	public ResponseEntity<?> checkAuth(@RequestBody LicenseAuthenticationRequest licenseAuthenticationRequest) {
		// AuthService를 통해 라이선스 키를 확인하고 결과를 반환
		return ResponseEntity.ok(authService.checkLicenseKey(licenseAuthenticationRequest));
	}
}