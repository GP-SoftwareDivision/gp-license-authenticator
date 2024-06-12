package com.goldenplanet.license.authenticator.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class NetworkInterfaceProviderTest {

	@InjectMocks
	private NetworkInterfaceProvider networkInterfaceProvider;

	@Test
	void shouldReturnNetworkInterfaces_whenCalled() throws SocketException {
		try (MockedStatic<NetworkInterface> mockedNetworkInterface = mockStatic(NetworkInterface.class)) {
			mockedNetworkInterface.when(NetworkInterface::getNetworkInterfaces).thenReturn(mock(Enumeration.class));

			Enumeration<NetworkInterface> networkInterfaces = networkInterfaceProvider.getNetworkInterfaces();

			assertNotNull(networkInterfaces);
		}
	}
}