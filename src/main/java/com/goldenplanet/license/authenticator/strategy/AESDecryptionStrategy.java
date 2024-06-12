package com.goldenplanet.license.authenticator.strategy;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

public class AESDecryptionStrategy implements DecryptionStrategy {
	@Override
	public String decrypt(String encryptedData, SecretKey key, String mode) throws Exception {
		Cipher cipher = Cipher.getInstance(mode);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] decodedData = Base64.getDecoder().decode(encryptedData);
		byte[] decrypted = cipher.doFinal(decodedData);
		return new String(decrypted);
	}
}
