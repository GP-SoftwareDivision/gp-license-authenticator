package com.goldenplanet.license.authenticator.handler;

import org.springframework.stereotype.Component;

import com.goldenplanet.license.authenticator.dto.EncryptPayloadData;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LicenseHandlerChain {
	private final ExpiryDateHandler expiryDateHandler;
	private final SolutionIdHandler solutionIdHandler;
	private final MacAddressHandler macAddressHandler;

	@PostConstruct
	public void init() {
		expiryDateHandler.setNextHandler(solutionIdHandler);
		solutionIdHandler.setNextHandler(macAddressHandler);
	}

	public void execute(EncryptPayloadData encryptPayloadData) {
		expiryDateHandler.handle(encryptPayloadData);
	}
}
