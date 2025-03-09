# Building the APK with AppCenter

This document provides instructions for building the APK using Microsoft's AppCenter service.

## Steps to Build the APK

1. **Create an AppCenter account**
   - Sign up for AppCenter at: https://appcenter.ms/
   - It's free for basic usage

2. **Create a new app in AppCenter**
   - Click on "Add new app"
   - Name it "MinimalistPuzzleAdventure"
   - Select "Android" as the OS and "Java/Kotlin" as the platform
   - Click "Add new app"

3. **Connect your repository**
   - In your app dashboard, go to "Build"
   - Connect your GitHub, Bitbucket, or Azure DevOps repository
   - Select the repository containing your Minimalist Puzzle Adventure project

4. **Configure the build**
   - Select the branch you want to build (usually "main" or "master")
   - Configure the build:
     - Build variant: debug
     - Build frequency: Manual (or set up automatic builds)
     - Sign builds: No (for debug builds)
     - Enable the "Post-clone script" option and enter: `appcenter-post-clone.sh`
   - Click "Save & Build"

5. **Download the APK**
   - Once the build completes, click on the build
   - Click on "Download" to get the APK
   - The APK will be in the "apk" folder of the downloaded archive

6. **Install the APK on your Android device**
   - Transfer the downloaded APK to your Android device
   - On your Android device, navigate to the APK file and tap on it
   - Follow the prompts to install the app
   - You may need to enable "Install from unknown sources" in your device settings

## Troubleshooting

If you encounter any issues:
- Check the build logs in AppCenter for any error messages
- Make sure the `appcenter-post-clone.sh` script is in the root of your repository
- Ensure the script has executable permissions
- Verify that your Android device allows installation from unknown sources 