package com.goldenplanet.license.authenticator.handler;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class ExpiryDateHandler extends AbstractLicenseHandler {
	@Override
	public void handle(EncryptPayloadData data) {
		LocalDate now = LocalDate.now();
		LocalDate expireDate = LocalDate.parse(data.expiredDate());
		if (now.isAfter(expireDate)) {
			throw new InvalidLicenseException(ErrorCode.EXPIRED.getErrorMessage());
		}
		super.next(data);
	}
}
