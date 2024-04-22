package com.goldenplanet.license.authenticator.dto;

import java.time.LocalDate;

public record LicenseRequest(String solutionId, LocalDate LicenseExpiredDt, String macAddress, String secretKey) {
}
