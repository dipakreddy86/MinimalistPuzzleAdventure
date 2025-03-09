@echo off
echo Minimalist Puzzle Adventure - GitHub Upload Helper
echo ================================================
echo.

REM Check if Git is installed
where git >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo Git is not installed or not in your PATH.
    echo Please download and install Git from https://git-scm.com/downloads
    echo Then run this script again.
    pause
    exit /b 1
)

REM Ask for GitHub username
set /p GITHUB_USERNAME=Enter your GitHub username: 

REM Ask for repository name
set /p REPO_NAME=Enter repository name (default: MinimalistPuzzleAdventure): 
if "%REPO_NAME%"=="" set REPO_NAME=MinimalistPuzzleAdventure

echo.
echo Initializing Git repository...
git init

echo.
echo Adding files to Git...
git add .

echo.
echo Committing files...
git commit -m "Initial commit"

echo.
echo Setting up main branch...
git branch -M main

echo.
echo Adding remote repository...
git remote add origin https://github.com/%GITHUB_USERNAME%/%REPO_NAME%.git

echo.
echo Pushing to GitHub...
git push -u origin main

echo.
echo Done! Your project has been uploaded to GitHub.
echo.
echo Next steps:
echo 1. Go to https://github.com/%GITHUB_USERNAME%/%REPO_NAME%
echo 2. Click on the "Actions" tab
echo 3. Click on "Build Android APK" workflow
echo 4. Click "Run workflow" and then "Run workflow" again
echo 5. Wait for the build to complete and download the APK
echo.
pause 