package com.goldenplanet.license.authenticator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goldenplanet.license.authenticator.strategy.AESDecryptionStrategy;
import com.goldenplanet.license.authenticator.strategy.DecryptionStrategy;

/**
 * EncryptConfig 클래스
 *
 * 암호화/복호화 관련 Bean을 설정하는 Spring Configuration 클래스입니다.
 */
@Configuration
public class EncryptConfig {

	/**
	 * DecryptionStrategy Bean 등록
	 *
	 * @return AES 기반의 DecryptionStrategy 구현체를 반환합니다.
	 */
	@Bean
	public DecryptionStrategy decryptionStrategy() {
		return new AESDecryptionStrategy();
	}
}