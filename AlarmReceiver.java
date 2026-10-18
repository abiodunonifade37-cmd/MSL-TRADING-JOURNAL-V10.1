package com.msl.journal;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;

public class AlarmReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name"); if (name == null) name = "Market session";
        NotificationManager nm = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26) nm.createNotificationChannel(new NotificationChannel("msl_sessions", "Trading session alarms", NotificationManager.IMPORTANCE_HIGH));
        Intent open = new Intent(context, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent content = PendingIntent.getActivity(context, 700, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        android.app.Notification.Builder builder = Build.VERSION.SDK_INT >= 26
            ? new android.app.Notification.Builder(context, "msl_sessions")
            : new android.app.Notification.Builder(context).setPriority(android.app.Notification.PRIORITY_HIGH);
        android.app.Notification notification = builder
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm).setContentTitle(name + " reminder")
            .setContentText("Your MSL Trading Journal session reminder is due. Review your plan and risk.")
            .setStyle(new android.app.Notification.BigTextStyle().bigText("Your " + name + " reminder is due. Review liquidity, confirmation, invalidation and risk before execution."))
            .setAutoCancel(true).setContentIntent(content).setVibrate(new long[]{0,350,180,350}).build();
        nm.notify(name.hashCode(), notification);
        // Schedule the next daily occurrence. Reboot restoration is also handled by BootReceiver.
        try {
            String json = context.getSharedPreferences(AlarmScheduler.PREFS, Context.MODE_PRIVATE).getString(AlarmScheduler.KEY, "[]");
            JSONArray arr = new JSONArray(json);
            for (int i=0;i<arr.length();i++) { JSONObject a=arr.getJSONObject(i); if(a.optString("name").equals(name) && a.optBoolean("enabled",false)) { AlarmScheduler.scheduleAll(context,json); break; } }
        } catch (Exception ignored) { }
    }
}
