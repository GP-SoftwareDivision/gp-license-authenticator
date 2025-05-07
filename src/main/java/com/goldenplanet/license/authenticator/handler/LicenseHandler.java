package com.goldenplanet.license.authenticator.handler;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;

/**
 * LicenseHandler 인터페이스
 *
 * 라이선스 검증을 위한 핸들러를 정의하는 인터페이스입니다.
 * 핸들러는 체인으로 연결되어 순차적으로 실행됩니다.
 */
public interface LicenseHandler {

	/**
	 * 다음 핸들러를 설정합니다.
	 *
	 * @param nextHandler 다음 핸들러
	 */
	void setNextHandler(LicenseHandler nextHandler);

	/**
	 * 라이선스 데이터를 처리하는 메서드
	 *
	 * @param data 처리할 EncryptPayloadData 객체
	 */
	void handle(EncryptPayloadData data);
}