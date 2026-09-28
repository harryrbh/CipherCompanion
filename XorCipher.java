package com.example.ciphertool.cipher;

import java.nio.charset.StandardCharsets;

public class XorCipher implements Cipher {

    @Override
    public String getName() {
        return "XOR Cipher";
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.ANY_TEXT;
    }

    @Override
    public String getKeyHint() {
        return "Any text key; output is hex (paste hex back in to decrypt)";
    }

    @Override
    public String encrypt(String plainText, String key) {
        String cleanKey = CipherUtils.requireNonEmpty(key, "Key");
        byte[] textBytes = plainText.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = cleanKey.getBytes(StandardCharsets.UTF_8);
        byte[] out = xor(textBytes, keyBytes);
        return toHex(out);
    }

    @Override
    public String decrypt(String cipherText, String key) {
        String cleanKey = CipherUtils.requireNonEmpty(key, "Key");
        byte[] cipherBytes;
        try {
            cipherBytes = fromHex(cipherText.trim());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Ciphertext must be valid hex (as produced by XOR encryption).");
        }
        byte[] keyBytes = cleanKey.getBytes(StandardCharsets.UTF_8);
        byte[] out = xor(cipherBytes, keyBytes);
        return new String(out, StandardCharsets.UTF_8);
    }

    private byte[] xor(byte[] data, byte[] key) {
        byte[] result = new byte[data.length];
        for (int i = 0; i < data.length; i++) {
            result[i] = (byte) (data[i] ^ key[i % key.length]);
        }
        return result;
    }

    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private byte[] fromHex(String hex) {
        if (hex.isEmpty() || hex.length() % 2 != 0) {
            throw new IllegalArgumentException("Invalid hex length.");
        }
        byte[] out = new byte[hex.length() / 2];
        for (int i = 0; i < out.length; i++) {
            int hi = Character.digit(hex.charAt(i * 2), 16);
            int lo = Character.digit(hex.charAt(i * 2 + 1), 16);
            if (hi < 0 || lo < 0) {
                throw new IllegalArgumentException("Invalid hex character.");
            }
            out[i] = (byte) ((hi << 4) + lo);
        }
        return out;
    }

    @Override
    public String toString() {
        return getName();
    }
}
