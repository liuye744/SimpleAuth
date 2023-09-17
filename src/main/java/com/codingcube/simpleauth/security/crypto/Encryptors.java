package com.codingcube.simpleauth.security.crypto;

import com.codingcube.simpleauth.security.crypto.encryptor.ByteEncryptor;
import com.codingcube.simpleauth.security.crypto.encryptor.TextEncryptor;
import com.codingcube.simpleauth.security.crypto.encryptor.impl.AesByteEncryptor;
import com.codingcube.simpleauth.security.crypto.encryptor.impl.TextAesEncryptor;

public class Encryptors {
    public static ByteEncryptor stronger(String password, String salt){
        return new AesByteEncryptor(password, salt);
    }

    public static TextEncryptor text(String password, String salt) {
        return new TextAesEncryptor(stronger(password, salt));
    }
}
