package com.codingcube.simpleauth.security.crypto.encryptor;

public interface ByteEncryptor {

    /**
     * Encrypt the byte array.
     */
    byte[] encrypt(byte[] byteArray);

    /**
     * Decrypt the byte array.
     */
    byte[] decrypt(byte[] encryptedByteArray);
}
