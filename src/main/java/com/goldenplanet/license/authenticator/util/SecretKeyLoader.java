package com.goldenplanet.license.authenticator.util;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.config.SecretProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecretKeyLoader {

	private final ResourceLoader resourceLoader;
	private final SecretProperties secretProperties;

	public SecretKey loadSecretKey() {
		try {
			Resource resource = resourceLoader.getResource("classpath:" + secretProperties.getKeyFilePath());
			byte[] keyBytes = Files.readAllBytes(resource.getFile().toPath());
			return decodeBase64ToSecretKey(new String(keyBytes));
		} catch (IOException e) {
			throw new RuntimeException("Failed to load key file from path: " + secretProperties.getKeyFilePath(), e);
		}
	}

	private SecretKey decodeBase64ToSecretKey(String base64Key) {
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		return new SecretKeySpec(decodedKey, secretProperties.getAlgorithm());
	}
}
