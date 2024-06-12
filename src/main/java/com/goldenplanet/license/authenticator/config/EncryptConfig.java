package com.goldenplanet.license.authenticator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.goldenplanet.license.authenticator.strategy.AESDecryptionStrategy;
import com.goldenplanet.license.authenticator.strategy.DecryptionStrategy;

@Configuration
public class EncryptConfig {

	@Bean
	public DecryptionStrategy decryptionStrategy() {
		return new AESDecryptionStrategy();
	}
}
