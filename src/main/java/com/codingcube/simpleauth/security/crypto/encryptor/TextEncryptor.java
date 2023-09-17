package com.codingcube.simpleauth.security.crypto.encryptor;

public interface TextEncryptor {

    /**
     * Encrypt the byte array.
     */
    String encrypt(String text);

    /**
     * Decrypt the byte array.
     */
    String decrypt(String encryptedText);
}
