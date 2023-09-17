package com.codingcube.simpleauth.security.crypto.encryptor.impl;

import com.codingcube.simpleauth.security.crypto.encryptor.ByteEncryptor;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;

public class AesByteEncryptor implements ByteEncryptor {
    private static final int GCM_NONCE_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16; // 128 bits
    private final String password;
    private final String salt;

    public AesByteEncryptor(String password, String salt){
        this.password = password;
        this.salt = salt;
    }

    @Override
    public byte[] encrypt(byte[] byteArray) {
        // Generate a random 12-byte nonce (IV)
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[GCM_NONCE_LENGTH];
        secureRandom.nextBytes(iv);

        // Derive a 256-bit AES key from the password and salt using PBKDF2
        SecretKey key = null;
        try {
            key = deriveKey(password, salt);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create GCM parameter specification
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

        // Initialize the cipher for encryption
        Cipher encryptCipher = null;
        try {
            encryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            assert encryptCipher != null;
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        } catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        // Encrypt the plaintext
        byte[] cipherText = new byte[0];
        try {
            cipherText = encryptCipher.doFinal(byteArray);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        // Combine the IV and ciphertext
        byte[] encryptedData = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(cipherText, 0, encryptedData, iv.length, cipherText.length);

        // Encode the encrypted data in Base64
        return encryptedData;
    }

    @Override
    public byte[] decrypt(byte[] encryptedByteArray) {
        try {
            // Extract the IV and ciphertext
            byte[] iv = new byte[GCM_NONCE_LENGTH];
            byte[] cipherText = new byte[encryptedByteArray.length - GCM_NONCE_LENGTH];
            System.arraycopy(encryptedByteArray, 0, iv, 0, GCM_NONCE_LENGTH);
            System.arraycopy(encryptedByteArray, GCM_NONCE_LENGTH, cipherText, 0, cipherText.length);

            // Derive a 256-bit AES key from the password and salt using PBKDF2
            SecretKey key = deriveKey(password, salt);

            // Create GCM parameter specification
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);

            // Initialize the cipher for decryption
            Cipher decryptCipher = Cipher.getInstance("AES/GCM/NoPadding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);

            // Decrypt the ciphertext
            return decryptCipher.doFinal(cipherText);
        }catch (Exception e){
            e.printStackTrace();

        }
        return new byte[0];
    }

    // Derive a 256-bit AES key from the password and salt using PBKDF2
    private static SecretKey deriveKey(String password, String salt) throws Exception {
        int iterationCount = 10000;
        int keyLength = 256; // 256 bits
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt.getBytes(StandardCharsets.UTF_8), iterationCount, keyLength);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }
}
