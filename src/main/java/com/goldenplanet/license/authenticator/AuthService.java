package com.goldenplanet.license.authenticator;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		LocalDate expiredDate = licenseRequest.LicenseExpiredDt();
		EncryptPayloadData encryptPayloadData = new EncryptPayloadData(licenseRequest.solutionId(),
			expiredDate.toString(),
			licenseRequest.macAddress());
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
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
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

			if (!systemUtil.matchMacAddress(encryptPayloadData.macAddress())) {
				throw new RuntimeException(ErrorCode.MAC_ADDRESS_MISMATCH.getErrorMessage());
			}

			LocalDate now = LocalDate.now();
			LocalDate expireDate = LocalDate.parse(encryptPayloadData.licenseExpiredDt());
			if (now.isAfter(expireDate)) {
				throw new RuntimeException(ErrorCode.EXPIRED.getErrorMessage());
			}

			String solutionId = encryptPayloadData.solutionId();
			if (!systemUtil.getSolutionId().equals(solutionId)) {
				throw new RuntimeException(ErrorCode.SOLUTION_CODE_MISMATCH.getErrorMessage());
			}
			return true;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
