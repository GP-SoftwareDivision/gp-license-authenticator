package com.goldenplanet.license.authenticator.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

@Component
public class NetworkInterfaceProvider {
	public Enumeration<NetworkInterface> getNetworkInterfaces() throws SocketException {
		return NetworkInterface.getNetworkInterfaces();
	}
}
