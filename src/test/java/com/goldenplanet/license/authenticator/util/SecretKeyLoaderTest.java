package com.goldenplanet.license.authenticator.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.crypto.SecretKey;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.config.SecretProperties;

@ExtendWith(SpringExtension.class)
class SecretKeyLoaderTest {
	@InjectMocks
	private SecretKeyLoader secretKeyLoader;

	@Mock
	private ResourceLoader resourceLoader;

	@Mock
	private SecretProperties secretProperties;

	@Mock
	private Resource resource;

	@BeforeEach
	void setUp() throws IOException {
		when(secretProperties.getKeyFilePath()).thenReturn("keyfile");
		when(secretProperties.getAlgorithm()).thenReturn("AES");
		when(resourceLoader.getResource(anyString())).thenReturn(resource);

		// Create a temporary file for testing
		Path tempFile = Files.createTempFile("test-keyfile", ".tmp");
		Files.write(tempFile, "c29tZSBrZXk=".getBytes()); // base64 for "some key"
		when(resource.getFile()).thenReturn(tempFile.toFile());
	}

	@Test
	void shouldReturnNotNull_whenKeyFileIsValid() throws Exception {
		SecretKey secretKey = secretKeyLoader.loadSecretKey();
		assertNotNull(secretKey);
	}

	@Test
	void shouldThrowRuntimeException_whenKeyFileNotFound() throws Exception {
		when(resource.getFile()).thenThrow(new IOException("File not found"));

		assertThrows(RuntimeException.class, () -> secretKeyLoader.loadSecretKey());
	}

	@Test
	void shouldThrowIllegalArgumentException_whenKeyDataIsInvalid() throws Exception {
		Path tempFile = Files.createTempFile("test-invalid-keyfile", ".tmp");
		Files.write(tempFile, "invalid base64 data".getBytes());
		when(resource.getFile()).thenReturn(tempFile.toFile());

		assertThrows(IllegalArgumentException.class, () -> secretKeyLoader.loadSecretKey());
	}
}
