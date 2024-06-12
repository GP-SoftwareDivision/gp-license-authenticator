package com.goldenplanet.license.authenticator.handler;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;

public abstract class AbstractLicenseHandler implements LicenseHandler {
	protected LicenseHandler nextHandler;

	@Override
	public void setNextHandler(LicenseHandler nextHandler) {
		this.nextHandler = nextHandler;
	}

	protected void next(EncryptPayloadData data) {
		if (nextHandler != null) {
			nextHandler.handle(data);
		}
	}
}
