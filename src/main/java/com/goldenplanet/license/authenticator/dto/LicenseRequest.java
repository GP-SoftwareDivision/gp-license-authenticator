package com.goldenplanet.license.authenticator.dto;

import java.time.LocalDate;

public record LicenseRequest(String solutionId, LocalDate expiredDate, String macAddress, String secretKey,
							 String licenseType) {
}
