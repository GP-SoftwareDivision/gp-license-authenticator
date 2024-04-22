package com.goldenplanet.license.authenticator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.dto.LicenseRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/license")
public class AuthController {
	private final AuthService authService;

	@PostMapping
	public ResponseEntity<?> authenticateLicence(@RequestBody LicenseRequest licenseRequest) {
		return ResponseEntity.ok(authService.createLicense(licenseRequest));
	}

	@GetMapping
	public ResponseEntity<?> getLicenseList() {
		return ResponseEntity.ok(authService.findAllLicense());
	}

	@PostMapping("/auth")
	public ResponseEntity<?> checkAuth(@RequestBody LicenseAuthenticationRequest licenseAuthenticationRequest) {
		return ResponseEntity.ok(authService.checkLicenseKey(licenseAuthenticationRequest));
	}

}
