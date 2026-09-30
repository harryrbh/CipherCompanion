package com.example.ciphertool.cipher;

final class CipherUtils {

    private CipherUtils() {
    }

    static int requireInt(String key, String fieldDescription) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(fieldDescription + " is required.");
        }
        try {
            return Integer.parseInt(key.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldDescription + " must be a whole number.");
        }
    }

    static String requireLettersOnly(String key, String fieldDescription) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(fieldDescription + " is required.");
        }
        String trimmed = key.trim();
        if (!trimmed.chars().allMatch(Character::isLetter)) {
            throw new IllegalArgumentException(fieldDescription + " must contain letters only.");
        }
        return trimmed;
    }
    
    static String requireNonEmpty(String key, String fieldDescription) {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException(fieldDescription + " is required.");
        }
        return key;
    }
}
