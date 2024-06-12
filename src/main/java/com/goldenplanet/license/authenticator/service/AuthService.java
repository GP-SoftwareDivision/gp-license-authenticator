package com.goldenplanet.license.authenticator.service;

import org.springframework.stereotype.Service;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.handler.LicenseHandlerChain;
import com.goldenplanet.license.authenticator.util.EncryptPayloadDataProcessor;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final LicenseHandlerChain licenseHandlerChain;
	private final EncryptPayloadDataProcessor encryptPayloadDataProcessor;

	public boolean checkLicenseKey(LicenseAuthenticationRequest licenseAuthenticationRequest) {
		EncryptPayloadData encryptPayloadData = encryptPayloadDataProcessor.process(licenseAuthenticationRequest);
		licenseHandlerChain.execute(encryptPayloadData);
		return true;
	}

}
