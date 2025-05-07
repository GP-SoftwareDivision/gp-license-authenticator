package com.goldenplanet.license.authenticator.handler;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;

/**
 * AbstractLicenseHandler 클래스
 *
 * 이 클래스는 라이선스 검증 핸들러의 공통 동작을 정의하는 추상 클래스입니다.
 * 여러 핸들러를 체인 형태로 연결하여 순차적으로 처리합니다.
 */
public abstract class AbstractLicenseHandler implements LicenseHandler {

	// 다음 핸들러를 참조하기 위한 변수
	protected LicenseHandler nextHandler;

	/**
	 * 다음 핸들러를 설정합니다.
	 *
	 * @param nextHandler 다음으로 연결될 핸들러
	 */
	@Override
	public void setNextHandler(LicenseHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	/**
	 * 체인의 다음 핸들러를 호출합니다.
	 *
	 * @param data 처리될 EncryptPayloadData 객체
	 */
	protected void next(EncryptPayloadData data) {
		if (nextHandler != null) {
			nextHandler.handle(data);
		}
	}
}