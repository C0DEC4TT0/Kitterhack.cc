package com.codecatto.kitterhack.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class StringEncryptor {
    // XOR key - change this to something unique for your mod
    private static final byte[] KEY = {0x4B, 0x69, 0x74, 0x74, 0x65, 0x72, 0x68, 0x61, 0x63, 0x6B};

    /**
     * Encrypt a string at compile time (use this when you're writing code)
     * Run this method to get encrypted strings, then paste them into your code
     */
    public static String encrypt(String plaintext) {
        byte[] input = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] output = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            output[i] = (byte) (input[i] ^ KEY[i % KEY.length]);
        }

        return Base64.getEncoder().encodeToString(output);
    }

    /**
     * Decrypt a string at runtime
     */
    public static String decrypt(String encrypted) {
        try {
            byte[] input = Base64.getDecoder().decode(encrypted);
            byte[] output = new byte[input.length];

            for (int i = 0; i < input.length; i++) {
                output[i] = (byte) (input[i] ^ KEY[i % KEY.length]);
            }

            return new String(output, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Helper main method to encrypt strings during development
     */
    public static void main(String[] args) {
        // Use this to generate encrypted strings
        System.out.println("Encrypting strings...\n");

        String[] toEncrypt = {
                "Kitterhack.cc is only supported on Linux and Android platforms.",
                "Kitterhack.cc initialized!",
                "/proc/version",
                "/sys/kernel",
                "libkitterhack_platform",
                "Native library not found",
                "Failed to load native library"
        };

        for (String s : toEncrypt) {
            System.out.println("Original: " + s);
            System.out.println("Encrypted: " + encrypt(s));
            System.out.println("Verify: " + decrypt(encrypt(s)));
            System.out.println();
        }
    }
}