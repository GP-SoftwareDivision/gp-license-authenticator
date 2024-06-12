package com.goldenplanet.license.authenticator.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.service.DecryptionService;

@ExtendWith(SpringExtension.class)
class EncryptPayloadDataProcessorTest {
	@InjectMocks
	private EncryptPayloadDataProcessor encryptPayloadDataProcessor;

	@Mock
	private ObjectMapper objectMapper;

	@Mock
	private DecryptionService decryptionService;

	@Test
	void shouldReturnEncryptPayloadData_whenLicenseKeyIsValid() throws Exception {
		// given
		LicenseAuthenticationRequest request = mock(LicenseAuthenticationRequest.class);
		when(request.licenseKey()).thenReturn("encryptedKey");
		when(decryptionService.decrypt("encryptedKey")).thenReturn("decryptedData");
		EncryptPayloadData expectedData = new EncryptPayloadData("1", LocalDate.now().plusDays(10).toString(),
			"12-32-23-44-22-22",
			"운영");
		when(objectMapper.readValue("decryptedData", EncryptPayloadData.class)).thenReturn(expectedData);
		// when
		EncryptPayloadData result = encryptPayloadDataProcessor.process(request);
		// then
		assertEquals(expectedData, result);
	}

	@Test
	void shouldThrowInvalidLicenseException_whenJsonDataIsCorrupted() throws Exception {
		// given
		LicenseAuthenticationRequest request = mock(LicenseAuthenticationRequest.class);
		when(request.licenseKey()).thenReturn("encryptedKey");
		when(decryptionService.decrypt("encryptedKey")).thenReturn("decryptedData");
		when(objectMapper.readValue("decryptedData", EncryptPayloadData.class)).thenThrow(
			new JsonProcessingException("Error") {
			});
		// when & then
		assertThrows(InvalidLicenseException.class, () -> encryptPayloadDataProcessor.process(request));
	}
}
