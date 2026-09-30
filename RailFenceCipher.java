package com.example.ciphertool.cipher;

public class RailFenceCipher implements Cipher {

    @Override
    public String getName() {
        return "Rail Fence Cipher";
    }

    @Override
    public KeyType getKeyType() {
        return KeyType.INTEGER;
    }

    @Override
    public String getKeyHint() {
        return "Number of rails (integer, 2 or more)";
    }

    @Override
    public String encrypt(String plainText, String key) {
        int rails = requireRails(key);
        if (plainText.isEmpty() || rails == 1) {
            return plainText;
        }
        int[] railOf = railSequence(plainText.length(), rails);

        StringBuilder[] fence = new StringBuilder[rails];
        for (int r = 0; r < rails; r++) {
            fence[r] = new StringBuilder();
        }
        for (int i = 0; i < plainText.length(); i++) {
            fence[railOf[i]].append(plainText.charAt(i));
        }

        StringBuilder result = new StringBuilder(plainText.length());
        for (StringBuilder row : fence) {
            result.append(row);
        }
        return result.toString();
    }

    @Override
    public String decrypt(String cipherText, String key) {
        int rails = requireRails(key);
        int length = cipherText.length();
        if (length == 0 || rails == 1) {
            return cipherText;
        }
        int[] railOf = railSequence(length, rails);

        int[] countPerRail = new int[rails];
        for (int r : railOf) {
            countPerRail[r]++;
        }

        // Slice cipherText into per-rail segments, in the same order encrypt concatenated them.
        String[] segment = new String[rails];
        int pos = 0;
        for (int r = 0; r < rails; r++) {
            segment[r] = cipherText.substring(pos, pos + countPerRail[r]);
            pos += countPerRail[r];
        }

        int[] nextIndexInRail = new int[rails];
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int rail = railOf[i];
            result.append(segment[rail].charAt(nextIndexInRail[rail]));
            nextIndexInRail[rail]++;
        }
        return result.toString();
    }

    /** Returns which rail (row) each character position belongs to, for the given length/rail count. */
    private int[] railSequence(int length, int rails) {
        int[] railOf = new int[length];
        int currentRail = 0;
        boolean goingDown = true;
        for (int i = 0; i < length; i++) {
            railOf[i] = currentRail;
            if (currentRail == 0) {
                goingDown = true;
            } else if (currentRail == rails - 1) {
                goingDown = false;
            }
            currentRail += goingDown ? 1 : -1;
        }
        return railOf;
    }

    private int requireRails(String key) {
        int rails = CipherUtils.requireInt(key, "Number of rails");
        if (rails < 2) {
            throw new IllegalArgumentException("Number of rails must be 2 or more.");
        }
        return rails;
    }

    @Override
    public String toString() {
        return getName();
    }
}
