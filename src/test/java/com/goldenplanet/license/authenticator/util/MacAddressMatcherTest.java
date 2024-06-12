package com.goldenplanet.license.authenticator.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.MacAddressCannotReadException;

@ExtendWith(SpringExtension.class)
class MacAddressMatcherTest {
	@Mock
	private NetworkInterfaceProvider networkInterfaceProvider;
	@InjectMocks
	private MacAddressMatcher macAddressMatcher;

	@Test
	void shouldReturnTrue_whenMacAddressMatches() throws Exception {
		// given
		NetworkInterface networkInterface = mock(NetworkInterface.class);
		when(networkInterface.getHardwareAddress()).thenReturn(new byte[] {0x00, 0x14, 0x22, 0x01, 0x23, 0x45});
		Enumeration<NetworkInterface> networkInterfaces = Collections.enumeration(
			Collections.singleton(networkInterface));
		when(networkInterfaceProvider.getNetworkInterfaces()).thenReturn(networkInterfaces);
		// when
		boolean result = macAddressMatcher.matchMacAddress("00-14-22-01-23-45");

		// then
		assertTrue(result);
	}

	@Test
	void shouldReturnFalse_whenMacAddressDoesNotMatch() throws Exception {
		// given
		NetworkInterface networkInterface = mock(NetworkInterface.class);
		when(networkInterface.getHardwareAddress()).thenReturn(new byte[] {0x00, 0x14, 0x22, 0x01, 0x23, 0x45});
		Enumeration<NetworkInterface> networkInterfaces = Collections.enumeration(
			Collections.singleton(networkInterface));
		when(networkInterfaceProvider.getNetworkInterfaces()).thenReturn(networkInterfaces);
		// when
		boolean result = macAddressMatcher.matchMacAddress("00-14-22-01-23-46");

		// then
		assertFalse(result);
	}

	@Test
	void shouldThrowMacAddressCannotReadException_whenSocketExceptionOccurs() throws SocketException {
		// given
		when(networkInterfaceProvider.getNetworkInterfaces()).thenThrow(
			new SocketException("Unable to read MAC address"));

		// when & then
		MacAddressCannotReadException exception = assertThrows(MacAddressCannotReadException.class, () -> {
			macAddressMatcher.matchMacAddress("00-14-22-01-23-45");
		});

		assertEquals(ErrorCode.MAC_ADDRESS_CANNOT_READ.getErrorMessage(), exception.getMessage());
	}

}
