#!/bin/bash

# Detect Java home
if [ -z "$JAVA_HOME" ]; then
    echo "JAVA_HOME not set, trying to detect..."
    JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
fi

echo "Using JAVA_HOME: $JAVA_HOME"

# Create output directories
mkdir -p src/client/resources/native/linux/x86_64
mkdir -p src/client/resources/native/linux/aarch64

# Compile for x86_64
echo "Compiling for x86_64..."
gcc -shared -fPIC -O2 \
    -I"$JAVA_HOME/include" \
    -I"$JAVA_HOME/include/linux" \
    -o src/client/resources/native/linux/x86_64/libkitterhack_platform.so \
    src/native/linux/platform_check.c

if [ $? -eq 0 ]; then
    echo "✓ x86_64 build successful"
else
    echo "✗ x86_64 build failed"
    exit 1
fi

# Compile for ARM64 (if cross-compiler available)
if command -v aarch64-linux-gnu-gcc &> /dev/null; then
    echo "Compiling for aarch64..."
    aarch64-linux-gnu-gcc -shared -fPIC -O2 \
        -I"$JAVA_HOME/include" \
        -I"$JAVA_HOME/include/linux" \
        -o src/client/resources/native/linux/aarch64/libkitterhack_platform.so \
        src/native/linux/platform_check.c

    if [ $? -eq 0 ]; then
        echo "✓ aarch64 build successful"
    else
        echo "✗ aarch64 build failed"
    fi
else
    echo "⊘ Skipping aarch64 build (cross-compiler not found)"
fi

echo ""
echo "Native libraries built successfully!"
ls -lh src/client/resources/native/linux/*/libkitterhack_platform.so