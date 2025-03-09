# Building the Minimalist Puzzle Adventure APK

This document provides instructions for building the APK using GitHub Actions.

## Steps to Build the APK

1. **Create a GitHub repository**
   - Sign up for a GitHub account if you don't have one: https://github.com/join
   - Create a new repository: https://github.com/new
   - Name it "MinimalistPuzzleAdventure" or any name you prefer
   - Make it public or private as per your preference

2. **Upload your project to GitHub**
   - Install Git from: https://git-scm.com/downloads
   - Open Command Prompt or PowerShell in your project directory
   - Run the following commands (replace YOUR_USERNAME with your GitHub username):

   ```
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/YOUR_USERNAME/MinimalistPuzzleAdventure.git
   git push -u origin main
   ```

3. **Run the GitHub Actions workflow**
   - Go to your repository on GitHub
   - Click on the "Actions" tab
   - You should see the "Build Android APK" workflow
   - Click on "Run workflow" and then "Run workflow" again in the dropdown

4. **Download the APK**
   - After the workflow completes (it may take a few minutes), click on the completed workflow run
   - Scroll down to the "Artifacts" section
   - Click on "app-debug" to download the APK

5. **Install the APK on your Android device**
   - Transfer the downloaded APK to your Android device
   - On your Android device, navigate to the APK file and tap on it
   - Follow the prompts to install the app
   - You may need to enable "Install from unknown sources" in your device settings

## Troubleshooting

If you encounter any issues:
- Make sure all files are properly uploaded to GitHub
- Check the workflow logs for any error messages
- Ensure your Android device allows installation from unknown sources 