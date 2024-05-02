package com.goldenplanet.license.authenticator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health-check")
public class HealthCheckController {
	@GetMapping
	public ResponseEntity<String> checkHealth() {
		boolean isHealthy = true;
		if (isHealthy) {
			return ResponseEntity.ok("UP");
		} else {
			return ResponseEntity.status(503).body("DOWN");
		}
	}
}
