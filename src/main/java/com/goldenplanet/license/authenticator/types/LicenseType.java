package com.goldenplanet.license.authenticator.types;

import lombok.Getter;

@Getter
public enum LicenseType {
	DEV("임시"),
	PROD("운영");
	private final String description;

	LicenseType(String description) {
		this.description = description;
	}

}
