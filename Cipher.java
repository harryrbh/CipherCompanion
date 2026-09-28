package com.example.ciphertool.cipher;

//interface to describe generic cipher behaviour
public interface Cipher {

    String getName();

    //gets type of key (useful for validation)
    KeyType getKeyType();

    //describes type of key expected
    String getKeyHint();

    String encrypt(String plainText, String key);


    String decrypt(String cipherText, String key);

    @Override
    String toString();
}
