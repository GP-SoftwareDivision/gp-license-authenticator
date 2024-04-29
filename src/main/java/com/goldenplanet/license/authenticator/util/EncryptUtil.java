package com.goldenplanet.license.authenticator.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.advice.exception.InvalidLicenseException;

@Component
public class EncryptUtil {
	@Value("${secret.mode}")
	private String mode;
	@Value("${secret.algorithm}")
	private String algorithm;
	@Value("${secret.key-file-path}")
	private String keyFilePath;

	public SecretKey getSecretKey() {
		try {
			Path path = new ClassPathResource(keyFilePath).getFile().toPath();
			byte[] keyBytes = Files.readAllBytes(path);
			return decodeBase64ToSecretKey(new String(keyBytes));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load key file.", e);
		}
	}

	public SecretKey decodeBase64ToSecretKey(String base64Key) {
		// Decode the Base64 string to bytes
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		// Reconstruct the key using SecretKeySpec
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, algorithm);
	}

	public String decrypt(String encryptedData) {
		try {
			Cipher cipher = Cipher.getInstance(mode);
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey());
			byte[] decodedData = Base64.getDecoder().decode(encryptedData);
			byte[] decrypted = cipher.doFinal(decodedData);
			return new String(decrypted);
		} catch (Exception e) {
			throw new InvalidLicenseException(e.getMessage());
		}
	}

}
