package com.goldenplanet.license.authenticator.strategy;

import javax.crypto.SecretKey;

public interface DecryptionStrategy {
	String decrypt(String encryptedData, SecretKey key, String mode) throws Exception;
}
