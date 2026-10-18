# Quick setup

### Deploy the website update
1. Extract this ZIP.
2. Upload the root files and `assets/` to the root of the GitHub Pages repository, replacing the previous V10 files.
3. Commit the changes and wait for GitHub Pages to deploy.
4. Open the site and refresh it. If old styling remains, clear the site's cache or remove/reinstall the PWA.

### Build the Android app with native alarms
1. Install Android Studio and Android SDK Platform 35.
2. In Android Studio, choose **Open** and select the `android-app` folder inside the extracted ZIP.
3. Allow Gradle sync to finish.
4. Choose **Build > Build APK(s)**.
5. Find the debug APK under `android-app/app/build/outputs/apk/debug/`.
6. Install it on your Android phone. Allow notifications and exact alarms when requested.
7. In MSL Journal > Settings, set and enable your session reminders, then tap **Save session times**. The Android bridge schedules them natively.

The `android-app` directory is Android source, not an APK. If Android Studio reports a Gradle sync or SDK error, resolve that before building. Android 12+ requires exact-alarm special access for precise delivery; Android 13+ requires notification permission. Power-management restrictions may still affect delivery on some devices.
