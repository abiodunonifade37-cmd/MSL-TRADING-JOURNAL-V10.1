# MSL Trading Journal V10.1 Premium

A local-first trading journal update with a refreshed premium mobile interface and an Android-native alarm project.

## What's updated
- Premium mobile visual refresh inspired by Samsung One UI-style clarity and iOS-style depth, blur, rounded surfaces, and restrained gradients.
- Alarm settings now synchronize to a native Android scheduler when running inside the included Android app.
- Native Android session alarms use `AlarmManager`, high-priority notifications, vibration, daily rescheduling, and restoration after device reboot or app update.
- Improved messaging makes the distinction clear: a website/PWA cannot guarantee exact alarms after its browser process is suspended or closed.
- Existing journal features and local data format are preserved. This version still stores trades locally on the device; use backup/restore to protect your records.

## Website / PWA update
Upload the root contents of this folder to the root of your GitHub Pages repository, preserving `assets/`, `index.html`, `styles.css`, `app.js`, `manifest.webmanifest`, and `sw.js`. After deploying, fully close and reopen the PWA or clear its site cache if the old CSS remains.

**PWA notification limitation:** The website can display notifications with permission, but a static GitHub Pages PWA cannot reliably fire exact scheduled alarms after the browser is closed. This is an operating-system/browser limitation, not something a `setInterval` can fix. For alarms while the app/browser is closed, use the native Android project below.

## Android app source
Open the `android-app/` folder in Android Studio (current stable version with Android SDK 35 installed), allow Gradle sync, then use **Build > Build APK(s)**. The APK output is normally under `android-app/app/build/outputs/apk/debug/`.

On first launch, allow notifications. On Android 12+, allow **Alarms & reminders / exact alarms** when Android prompts or in App info > Special app access. On Android 13+, allow notification permission. Do not force-stop the app if you expect Android alarms to run; Android may suppress alarms for force-stopped apps until the app is opened again. Battery optimization or device-manufacturer background restrictions can also affect delivery.

The Android project packages the web journal into a WebView and uses native `AlarmManager` notifications. Alarm schedules are stored in app-private preferences and restored after reboot/update. Times use the device's local timezone. Recheck session times after changing timezone or daylight-saving conditions.

## Important
- This ZIP contains Android source code, not a prebuilt APK.
- The journal is local-first and does not sync automatically between devices or to Supabase.
- Download a full backup regularly. Clearing browser/app data or uninstalling can erase locally stored journal records.
- Notifications require user-granted Android permission; exact-alarm access is subject to Android version and device policy.
