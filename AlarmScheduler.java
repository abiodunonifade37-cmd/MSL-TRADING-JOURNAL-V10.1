package com.msl.journal;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Calendar;

final class AlarmScheduler {
    static final String PREFS = "msl_native_alarms";
    static final String KEY = "alarms_json";
    static void scheduleAll(Context context, String json) {
        try {
            JSONArray alarms = new JSONArray(json);
            AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            for (int i=0; i<alarms.length(); i++) {
                JSONObject a = alarms.getJSONObject(i);
                String name = a.optString("name", "Market session");
                int id = name.hashCode() & 0x7fffffff;
                Intent intent = new Intent(context, AlarmReceiver.class).putExtra("name", name).putExtra("alarmId", id);
                PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                manager.cancel(pi);
                if (!a.optBoolean("enabled", false)) continue;
                String[] parts = a.optString("time", "08:00").split(":");
                int hour = Integer.parseInt(parts[0]), minute = Integer.parseInt(parts[1]);
                Calendar at = Calendar.getInstance(); at.set(Calendar.HOUR_OF_DAY, hour); at.set(Calendar.MINUTE, minute); at.set(Calendar.SECOND, 0); at.set(Calendar.MILLISECOND, 0);
                if (at.getTimeInMillis() <= System.currentTimeMillis()) at.add(Calendar.DAY_OF_YEAR, 1);
                if (Build.VERSION.SDK_INT >= 23) manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, at.getTimeInMillis(), pi);
                else if (Build.VERSION.SDK_INT >= 19) manager.setExact(AlarmManager.RTC_WAKEUP, at.getTimeInMillis(), pi);
                else manager.set(AlarmManager.RTC_WAKEUP, at.getTimeInMillis(), pi);
            }
        } catch (Exception e) { android.util.Log.e("MSLAlarm", "Could not schedule alarms", e); }
    }
}
