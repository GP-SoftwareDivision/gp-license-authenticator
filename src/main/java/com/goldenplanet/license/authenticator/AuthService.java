package com.goldenplanet.license.authenticator;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldenplanet.license.authenticator.advice.exception.ErrorCode;
import com.goldenplanet.license.authenticator.advice.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.dto.LicenseRequest;
import com.goldenplanet.license.authenticator.util.EncryptPayloadData;
import com.goldenplanet.license.authenticator.util.EncryptUtil;
import com.goldenplanet.license.authenticator.util.SystemUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final AuthRepository authRepository;
	private final EncryptUtil encryptUtil;
	private final SystemUtil systemUtil;

	public LicenseJpaEntity createLicense(LicenseRequest licenseRequest) {
		LocalDate expiredDate = licenseRequest.expiredDate();
		EncryptPayloadData encryptPayloadData = new EncryptPayloadData(
			licenseRequest.solutionId(),
			expiredDate.toString(),
			licenseRequest.macAddress(),
			licenseRequest.licenseType()
		);
		LicenseJpaEntity licenseJpaEntity = LicenseJpaEntity.builder()
			.secretKey(licenseRequest.secretKey())
			.licenseExpiredDt(expiredDate)
			.macAddress(licenseRequest.macAddress())
			.licenseKey(createLicenseKey(encryptPayloadData))
			.build();
		return authRepository.save(licenseJpaEntity);
	}

	private String createLicenseKey(EncryptPayloadData encryptPayloadData) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String stringData = objectMapper.writeValueAsString(encryptPayloadData);
			String encryptedData = encryptUtil.encrypt(stringData);
			return encryptedData;
		} catch (IOException e) {
			throw new InvalidLicenseException(ErrorCode.INVALID.getErrorMessage());
		}
	}

	public List<LicenseJpaEntity> findAllLicense() {
		return authRepository.findAll();
	}

	public boolean checkLicenseKey(LicenseAuthenticationRequest licenseAuthenticationRequest) {
		try {
			String decryptedData = encryptUtil.decrypt(licenseAuthenticationRequest.licenseKey());
			ObjectMapper objectMapper = new ObjectMapper();
			EncryptPayloadData encryptPayloadData = objectMapper.readValue(decryptedData, EncryptPayloadData.class);

			LocalDate now = LocalDate.now();
			LocalDate expireDate = LocalDate.parse(encryptPayloadData.licenseExpiredDt());
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
