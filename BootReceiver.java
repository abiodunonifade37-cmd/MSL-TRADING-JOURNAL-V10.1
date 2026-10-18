package com.msl.journal;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
public class BootReceiver extends BroadcastReceiver {
    @Override public void onReceive(Context context, Intent intent) {
        String json = context.getSharedPreferences(AlarmScheduler.PREFS, Context.MODE_PRIVATE).getString(AlarmScheduler.KEY, "[]");
        AlarmScheduler.scheduleAll(context, json);
    }
}
