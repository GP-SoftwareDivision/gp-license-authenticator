package com.goldenplanet.license.authenticator.handler;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.config.SecretProperties;
import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;
import com.goldenplanet.license.authenticator.exception.ErrorCode;
import com.goldenplanet.license.authenticator.exception.InvalidLicenseException;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
class SolutionIdHandler extends AbstractLicenseHandler {
	private final SecretProperties secretProperties;

	@Override
	public void handle(EncryptPayloadData data) {
		if (!secretProperties.getSolutionId().equals(data.solutionId())) {
			throw new InvalidLicenseException(ErrorCode.SOLUTION_CODE_MISMATCH.getErrorMessage());
		}
		super.next(data);
	}
}
