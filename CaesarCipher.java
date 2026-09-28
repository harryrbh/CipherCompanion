package com.example.ciphertool.cipher;

public class CaesarCipher implements Cipher {

    @Override
    public String getName() {
        return "Caesar Cipher";
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.INTEGER;
    }

    @Override
    public String getKeyHint() {
        return "Shift amount (any integer, e.g. 3)";
    }

    @Override
    public String encrypt(String plainText, String key) {
        int shift = CipherUtils.requireInt(key, "Shift amount");
        return shift(plainText, shift);
    }

    @Override
    public String decrypt(String cipherText, String key) {
        int shift = CipherUtils.requireInt(key, "Shift amount");
        return shift(cipherText, -shift);
    }

    private String shift(String text, int shift) {
        int normalized = ((shift % 26) + 26) % 26;
        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append((char) ('A' + (c - 'A' + normalized) % 26));
            } else if (Character.isLowerCase(c)) {
                result.append((char) ('a' + (c - 'a' + normalized) % 26));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
