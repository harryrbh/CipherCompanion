package com.example.ciphertool.cipher;

/**
 * Repeats a keyword over the text and shifts each letter by
 * the corresponding keyword letter's alphabet position. Non-letters pass
 * through unchanged.
 */
public class VigenereCipher implements Cipher {

    @Override
    public String getName() {
        return "Vigenere Cipher";
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.LETTERS;
    }

    @Override
    public String getKeyHint() {
        return "Keyword (letters only, e.g. LEMON)";
    }

    @Override
    public String encrypt(String plainText, String key) {
        String cleanKey = CipherUtils.requireLettersOnly(key, "Keyword").toUpperCase();
        return process(plainText, cleanKey, true);
    }

    @Override
    public String decrypt(String cipherText, String key) {
        String cleanKey = CipherUtils.requireLettersOnly(key, "Keyword").toUpperCase();
        return process(cipherText, cleanKey, false);
    }

    private String process(String text, String key, boolean encrypting) {
        StringBuilder result = new StringBuilder(text.length());
        int keyIndex = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int keyShift = key.charAt(keyIndex % key.length()) - 'A';
                int shift = encrypting ? keyShift : -keyShift;
                if (Character.isUpperCase(c)) {
                    result.append((char) ('A' + Math.floorMod(c - 'A' + shift, 26)));
                } else {
                    result.append((char) ('a' + Math.floorMod(c - 'a' + shift, 26)));
                }
                keyIndex++;
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
