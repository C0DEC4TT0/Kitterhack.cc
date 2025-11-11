package com.codecatto.kitterhack;

import java.io.*;
import java.nio.file.*;

public class PlatformCheck {
    private static final String LIB_NAME = "kitterhack_platform";
    private static boolean initialized = false;

    static {
        try {
            loadNativeLibrary();
            if (!validatePlatform()) {
                corruptAndExit();
            }
            initialized = true;
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Kitterhack.cc is only supported on Linux and Android platforms.");
            corruptAndExit();
        } catch (Exception e) {
            e.printStackTrace();
            corruptAndExit();
        }
    }

    private static void loadNativeLibrary() {
        try {
            // Try to load from java.library.path first
            System.loadLibrary(LIB_NAME);
        } catch (UnsatisfiedLinkError e) {
            // Extract from JAR and load
            loadFromJar();
        }
    }

    private static void loadFromJar() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        String libraryPath;
        if (os.contains("linux")) {
            if (arch.contains("aarch64") || arch.contains("arm")) {
                libraryPath = "/native/linux/aarch64/libkitterhack_platform.so";
            } else {
                libraryPath = "/native/linux/x86_64/libkitterhack_platform.so";
            }
        } else if (os.contains("android")) {
            libraryPath = "/native/android/" + arch + "/libkitterhack_platform.so";
        } else {
            throw new UnsatisfiedLinkError("Unsupported platform: " + os);
        }

        try {
            InputStream in = PlatformCheck.class.getResourceAsStream(libraryPath);
            if (in == null) {
                throw new UnsatisfiedLinkError("Native library not found: " + libraryPath);
            }

            // Create temp file
            File tempLib = File.createTempFile("libkitterhack_platform", ".so");
            tempLib.deleteOnExit();

            // Copy to temp file
            Files.copy(in, tempLib.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            // Load the library
            System.load(tempLib.getAbsolutePath());
        } catch (IOException e) {
            throw new UnsatisfiedLinkError("Failed to load native library: " + e.getMessage());
        }
    }

    // Native method declarations
    private static native boolean validatePlatform();
    private static native String getKernelVersion();
    private static native boolean checkProcFS();

    private static void corruptAndExit() {
        // Make it annoying to bypass
        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {}
        System.exit(1);
    }

    public static void ensureValid() {
        if (!initialized) {
            corruptAndExit();
        }
    }

    // Optional: Public methods for debugging (remove in production)
    public static String getSystemInfo() {
        if (!initialized) return "Invalid platform";
        return "Kernel: " + getKernelVersion() + " | ProcFS: " + checkProcFS();
    }
}