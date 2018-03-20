package com.project.altysh.firebaseloginandsaving.ReminderAlarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;

import java.util.Date;

public class FnReminder {

    public static final String ACTION_NOTIFY = "ACTION_NOTIFY";
    public static final String PREFS_NAME = "MyPrefsTime";
    public static final String TimeSnooze = "6000";
    private static FnReminder fnReminder = null;
    AlarmManager alarm;
    Context context;

    private FnReminder(Context context) {
        this.context = context;
        alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    public static FnReminder getInstance(Context context) {
        return new FnReminder(context);
    }

    public void AddReminder(Trip_DTO TripDetails, Intent Mapsintent) {

        //get date of trip
        long diff = TripDetails.getDateTime() - new Date().getTime();
        Intent intent = new Intent(context, MyReceiverDialog.class);
        //specific dialog for each alarm
        intent.putExtra("newTripDailoge", TripDetails.getId() + "");
        //  intent.putExtra("Mapsintent", Mapsintent);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, TripDetails.getId(), intent, PendingIntent.FLAG_ONE_SHOT);
        Toast.makeText(context, "alarm set" + TripDetails.getId(), Toast.LENGTH_LONG).show();
        alarm.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + diff,
                pendingIntent);

    }

    public void RemoveReminder(Trip_DTO TripDetails) {
        Intent intent = new Intent(context, MyReceiverDialog.class);
        alarm.cancel(PendingIntent.getBroadcast(context, TripDetails.getId(), intent, PendingIntent.FLAG_ONE_SHOT));
        Log.i("TAG", "RemoveReminder: " + TripDetails.getId());
        NotificationService.getmNotificationManager(context).cancel(TripDetails.getId());
    }

    public void saveSP(android.view.View v) {

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("userTimeSnooze", TimeSnooze);
        // Commit the edits!
        editor.commit();

    }


}
