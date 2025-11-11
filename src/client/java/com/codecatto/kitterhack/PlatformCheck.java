package com.codecatto.kitterhack;

import com.codecatto.kitterhack.util.StringEncryptor;
import java.io.*;
import java.nio.file.*;

public class PlatformCheck {
    // Encrypted strings - not obvious what they are
    private static final String ENC_LIB_NAME = "JwAWHwwGHAQRAyoKHysVHgkVBQQ5BA=="; // "libkitterhack_platform"
    private static final String ENC_ERROR_MSG = "AAAAAAAAAAAAAGUKF1QMAUgODQcySQcBFQIHExcOL0kbGkU+AQ8WE2sIGhBFMwYFEQQiDVQECRMcBwwZJhpa"; // Error message
    private static final String ENC_PROC_VERSION = "ZBkGGwZdHgQRGCIGGg=="; // "/proc/version"
    private static final String ENC_SYS_KERNEL = "ZBoNB0oZDRMNDic="; // "/sys/kernel"
    private static final String ENC_NOT_FOUND = "BQgAHRMXSA0KCTkIBg1FHAcVQw0kHBoQ"; // "Native library not found"
    private static final String ENC_LOAD_FAILED = "DQgdGAAWSBUMSycGFRBFHAkVCh0uSRgdBwAJExo="; // "Failed to load native library"
    private static final String ENC_INITIALIZED = "AAAAAAAAAAAAAGUKF1QMHAEVCgonAA4RAVM="; // "Kitterhack.cc initialized!"

    private static boolean initialized = false;

    static {
        try {
            loadNativeLibrary();
            if (!validatePlatform()) {
                corruptAndExit();
            }
            initialized = true;
        } catch (UnsatisfiedLinkError e) {
            System.err.println(StringEncryptor.decrypt(ENC_ERROR_MSG));
            corruptAndExit();
        } catch (Exception e) {
            e.printStackTrace();
            corruptAndExit();
        }
    }

    private static void loadNativeLibrary() {
        try {
            System.loadLibrary(StringEncryptor.decrypt(ENC_LIB_NAME));
        } catch (UnsatisfiedLinkError e) {
            loadFromJar();
        }
    }

    private static void loadFromJar() {
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();

        String libraryPath;
        String libName = StringEncryptor.decrypt(ENC_LIB_NAME) + ".so";

        if (os.contains("linux")) {
            if (arch.contains("aarch64") || arch.contains("arm")) {
                libraryPath = "/native/linux/aarch64/" + libName;
            } else {
                libraryPath = "/native/linux/x86_64/" + libName;
            }
        } else if (os.contains("android")) {
            libraryPath = "/native/android/" + arch + "/" + libName;
        } else {
            throw new UnsatisfiedLinkError(StringEncryptor.decrypt(ENC_NOT_FOUND));
        }

        try {
            InputStream in = PlatformCheck.class.getResourceAsStream(libraryPath);
            if (in == null) {
                throw new UnsatisfiedLinkError(StringEncryptor.decrypt(ENC_NOT_FOUND) + ": " + libraryPath);
            }

            File tempLib = File.createTempFile(StringEncryptor.decrypt(ENC_LIB_NAME), ".so");
            tempLib.deleteOnExit();

            Files.copy(in, tempLib.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();

            System.load(tempLib.getAbsolutePath());
        } catch (IOException e) {
            throw new UnsatisfiedLinkError(StringEncryptor.decrypt(ENC_LOAD_FAILED) + ": " + e.getMessage());
        }
    }

    private static native boolean validatePlatform();
    private static native String getKernelVersion();
    private static native boolean checkProcFS();

    private static void corruptAndExit() {
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

    public static String getSystemInfo() {
        if (!initialized) return "Invalid platform";
        return "Kernel: " + getKernelVersion() + " | ProcFS: " + checkProcFS();
    }
}