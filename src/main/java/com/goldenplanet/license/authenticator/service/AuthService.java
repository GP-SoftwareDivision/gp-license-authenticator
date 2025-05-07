package com.goldenplanet.license.authenticator.service;

import org.springframework.stereotype.Service;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.dto.LicenseAuthenticationRequest;
import com.goldenplanet.license.authenticator.handler.LicenseHandlerChain;
import com.goldenplanet.license.authenticator.util.EncryptPayloadDataProcessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * AuthService 클래스
 *
 * 이 클래스는 라이선스 인증 절차를 처리하는 서비스입니다.
 * LicenseAuthenticationRequest를 검증하고 라이선스 체인을 실행합니다.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

	// 라이선스 검증 처리를 담당하는 핸들러 체인
	private final LicenseHandlerChain licenseHandlerChain;

	// 라이선스 키를 복호화하고 처리하는 유틸리티
	private final EncryptPayloadDataProcessor encryptPayloadDataProcessor;

	/**
	 * 라이선스 키의 유효성을 검사합니다.
	 *
	 * @param licenseAuthenticationRequest 라이선스 인증 요청 객체
	 * @return boolean 라이선스 키 체크 성공 여부 (항상 true)
	 */
	public boolean checkLicenseKey(LicenseAuthenticationRequest licenseAuthenticationRequest) {
		// 요청 데이터를 복호화 및 처리하여 EncryptPayloadData 객체 생성
		EncryptPayloadData encryptPayloadData = encryptPayloadDataProcessor.process(licenseAuthenticationRequest);

		// 라이선스 검증 핸들러 체인을 실행
		licenseHandlerChain.execute(encryptPayloadData);

		// 검증 성공 시 true 반환
		return true;
	}
}