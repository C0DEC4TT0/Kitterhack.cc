package com.codecatto.kitterhack;

import com.codecatto.kitterhack.util.StringEncryptor;
import net.fabricmc.api.ClientModInitializer;

public class KitterhackClient implements ClientModInitializer {
    private static final String ENC_INITIALIZED = "AAAAAAAAAAAAAGUKF1QMHAEVCgonAA4RAVM=";

    @Override
    public void onInitializeClient() {
        // Platform check - will exit immediately if not Linux/Android
        PlatformCheck.ensureValid();

        System.out.println(StringEncryptor.decrypt(ENC_INITIALIZED));
        System.out.println("Running on: " + PlatformCheck.getSystemInfo());

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}