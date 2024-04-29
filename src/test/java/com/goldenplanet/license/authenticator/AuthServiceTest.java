package com.goldenplanet.license.authenticator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.goldenplanet.license.authenticator.advice.exception.ErrorCode;
import com.goldenplanet.license.authenticator.advice.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.util.EncryptUtil;
import com.goldenplanet.license.authenticator.util.SystemUtil;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {
	@Mock
	private AuthRepository authRepository;
	@Mock
	private EncryptUtil encryptUtil;
	@Mock
	private SystemUtil systemUtil;

	@InjectMocks
	private AuthService authService;

	@Test
	void checkLicenseKey_AllConditionsMatch_True() {
		// Given
		String encryptedKey = "validEncryptedLicenseKey";
		String decryptedData = """
			{
			    "solutionId": "solution123",
			    "licenseExpiredDt": "%s",
			    "macAddress": "00-14-22-01-23-46",
			    "licenseType": "운영"
			}
			""".formatted(LocalDate.now().plusDays(1).toString());
		LicenseAuthenticationRequest request = new LicenseAuthenticationRequest(encryptedKey);

		when(encryptUtil.decrypt(encryptedKey)).thenReturn(decryptedData);
		when(systemUtil.matchMacAddress(anyString())).thenReturn(true);
		when(systemUtil.getSolutionId()).thenReturn("solution123");

		// When
		boolean result = authService.checkLicenseKey(request);

		// Then
		assertTrue(result);
		verify(encryptUtil).decrypt(encryptedKey);
		verify(systemUtil).matchMacAddress("00-14-22-01-23-46");
		verify(systemUtil).getSolutionId();
	}

	@Test
	void checkLicenseKey_LicenseTypeIsDev_True() {
		// Given
		String encryptedKey = "validEncryptedLicenseKey";
		String decryptedData = """
			{
			    "solutionId": "solution123",
			    "licenseExpiredDt": "%s",
			    "macAddress": "00-14-22-01-23-46",
			    "licenseType": "임시"
			}
			""".formatted(LocalDate.now().plusDays(1).toString());
		LicenseAuthenticationRequest request = new LicenseAuthenticationRequest(encryptedKey);

		when(encryptUtil.decrypt(encryptedKey)).thenReturn(decryptedData);
		when(systemUtil.matchMacAddress(anyString())).thenReturn(false);
		when(systemUtil.getSolutionId()).thenReturn("solution123");

		// When
		boolean result = authService.checkLicenseKey(request);

		// Then
		assertTrue(result);
		verify(encryptUtil).decrypt(encryptedKey);
		verify(systemUtil).getSolutionId();
		verify(systemUtil, times(0)).matchMacAddress(anyString());
	}

	@Test
	void checkLicenseKey_MacAddressDoesNotMatch_False() {
		// Given
		String encryptedKey = "invalidMacLicenseKey";
		String decryptedData = """
			{
			    "solutionId": "solution123",
			    "licenseExpiredDt": "%s",
			    "macAddress": "00-14-22-01-23-46",
			    "licenseType": "운영"
			}
			""".formatted(LocalDate.now().plusDays(1).toString());
		LicenseAuthenticationRequest request = new LicenseAuthenticationRequest(encryptedKey);

		when(systemUtil.getSolutionId()).thenReturn("solution123");
		when(encryptUtil.decrypt(encryptedKey)).thenReturn(decryptedData);
		when(systemUtil.matchMacAddress("00-14-22-01-23-46")).thenReturn(false);

		// When & Then(throws)
		Exception exception = assertThrows(InvalidLicenseException.class, () -> authService.checkLicenseKey(request));
		assertEquals(ErrorCode.MAC_ADDRESS_MISMATCH.getErrorMessage(), exception.getMessage());
	}

	@Test
	void checkLicenseKey_LicenseIsExpired_False() {
		// Given
		String encryptedKey = "expiredLicenseKey";
		String decryptedData = """
			{
			    "solutionId": "solution123",
			    "licenseExpiredDt": "%s",
			    "macAddress": "00-14-22-01-23-45",
			    "licenseType": "운영"
			}
			""".formatted(LocalDate.now().minusDays(1).toString());
		LicenseAuthenticationRequest request = new LicenseAuthenticationRequest(encryptedKey);

		when(systemUtil.matchMacAddress(anyString())).thenReturn(true);
		when(encryptUtil.decrypt(encryptedKey)).thenReturn(decryptedData);

		// When & Then(throws)
		Exception exception = assertThrows(InvalidLicenseException.class, () -> authService.checkLicenseKey(request));
		assertEquals(ErrorCode.EXPIRED.getErrorMessage(), exception.getMessage());
	}

	@Test
	void checkLicenseKey_SolutionIdDoesNotMatch_False() {
		// Arrange
		String encryptedKey = "mismatchSolutionIdLicenseKey";
		String decryptedData = """
			{
			    "solutionId": "solution999",
			    "licenseExpiredDt": "%s",
			    "macAddress": "00-14-22-01-23-45",
			    "licenseType": "운영"
			}
			""".formatted(LocalDate.now().plusDays(1).toString());
		LicenseAuthenticationRequest request = new LicenseAuthenticationRequest(encryptedKey);

		when(systemUtil.matchMacAddress(anyString())).thenReturn(true);
		when(encryptUtil.decrypt(encryptedKey)).thenReturn(decryptedData);
		when(systemUtil.getSolutionId()).thenReturn("solution123");

		// Act & Assert
		Exception exception = assertThrows(InvalidLicenseException.class, () -> authService.checkLicenseKey(request));
		assertEquals(ErrorCode.SOLUTION_CODE_MISMATCH.getErrorMessage(), exception.getMessage());
	}

}
