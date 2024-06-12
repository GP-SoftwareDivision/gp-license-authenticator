package com.goldenplanet.license.authenticator.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.util.MacAddressMatcher;

@ExtendWith(SpringExtension.class)
class MacAddressHandlerTest {
	@InjectMocks
	private MacAddressHandler macAddressHandler;

	@Mock
	private MacAddressMatcher macAddressMatcher;

	@Test
	void shouldThrowInvalidLicenseExceptionWhenMacAddressDoesNotMatch() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.licenseType()).thenReturn("운영");
		when(data.macAddress()).thenReturn("00-14-22-01-23-45");
		when(macAddressMatcher.matchMacAddress(anyString())).thenReturn(false);

		assertThrows(InvalidLicenseException.class, () -> macAddressHandler.handle(data));
	}

	@Test
	void shouldHandleSuccessfullyWhenMacAddressIsValid() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.licenseType()).thenReturn("운영");
		when(data.macAddress()).thenReturn("00-14-22-01-23-45");
		when(macAddressMatcher.matchMacAddress(anyString())).thenReturn(true);

		macAddressHandler.handle(data);
	}

	@Test
	void shouldHandleWithoutExceptionForDevLicenseType() {
		EncryptPayloadData data = mock(EncryptPayloadData.class);
		when(data.licenseType()).thenReturn("임시");

		macAddressHandler.handle(data);
	}
}
