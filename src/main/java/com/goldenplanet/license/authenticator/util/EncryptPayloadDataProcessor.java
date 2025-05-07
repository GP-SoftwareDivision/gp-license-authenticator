package com.goldenplanet.license.authenticator.util;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.service.DecryptionService;

import lombok.RequiredArgsConstructor;

/**
 * 이 클래스는 라이선스 인증 요청을 처리하는 컴포넌트입니다.
 * 주어진 라이선스 키를 복호화하고, 복호화된 데이터를
 * EncryptPayloadData 객체로 변환합니다.
 */

@Component
@RequiredArgsConstructor
public class EncryptPayloadDataProcessor {
	// JSON 데이터를 객체로 변환하기 위한 ObjectMapper 인스턴스입니다.
	private final ObjectMapper objectMapper;
	// 라이선스 키를 복호화하는 데 사용되는 서비스입니다.
	private final DecryptionService decryptionService;

	/**
	 * LicenseAuthenticationRequest를 처리하여 복호화된 데이터를 기반으로
	 * EncryptPayloadData 객체를 생성합니다.
	 *
	 * @param request 라이선스 인증 요청 객체로, 암호화된 라이선스 키를 포함하고 있습니다.
	 * @return EncryptPayloadData 복호화된 데이터를 기반으로 생성된 객체를 반환합니다.
	 * @throws InvalidLicenseException 만약 복호화된 데이터를 파싱할 수 없는 경우 발생합니다.
	 */
	public EncryptPayloadData process(LicenseAuthenticationRequest request) {
		String decryptedData = decryptionService.decrypt(request.licenseKey());
		try {
			return objectMapper.readValue(decryptedData, EncryptPayloadData.class);
		} catch (JsonProcessingException e) {
			throw new InvalidLicenseException(e.getMessage());
		}
	}
}
