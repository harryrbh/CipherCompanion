package com.example.ciphertool.cipher;

// maps A-Z to Z-A
public class AtbashCipher implements Cipher {

    @Override
    public String getName() {
        return "Atbash Cipher";
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.NONE;
    }

    @Override
    public String getKeyHint() {
        return "No key needed";
    }

    @Override
    public String encrypt(String plainText, String key) {
        return transform(plainText);
    }

    @Override
    public String decrypt(String cipherText, String key) {
        return transform(cipherText); // self-inverse
    }

    private String transform(String text) {
        StringBuilder result = new StringBuilder(text.length());
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append((char) ('Z' - (c - 'A')));
            } else if (Character.isLowerCase(c)) {
                result.append((char) ('z' - (c - 'a')));
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
