package com.goldenplanet.license.authenticator.dto;

public record EncryptPayloadData(String solutionId, String expiredDate, String macAddress, String licenseType) {
}
