#include <jni.h>
#include <stdio.h>
#include <string.h>
#include <sys/utsname.h>
#include <unistd.h>
#include <sys/stat.h>

// Validate this is actually a Linux system
JNIEXPORT jboolean JNICALL Java_com_codecatto_kitterhack_PlatformCheck_validatePlatform(JNIEnv *env, jclass cls) {
    struct utsname buffer;

    // Check uname (Linux-specific syscall)
    if (uname(&buffer) != 0) {
        return JNI_FALSE;
    }

    // Verify it's actually Linux
    if (strstr(buffer.sysname, "Linux") == NULL) {
        return JNI_FALSE;
    }

    // Check for /proc filesystem (Linux-specific)
    struct stat st;
    if (stat("/proc/version", &st) != 0) {
        return JNI_FALSE;
    }

    // Additional check: verify /sys exists (Linux-specific)
    if (stat("/sys/kernel", &st) != 0) {
        return JNI_FALSE;
    }

    return JNI_TRUE;
}

// Get kernel version
JNIEXPORT jstring JNICALL Java_com_codecatto_kitterhack_PlatformCheck_getKernelVersion(JNIEnv *env, jclass cls) {
    struct utsname buffer;

    if (uname(&buffer) != 0) {
        return (*env)->NewStringUTF(env, "Unknown");
    }

    char version[256];
    snprintf(version, sizeof(version), "%s %s", buffer.sysname, buffer.release);

    return (*env)->NewStringUTF(env, version);
}

// Check if /proc filesystem is accessible
JNIEXPORT jboolean JNICALL Java_com_codecatto_kitterhack_PlatformCheck_checkProcFS(JNIEnv *env, jclass cls) {
    struct stat st;
    return (stat("/proc", &st) == 0 && S_ISDIR(st.st_mode)) ? JNI_TRUE : JNI_FALSE;
}