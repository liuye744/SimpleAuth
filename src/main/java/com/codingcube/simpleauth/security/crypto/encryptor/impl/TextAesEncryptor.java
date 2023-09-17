package com.codingcube.simpleauth.security.crypto.encryptor.impl;

import com.codingcube.simpleauth.security.crypto.encryptor.ByteEncryptor;
import com.codingcube.simpleauth.security.crypto.encryptor.TextEncryptor;

import java.nio.charset.StandardCharsets;

public class TextAesEncryptor implements TextEncryptor {
    private final ByteEncryptor byteEncryptor;

    public TextAesEncryptor(ByteEncryptor byteEncryptor) {
        this.byteEncryptor = byteEncryptor;
    }

    @Override
    public String encrypt(String text) {
        return new String(byteEncryptor.encrypt(text.getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public String decrypt(String encryptedText) {
        return new String(byteEncryptor.encrypt(encryptedText.getBytes(StandardCharsets.UTF_8)));
    }
}
