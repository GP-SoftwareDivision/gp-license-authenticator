package com.goldenplanet.license.authenticator.types;

import lombok.Getter;

/**
 * LicenseType 열거형
 *
 * 이 enum 클래스는 라이선스의 유형(개발 또는 운영)을 정의합니다.
 * 각 열거형 값은 설명(description)을 포함하고 있습니다.
 */
@Getter
public enum LicenseType {

	/**
	 * 개발(임시) 라이선스를 나타냄
	 */
	DEV("임시"),

	/**
	 * 운영 라이선스를 나타냄
	 */
	PROD("운영");

	/**
	 * 각 라이선스 타입에 대한 설명
	 */
	private final String description;

	/**
	 * LicenseType 생성자
	 *
	 * @param description 라이선스 타입에 대한 설명 문자열
	 */
	LicenseType(String description) {
		this.description = description; // 생성 시 받아온 설명을 필드에 저장
	}

}