package com.project.altysh.firebaseloginandsaving.ReminderAlarm;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;
import com.project.altysh.firebaseloginandsaving.mapUtil.MaPUtil;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.FloatingWidgetService;


public class NotificationService extends IntentService {

    public static final String ACTION_DISMISS = "ACTION_DISMISS";
    public static final String ACTION_NOTIFY = "ACTION_NOTIFY";
    public static final String ACTION_DIALOG = "dialog";
    public static final String ACTION_START = "ACTION_START";
    public static int NOTIFICATION_ID;
    private static NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    AlarmManager alarm;
    PendingIntent pintent;
    Intent mServiceIntent;
    int Millis;

    public NotificationService() {
        super("NotificationService");
    }

    public static NotificationManager getmNotificationManager(Context context) {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //check new later dialoge or dialoge before
        if (intent.getStringExtra("newTripLater") != null) {
            NOTIFICATION_ID = Integer.parseInt(intent.getStringExtra("newTripLater"));
        } else if (intent.getStringExtra("laterbefore") != null) {
            NOTIFICATION_ID = Integer.parseInt(intent.getStringExtra("laterbefore"));
        }
        Log.i("NOTIFICATION_ID", NOTIFICATION_ID + "");
        mServiceIntent = new Intent(getApplicationContext(), MyReceiverDialog.class);
        mServiceIntent.putExtra("laterbefore", NOTIFICATION_ID + "");
        mServiceIntent.setAction(ACTION_DIALOG);
        mServiceIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        pintent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, mServiceIntent, PendingIntent.FLAG_ONE_SHOT);

        SharedPreferences settings = getSharedPreferences(FnReminder.PREFS_NAME, 0);
        String userM = settings.getString("userTimeSnooze", FnReminder.TimeSnooze);//10 min
        Millis = Integer.parseInt(userM);
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        String action = intent.getAction();
        if (action.equals(ACTION_NOTIFY)) {
            //later notification
            if (intent.getStringExtra("newTripLater") != null) {
                createNotification(intent, NOTIFICATION_ID + "");
                createNotification(builder);
            }

            alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                    + Millis, pintent);

        } else if (action.equals(ACTION_DISMISS)) {
            mNotificationManager.cancel(intent.getIntExtra("idC", 0));
            alarm.cancel(pintent);
            FireBaseConnection fireBaseConnection = FireBaseConnection.getInstance(getApplicationContext());
            HistoryDto historyDto = new HistoryDto();
            historyDto.setTrip_dto(fireBaseConnection.getTripById(NOTIFICATION_ID));
            historyDto.setStatus(historyDto.CANCLED);
            fireBaseConnection.addHistoryTrip(historyDto);
            fireBaseConnection.deleteTrip(fireBaseConnection.getTripById(NOTIFICATION_ID));

        } else if (action.equals(ACTION_START)) {
            //start Trip
            Trip_DTO trip_dto = FireBaseConnection.getInstance(getApplicationContext()).getTripById(NOTIFICATION_ID);
            if (trip_dto != null) {
                getApplicationContext().startActivity(MaPUtil.getIntentDir(trip_dto));
                Intent widgit = new Intent(getApplicationContext(), FloatingWidgetService.class);
                widgit.putExtra("activity_background", true);
                widgit.putExtra("trip", NOTIFICATION_ID);
                startService(widgit);
            }
            //getApplicationContext().startActivity((Intent) intent.getSerializableExtra("Mapsintent"));
            mNotificationManager.cancel(intent.getIntExtra("idS", 0));
            alarm.cancel(pintent);
        } else if (action.equals(ACTION_DIALOG)) {

            broadcastIntent();

        }

    }


    private void createNotification(Intent intent, String msg) {
        // Sets up the Start and Dismiss action buttons
        Intent dismissIntent = new Intent(this, NotificationService.class);
        dismissIntent.setAction(ACTION_DISMISS);
        dismissIntent.putExtra("idC", NOTIFICATION_ID);
        PendingIntent piDismiss = PendingIntent.getService(this, NOTIFICATION_ID,
                dismissIntent, 0);

        Intent startIntent = new Intent(this, NotificationService.class);
        startIntent.setAction(ACTION_START);
        startIntent.putExtra("idS", NOTIFICATION_ID);
        startIntent.putExtra("Mapsintent", intent.getSerializableExtra("Mapsintent"));
        PendingIntent piStart = PendingIntent.getService(this, NOTIFICATION_ID,
                startIntent, 0);


        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setContentTitle("planned Trip no " + msg)
                .setContentText("it is time now for your Trip")
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .addAction(R.drawable.ic_do_not_disturb_black_24dp,
                        getString(R.string.dismiss), piDismiss)
                .addAction(R.drawable.ic_event_available_black_24dp,
                        "start", piStart);
        builder.setOngoing(true);


    }

    private void createNotification(NotificationCompat.Builder builder) {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }


    public void broadcastIntent() {
        Intent intent = new Intent();
        intent.setAction("NotificationService");
        sendBroadcast(intent);
    }
}
