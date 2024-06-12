package com.goldenplanet.license.authenticator.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.service.DecryptionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EncryptPayloadDataProcessor {
	private final ObjectMapper objectMapper;
	private final DecryptionService decryptionService;

	public EncryptPayloadData process(LicenseAuthenticationRequest request) {
		String decryptedData = decryptionService.decrypt(request.licenseKey());
		try {
			return objectMapper.readValue(decryptedData, EncryptPayloadData.class);
		} catch (JsonProcessingException e) {
			throw new InvalidLicenseException(e.getMessage());
		}
	}
}
