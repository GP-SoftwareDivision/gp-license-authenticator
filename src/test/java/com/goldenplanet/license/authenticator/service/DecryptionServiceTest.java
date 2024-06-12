package com.goldenplanet.license.authenticator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.strategy.DecryptionStrategy;
import com.goldenplanet.license.authenticator.util.SecretKeyLoader;

@ExtendWith(SpringExtension.class)
class DecryptionServiceTest {
	@InjectMocks
	private DecryptionService decryptionService;

	@Mock
	private SecretKeyLoader secretKeyLoader;

	@Mock
	private SecretProperties secretProperties;

	@Mock
	private DecryptionStrategy decryptionStrategy;

	@Mock
	private SecretKey secretKey;

	@BeforeEach
	void setUp() {
		when(secretProperties.getMode()).thenReturn("AES/ECB/PKCS5Padding");
	}

	@Test
	void shouldReturnDecryptedData_whenEncryptionIsValid() throws Exception {
		String encryptedData = "encryptedData";
		String decryptedData = "decryptedData";

		when(secretKeyLoader.loadSecretKey()).thenReturn(secretKey);
		when(decryptionStrategy.decrypt(encryptedData, secretKey, "AES/ECB/PKCS5Padding")).thenReturn(decryptedData);

		String result = decryptionService.decrypt(encryptedData);

		assertEquals(decryptedData, result);
	}

	@Test
	void shouldThrowInvalidLicenseException_whenDecryptionFails() throws Exception {
		String encryptedData = "encryptedData";

		when(secretKeyLoader.loadSecretKey()).thenReturn(secretKey);
		when(decryptionStrategy.decrypt(encryptedData, secretKey, "AES/ECB/PKCS5Padding")).thenThrow(new Exception());

		assertThrows(InvalidLicenseException.class, () -> decryptionService.decrypt(encryptedData));
	}
}
