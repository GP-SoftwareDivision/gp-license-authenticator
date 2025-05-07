package com.goldenplanet.license.authenticator.handler;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;

import lombok.RequiredArgsConstructor;

/**
 * ExpiryDateHandler 클래스
 *
 * 라이선스의 만료 날짜를 검증하는 핸들러입니다.
 * 만료된 경우 예외를 던지며, 만료되지 않았다면 체인의 다음 핸들러를 호출합니다.
 */
@Component
@RequiredArgsConstructor
class ExpiryDateHandler extends AbstractLicenseHandler {

	/**
	 * 만료 날짜를 검증하는 메서드
	 *
	 * @param data EncryptPayloadData 객체 (검증에 필요한 데이터 포함)
	 * @throws InvalidLicenseException 만료된 라이선스가 감지되었을 경우 발생
	 */
	@Override
	public void handle(EncryptPayloadData data) {
		LocalDate now = LocalDate.now();
		LocalDate expireDate = LocalDate.parse(data.expiredDate());

		// 현재 날짜가 만료 날짜를 초과한 경우 예외를 발생시킴
		if (now.isAfter(expireDate)) {
			throw new InvalidLicenseException(ErrorCode.EXPIRED.getErrorMessage());
		}

		// 검증이 성공하면 다음 핸들러 호출
		super.next(data);
	}
}