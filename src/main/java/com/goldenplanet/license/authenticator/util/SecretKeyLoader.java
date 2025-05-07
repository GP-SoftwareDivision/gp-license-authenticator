package com.goldenplanet.license.authenticator.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.config.SecretProperties;

import lombok.RequiredArgsConstructor;

/**
 * SecretKeyLoader 클래스
 *
 * 이 클래스는 암호화/복호화에 사용되는 SecretKey를 로드하는 데 책임이 있습니다.
 * 키는 클래스 경로에 저장된 파일에서 로드되며, Base64로 인코딩된 문자열을 디코딩하여 SecretKey 객체로 변환합니다.
 */
@Component
@RequiredArgsConstructor
public class SecretKeyLoader {

	// SecretKeyLoader에 필요한 속성을 제공하는 SecretProperties 의존성
	private final SecretProperties secretProperties;

	/**
	 * 키 파일에서 SecretKey를 로드합니다.
	 *
	 * @return SecretKey 복호화된 키를 SecretKey 객체로 반환합니다.
	 * @throws RuntimeException 키 파일을 읽는 데 오류가 발생할 경우 예외를 던집니다.
	 */
	public SecretKey loadSecretKey() {
		// 키 파일의 경로 정보를 ClassPathResource로 가져옵니다.
		Resource resource = new ClassPathResource(secretProperties.getKeyFilePath());
		try (InputStream inputStream = resource.getInputStream()) {
			// 키 파일 데이터를 읽어 바이트 배열로 변환
			byte[] keyBytes = inputStream.readAllBytes();
			// Base64 문자열을 SecretKey로 디코딩하여 반환
			return decodeBase64ToSecretKey(new String(keyBytes));
		} catch (IOException e) {
			// 파일을 읽을 수 없는 경우 RuntimeException 예외를 발생
			throw new RuntimeException("Failed to load key file from path: " + secretProperties.getKeyFilePath(), e);
		}
	}

	/**
	 * Base64로 인코딩된 문자열을 SecretKey로 변환합니다.
	 *
	 * @param base64Key Base64 형식으로 인코딩된 키 문자열
	 * @return SecretKey Base64 디코딩 후 생성된 SecretKey 객체
	 */
	private SecretKey decodeBase64ToSecretKey(String base64Key) {
		// Base64를 디코딩하여 바이트 배열로 변환
		byte[] decodedKey = Base64.getDecoder().decode(base64Key);
		// SecretKeySpec 객체를 생성하여 SecretKey로 반환
		return new SecretKeySpec(decodedKey, secretProperties.getAlgorithm());
	}
}