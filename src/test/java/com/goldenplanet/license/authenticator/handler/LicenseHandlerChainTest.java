package com.goldenplanet.license.authenticator.handler;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.util.MacAddressMatcher;

class LicenseHandlerChainTest {

	private LicenseHandlerChain licenseHandlerChain;

	@BeforeEach
	void setUp() {
		SecretProperties secretProperties = new SecretProperties();
		secretProperties.setSolutionId("1");
		secretProperties.setMode("AES/ECB/PKCS5Padding");
		secretProperties.setAlgorithm("AES");
		secretProperties.setKeyFilePath("secretkey.txt");

		// Mac 주소 고정
		MacAddressMatcher macAddressMatcher = mock(MacAddressMatcher.class);
		when(macAddressMatcher.matchMacAddress("XX-XX-XX-XX-XX-XX")).thenReturn(true);
		// 핸들러 인스턴스 생성
		ExpiryDateHandler expiryDateHandler = new ExpiryDateHandler();
		SolutionIdHandler solutionIdHandler = new SolutionIdHandler(secretProperties);
		MacAddressHandler macAddressHandler = new MacAddressHandler(macAddressMatcher);

		// 체인 초기화
		expiryDateHandler.setNextHandler(solutionIdHandler);
		solutionIdHandler.setNextHandler(macAddressHandler);

		// 체인 생성
		licenseHandlerChain = new LicenseHandlerChain(expiryDateHandler, solutionIdHandler, macAddressHandler);
	}

	@Test
	void shouldExecuteSuccessfullyWithValidData() {
		EncryptPayloadData data = new EncryptPayloadData("1", "2999-12-31", "XX-XX-XX-XX-XX-XX", "운영"); // 테스트 데이터 생성
		licenseHandlerChain.execute(data); // 체인 실행
	}

	@Test
	void shouldThrowInvalidLicenseExceptionForInvalidMacAddress() {
		EncryptPayloadData data = new EncryptPayloadData("1", "2999-12-31", "OX-XX-XX-XX-XX-XX", "운영");
		assertThrows(InvalidLicenseException.class, () -> {
			licenseHandlerChain.execute(data);
		}, "InvalidLicenseException should be thrown for invalid MAC address");
	}

	@Test
	void shouldThrowInvalidLicenseExceptionForExpiredLicense() {
		EncryptPayloadData data = new EncryptPayloadData("1", "2000-01-01", "XX-XX-XX-XX-XX-XX", "운영");
		assertThrows(InvalidLicenseException.class, () -> {
			licenseHandlerChain.execute(data);
		}, "InvalidLicenseException should be thrown for expired license");
	}

	@Test
	void shouldThrowInvalidLicenseExceptionForWrongSolutionId() {
		EncryptPayloadData data = new EncryptPayloadData("2", "2999-12-31", "XX-XX-XX-XX-XX-XX", "운영");
		assertThrows(InvalidLicenseException.class, () -> {
			licenseHandlerChain.execute(data);
		}, "InvalidLicenseException should be thrown for wrong solution ID");
	}
}