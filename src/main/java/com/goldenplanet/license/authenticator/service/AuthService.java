package com.goldenplanet.license.authenticator.service;

import java.io.IOException;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldenplanet.license.authenticator.advice.exception.ErrorCode;
import com.goldenplanet.license.authenticator.advice.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.types.LicenseType;
import com.goldenplanet.license.authenticator.util.EncryptUtil;
import com.goldenplanet.license.authenticator.util.SystemUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final EncryptUtil encryptUtil;
	private final SystemUtil systemUtil;

	public boolean checkLicenseKey(LicenseAuthenticationRequest licenseAuthenticationRequest) {
		try {
			String decryptedData = encryptUtil.decrypt(licenseAuthenticationRequest.licenseKey());
			ObjectMapper objectMapper = new ObjectMapper();
			EncryptPayloadData encryptPayloadData = objectMapper.readValue(decryptedData, EncryptPayloadData.class);

			LocalDate now = LocalDate.now();
			LocalDate expireDate = LocalDate.parse(encryptPayloadData.expiredDate());
			if (now.isAfter(expireDate)) {
				throw new InvalidLicenseException(ErrorCode.EXPIRED.getErrorMessage());
			}

			String solutionId = encryptPayloadData.solutionId();
			if (!systemUtil.getSolutionId().equals(solutionId)) {
				throw new InvalidLicenseException(ErrorCode.SOLUTION_CODE_MISMATCH.getErrorMessage());
			}

			if (encryptPayloadData.licenseType().equals(LicenseType.DEV.getDescription()))
				return true;

			if (!systemUtil.matchMacAddress(encryptPayloadData.macAddress())) {
				throw new InvalidLicenseException(ErrorCode.MAC_ADDRESS_MISMATCH.getErrorMessage());
			}
			return true;
		} catch (IOException e) {
			throw new InvalidLicenseException(ErrorCode.INVALID.getErrorMessage());
		}
	}

}
