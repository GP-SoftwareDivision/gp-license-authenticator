package com.goldenplanet.license.authenticator.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.advice.exception.ErrorCode;
import com.goldenplanet.license.authenticator.advice.exception.MacAddressCannotReadException;

@Component
public class SystemUtil {
	@Value("${secret.solutionId}")
	private String solutionId;

	public String getSolutionId() {
		return solutionId;
	}

	public boolean matchMacAddress(String macAddress) {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			for (NetworkInterface netIf : Collections.list(networkInterfaces)) {
				byte[] mac = netIf.getHardwareAddress();
				if (mac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
					}
					if (macAddress.equals(sb.toString()))
						return true;
				}
			}
			return false;
		} catch (SocketException e) {
			throw new MacAddressCannotReadException(ErrorCode.MAC_ADDRESS_CANNOT_READ.getErrorMessage());
		}
	}
}
