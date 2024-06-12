package com.goldenplanet.license.authenticator.handler;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;
import com.goldenplanet.license.authenticator.types.LicenseType;
import com.goldenplanet.license.authenticator.util.MacAddressMatcher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class MacAddressHandler extends AbstractLicenseHandler {
	private final MacAddressMatcher matchMacAddress;

	@Override
	public void handle(EncryptPayloadData data) {
		if (data.licenseType().equals(LicenseType.DEV.getDescription())) { // DEV는 MAC address를 확인하지 않습니다.
			return;
		}

		if (!matchMacAddress.matchMacAddress(data.macAddress())) {
			throw new InvalidLicenseException(ErrorCode.MAC_ADDRESS_MISMATCH.getErrorMessage());
		}
		super.next(data);
	}
}
