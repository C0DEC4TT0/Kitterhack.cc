package com.codecatto.kitterhack.mixin.client;

import com.codecatto.kitterhack.PlatformCheck;
import net.fabricmc.api.ClientModInitializer;

public class KitterhackClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Platform check happens immediately
        PlatformCheck.ensureValid();

        // Your existing initialization code here
        System.out.println("Kitterhack.cc initialized!");
        System.out.println("System: " + PlatformCheck.getSystemInfo());

        // Rest of your mod initialization...
    }
}