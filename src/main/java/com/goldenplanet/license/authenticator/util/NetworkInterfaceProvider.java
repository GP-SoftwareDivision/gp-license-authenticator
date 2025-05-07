package com.goldenplanet.license.authenticator.util;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

/**
 * NetworkInterfaceProvider 클래스
 *
 * 네트워크 인터페이스 정보를 제공하는 유틸리티 클래스입니다.
 * 네트워크 인터페이스의 목록을 가져오는 메서드를 제공합니다.
 */
@Component
public class NetworkInterfaceProvider {

	/**
	 * 네트워크 인터페이스 목록을 반환합니다.
	 *
	 * @return 네트워크 인터페이스의 열거형(Enumeration<NetworkInterface>)
	 * @throws SocketException 네트워크 인터페이스를 가져오는 동안 발생할 수 있는 예외
	 */
	public Enumeration<NetworkInterface> getNetworkInterfaces() throws SocketException {
		return NetworkInterface.getNetworkInterfaces(); // 시스템의 모든 네트워크 인터페이스 정보를 검색하여 반환
	}
}