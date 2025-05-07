package com.goldenplanet.license.authenticator.handler;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.types.LicenseType;
import com.goldenplanet.license.authenticator.util.MacAddressMatcher;

import lombok.RequiredArgsConstructor;

/**
 * MacAddressHandler 클래스
 *
 * 라이선스 요청의 MAC 주소를 검증하는 핸들러입니다.
 * 운영(Production) 라이선스를 위한 MAC 주소 일치를 확인합니다.
 */
@Component
@RequiredArgsConstructor
class MacAddressHandler extends AbstractLicenseHandler {

	// MAC 주소 검증 유틸리티
	private final MacAddressMatcher matchMacAddress;

	/**
	 * MAC 주소를 검증하는 메서드
	 *
	 * @param data EncryptPayloadData 객체 (검증할 데이터 포함)
	 * @throws InvalidLicenseException MAC 주소 불일치 시 발생
	 */
	@Override
	public void handle(EncryptPayloadData data) {
		// 개발 환경(DEV)에서는 MAC 주소 검증을 생략
		if (data.licenseType().equals(LicenseType.DEV.getDescription())) {
			return;
		}

		// MAC 주소가 일치하지 않을 경우 예외를 발생
		if (!matchMacAddress.matchMacAddress(data.macAddress())) {
			throw new InvalidLicenseException(ErrorCode.MAC_ADDRESS_MISMATCH.getErrorMessage());
		}

		// 검증 성공 시 다음 핸들러 호출
		super.next(data);
	}
}