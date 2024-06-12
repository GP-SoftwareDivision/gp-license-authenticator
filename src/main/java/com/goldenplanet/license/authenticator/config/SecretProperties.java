package com.goldenplanet.license.authenticator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "secret")
public class SecretProperties {
	private String solutionId;
	private String mode;
	private String algorithm;
	private String keyFilePath;
}
