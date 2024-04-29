package com.goldenplanet.license.authenticator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/license")
public class AuthController {
	private final AuthService authService;

	@PostMapping("/auth")
	public ResponseEntity<?> checkAuth(@RequestBody LicenseAuthenticationRequest licenseAuthenticationRequest) {
		return ResponseEntity.ok(authService.checkLicenseKey(licenseAuthenticationRequest));
	}

}
