package com.codingcube.simpleauth.security.crypto.password;

public interface PasswordEncoder {
    String encode(CharSequence password);

    boolean matches(CharSequence password, String passwordEncodeString);

    default boolean upgradeEncoding(String encodedPassword) {
        return false;
    }
}