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

@Component
public class EncryptUtil {
	private SecretKey secretKey;
	@Value("${secret.mode}")
	private String mode;

	public EncryptUtil(@Value("${secret.algorithm}") String algorithm,
		@Value("${secret.key-file-path}") String keyFilePath) {
		try {
			Path path = new ClassPathResource(keyFilePath).getFile().toPath();
			byte[] keyBytes = Files.readAllBytes(path);
			this.secretKey = new SecretKeySpec(keyBytes, algorithm);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load key file.", e);
		}
	}

	public String encrypt(String data) {
		try {
			Cipher cipher = Cipher.getInstance(mode);
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] encrypted = cipher.doFinal(data.getBytes());
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String decrypt(String encryptedData) {
		try {
			Cipher cipher = Cipher.getInstance(mode);
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			byte[] decodedData = Base64.getDecoder().decode(encryptedData);
			byte[] decrypted = cipher.doFinal(decodedData);
			return new String(decrypted);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
