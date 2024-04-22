package com.goldenplanet.license.authenticator.util;

public record EncryptPayloadData(String solutionId, String licenseExpiredDt, String macAddress) {
}
