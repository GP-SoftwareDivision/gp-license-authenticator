package com.goldenplanet.license.authenticator.service;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.strategy.DecryptionStrategy;
import com.goldenplanet.license.authenticator.util.SecretKeyLoader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DecryptionService {

	private final SecretKeyLoader secretKeyLoader;
	private final SecretProperties secretProperties;
	private final DecryptionStrategy decryptionStrategy;

	public String decrypt(String encryptedData) {
		try {
			SecretKey secretKey = secretKeyLoader.loadSecretKey();
			return decryptionStrategy.decrypt(encryptedData, secretKey, secretProperties.getMode());
		} catch (Exception e) {
			throw new InvalidLicenseException("Decryption failed due to an internal error");
		}
	}
}