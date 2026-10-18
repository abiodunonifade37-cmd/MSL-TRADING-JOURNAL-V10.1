package com.msl.journal;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import org.json.JSONArray;

public class MainActivity extends Activity {
    private WebView webView;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(android.graphics.Color.rgb(10,14,25));
        getWindow().setNavigationBarColor(android.graphics.Color.rgb(10,14,25));
        webView = new WebView(this);
        webView.setBackgroundColor(android.graphics.Color.rgb(10,14,25));
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new AlarmBridge(this), "AndroidAlarmBridge");
        setContentView(webView);
        webView.loadUrl("file:///android_asset/www/index.html");
        requestNotificationPermission();
    }
    @Override public void onBackPressed() { if (webView != null && webView.canGoBack()) webView.goBack(); else super.onBackPressed(); }

    public void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 501);
        if (Build.VERSION.SDK_INT >= 31) {
            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            if (!am.canScheduleExactAlarms()) {
                try { startActivity(new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("package:" + getPackageName()))); }
                catch (Exception ignored) { Toast.makeText(this, "Allow alarms and reminders in Android settings.", Toast.LENGTH_LONG).show(); }
            }
        }
    }
    public class AlarmBridge {
        private final Context context;
        AlarmBridge(Context c) { context = c; }
        @JavascriptInterface public void syncAlarms(String json) {
            getSharedPreferences(AlarmScheduler.PREFS, MODE_PRIVATE).edit().putString(AlarmScheduler.KEY, json).apply();
            AlarmScheduler.scheduleAll(context, json);
        }
        @JavascriptInterface public void requestNotificationPermission() {
            runOnUiThread(() -> MainActivity.this.requestNotificationPermission());
        }
        @JavascriptInterface public boolean isNativeApp() { return true; }
    }
}
