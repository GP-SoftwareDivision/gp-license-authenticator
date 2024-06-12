package com.goldenplanet.license.authenticator.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.handler.LicenseHandlerChain;
import com.goldenplanet.license.authenticator.util.EncryptPayloadDataProcessor;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {
	@Mock
	private LicenseHandlerChain licenseHandlerChain;
	@Mock
	private EncryptPayloadDataProcessor encryptPayloadDataProcessor;

	@InjectMocks
	private AuthService authService;

	@Test
	void shouldReturnTrue_whenLicenseKeyIsValid() {
		// given
		LicenseAuthenticationRequest licenseAuthenticationRequest = mock(LicenseAuthenticationRequest.class);
		EncryptPayloadData encryptPayloadData = mock(EncryptPayloadData.class);

		when(encryptPayloadDataProcessor.process(any(LicenseAuthenticationRequest.class)))
			.thenReturn(encryptPayloadData);
		doNothing().when(licenseHandlerChain).execute(any(EncryptPayloadData.class));

		// when
		boolean result = authService.checkLicenseKey(licenseAuthenticationRequest);

		// then
		assertTrue(result);
		verify(encryptPayloadDataProcessor, times(1)).process(any(LicenseAuthenticationRequest.class));
		verify(licenseHandlerChain, times(1)).execute(any(EncryptPayloadData.class));
	}

	@Test
	void shouldThrowInvalidLicenseException_whenLicenseIsExpired() {
		// given
		LicenseAuthenticationRequest licenseAuthenticationRequest = mock(LicenseAuthenticationRequest.class);
		EncryptPayloadData encryptPayloadData = mock(EncryptPayloadData.class);

		when(encryptPayloadDataProcessor.process(any(LicenseAuthenticationRequest.class)))
			.thenReturn(encryptPayloadData);
		doThrow(new InvalidLicenseException("License has expired")).when(licenseHandlerChain)
			.execute(any(EncryptPayloadData.class));

		// when & then
		assertThrows(InvalidLicenseException.class, () -> authService.checkLicenseKey(licenseAuthenticationRequest));

		verify(encryptPayloadDataProcessor, times(1)).process(any(LicenseAuthenticationRequest.class));
		verify(licenseHandlerChain, times(1)).execute(any(EncryptPayloadData.class));
	}

}
