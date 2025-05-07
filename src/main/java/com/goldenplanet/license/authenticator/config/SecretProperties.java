package com.goldenplanet.license.authenticator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * SecretProperties 클래스
 *
 * 외부 설정 파일(application.yml 또는 application.properties)에 정의된
 * 암호화/복호화 관련 설정을 매핑하는 클래스입니다.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "secret") // "secret"으로 시작하는 설정 값을 매핑
public class SecretProperties {

	/**
	 * 솔루션 ID
	 * - 라이선스 인증에 사용되는 고유 식별자
	 */
	private String solutionId;

	/**
	 * 모드 설정
	 * - 암호화 또는 복호화 모드를 정의 (예: AES, RSA 등)
	 */
	private String mode;

	/**
	 * 암호화 알고리즘
	 * - 사용 중인 암호화 알고리즘의 이름 (예: AES)
	 */
	private String algorithm;

	/**
	 * 키 파일 경로
	 * - 암호화/복호화에 사용하는 키 파일의 위치
	 */
	private String keyFilePath;
}