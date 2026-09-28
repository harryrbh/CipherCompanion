package com.example.ciphertool.cipher;

public class Rot13Cipher implements Cipher {
//Caesar cipher with key 13
    private final CaesarCipher caesar = new CaesarCipher();

    @Override
    public String getName() {
        return "ROT13";
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
        return caesar.encrypt(plainText, "13");
    }

    @Override
    public String decrypt(String cipherText, String key) {
        return caesar.encrypt(cipherText, "13");
    }

    @Override
    public String toString() {
        return getName();
    }
}
