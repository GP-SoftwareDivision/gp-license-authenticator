package com.goldenplanet.license.authenticator.strategy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AESDecryptionStrategyTest {

	private AESDecryptionStrategy decryptionStrategy;
	private SecretKey secretKey;
	private String mode = "AES/ECB/PKCS5Padding";

	@BeforeEach
	void setUp() throws Exception {
		decryptionStrategy = new AESDecryptionStrategy();
		KeyGenerator keyGen = KeyGenerator.getInstance("AES");
		keyGen.init(128);
		secretKey = keyGen.generateKey();
	}

	private String encrypt(String data, SecretKey key, String mode) throws Exception {
		Cipher cipher = Cipher.getInstance(mode);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedData = cipher.doFinal(data.getBytes());
		return Base64.getEncoder().encodeToString(encryptedData);
	}

	@Test
	void shouldDecryptSuccessfully_whenBase64AndKeyAreValid() throws Exception {
		String originalData = "Hello World";
		String encryptedData = encrypt(originalData, secretKey, mode);

		String decryptedData = decryptionStrategy.decrypt(encryptedData, secretKey, mode);

		assertEquals(originalData, decryptedData);
	}

	@Test
	void shouldThrowIllegalArgumentException_whenEncryptedDataIsNotBase64() throws Exception {
		String encryptedData = "invalidBase64";

		assertThrows(IllegalArgumentException.class, () -> decryptionStrategy.decrypt(encryptedData, secretKey, mode));
	}

	@Test
	void shouldFailDecryption_whenUsingInvalidKey() throws Exception {
		String originalData = "Hello World";
		String encryptedData = encrypt(originalData, secretKey, mode);

		SecretKey invalidKey = new SecretKeySpec("shortkey".getBytes(), "AES");

		assertThrows(Exception.class, () -> decryptionStrategy.decrypt(encryptedData, invalidKey, mode));
	}

	@Test
	void shouldThrowException_whenCipherModeIsInvalid() throws Exception {
		String originalData = "Hello World";
		String encryptedData = encrypt(originalData, secretKey, mode);

		String invalidMode = "AES/INVALID_MODE/PKCS5Padding";

		assertThrows(Exception.class, () -> decryptionStrategy.decrypt(encryptedData, secretKey, invalidMode));
	}
}