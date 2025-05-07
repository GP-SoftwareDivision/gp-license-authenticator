package com.goldenplanet.license.authenticator.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.MacAddressCannotReadException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MacAddressMatcher {
	private final NetworkInterfaceProvider networkInterfaceProvider;

	/**
	 * 주어진 MAC 주소와 현재 시스템의 네트워크 인터페이스 MAC 주소를 비교합니다.
	 *
	 * @param macAddress 비교 대상 MAC 주소 (문자열 형식).
	 * @return boolean 주어진 MAC 주소와 일치하면 true, 그렇지 않으면 false를 반환합니다.
	 * @throws MacAddressCannotReadException 네트워크 인터페이스의 MAC 주소를 읽을 수 없을 경우 예외를 발생시킵니다.
	 */
	public boolean matchMacAddress(String macAddress) {
		try {
			Enumeration<NetworkInterface> networkInterfaces = networkInterfaceProvider.getNetworkInterfaces();
			for (NetworkInterface netIf : Collections.list(networkInterfaces)) {
				byte[] mac = netIf.getHardwareAddress();
				if (mac != null) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
					}
					if (macAddress.contentEquals(sb)) {
						return true;
					}
				}
			}
			return false;
		} catch (SocketException e) {
			throw new MacAddressCannotReadException(ErrorCode.MAC_ADDRESS_CANNOT_READ.getErrorMessage());
		}
	}
}