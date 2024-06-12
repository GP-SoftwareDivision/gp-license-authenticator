package com.goldenplanet.license.authenticator.handler;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;

public interface LicenseHandler {
	void setNextHandler(LicenseHandler nextHandler);

	void handle(EncryptPayloadData data);
}
