#!/usr/bin/env bash

# Install dependencies
echo "Installing dependencies..."
sudo apt-get update
sudo apt-get install -y lib32z1 lib32stdc++6

# Fix Gradle wrapper
echo "Fixing Gradle wrapper..."
echo "distributionBase=GRADLE_USER_HOME" > gradle/wrapper/gradle-wrapper.properties
echo "distributionPath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties
echo "distributionUrl=https\://services.gradle.org/distributions/gradle-8.1.1-bin.zip" >> gradle/wrapper/gradle-wrapper.properties
echo "zipStoreBase=GRADLE_USER_HOME" >> gradle/wrapper/gradle-wrapper.properties
echo "zipStorePath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties

# Create local.properties
echo "Creating local.properties..."
echo "sdk.dir=$ANDROID_SDK_ROOT" > local.properties

# Build the APK
echo "Building APK..."
chmod +x ./gradlew
./gradlew assembleDebug --stacktrace

# Copy the APK to the output directory
echo "Copying APK to output directory..."
mkdir -p $APPCENTER_OUTPUT_DIRECTORY/apk
cp app/build/outputs/apk/debug/app-debug.apk $APPCENTER_OUTPUT_DIRECTORY/apk/

echo "Build completed successfully!" 