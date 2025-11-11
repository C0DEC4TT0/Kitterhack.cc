package com.codecatto.kitterhack;

import net.fabricmc.api.ClientModInitializer;

public class KitterhackClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Platform check - will exit immediately if not Linux/Android
        PlatformCheck.ensureValid();

        System.out.println("Kitterhack.cc initialized!");
        System.out.println("Running on: " + PlatformCheck.getSystemInfo());

        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
    }
}