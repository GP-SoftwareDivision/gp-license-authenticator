package com.goldenplanet.license.authenticator.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;

@ExtendWith(SpringExtension.class)
class ExpiryDateHandlerTest {
	@InjectMocks
	private ExpiryDateHandler expiryDateHandler;

	@Test
	void shouldThrowInvalidLicenseException_whenLicenseIsExpired() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.expiredDate()).thenReturn("2020-01-01");

		assertThrows(InvalidLicenseException.class, () -> expiryDateHandler.handle(data));
	}

	@Test
	void shouldNotThrowAnyException_whenLicenseIsValid() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.expiredDate()).thenReturn("2999-01-01");

		expiryDateHandler.handle(data);
	}
}
