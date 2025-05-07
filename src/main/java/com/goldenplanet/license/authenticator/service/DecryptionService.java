package com.goldenplanet.license.authenticator.service;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.strategy.DecryptionStrategy;
import com.goldenplanet.license.authenticator.util.SecretKeyLoader;

import lombok.RequiredArgsConstructor;

/**
 * DecryptionService 클래스
 *
 * 암호화된 데이터를 복호화하는 서비스입니다.
 */
@Service
@RequiredArgsConstructor
public class DecryptionService {

	// SecretKey를 로드하는 유틸리티 클래스
	private final SecretKeyLoader secretKeyLoader;

	// 암호화 관련 속성을 보관하는 설정 클래스
	private final SecretProperties secretProperties;

	// 복호화를 처리하는 전략 클래스
	private final DecryptionStrategy decryptionStrategy;

	/**
	 * 암호화된 데이터를 복호화합니다.
	 *
	 * @param encryptedData 복호화할 데이터 (암호화된 문자열)
	 * @return String 복호화된 원본 데이터 문자열
	 * @throws InvalidLicenseException 복호화 실패 시 발생 (내부 오류 포함)
	 */
	public String decrypt(String encryptedData) {
		try {
			// SecretKey를 로드
			SecretKey secretKey = secretKeyLoader.loadSecretKey();

			// DecryptionStrategy를 사용하여 데이터 복호화
			return decryptionStrategy.decrypt(encryptedData, secretKey, secretProperties.getMode());
		} catch (Exception e) {
			// 복호화 실패 시 사용자 정의 예외를 발생
			throw new InvalidLicenseException("Decryption failed due to an internal error: " + e.getMessage());
		}
	}
}