package com.project.altysh.firebaseloginandsaving.ReminderAlarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class MyReceiverDialog extends BroadcastReceiver {

    Intent intent1;

    @Override
    public void onReceive(final Context context, Intent intent) {

        Log.i("recive", "dialoge");
        Toast.makeText(context, "reciver", Toast.LENGTH_LONG).show();
        //check
        if (intent.getStringExtra("newTripDailoge") != null) {
            Log.i("reciveintent", "dialoge");
            String newTrip = intent.getStringExtra("newTripDailoge");
            intent1 = new Intent(context, reminder.class);
            intent1.putExtra("newTripValue", newTrip);
            //IntentWarrper intent2 = (IntentWarrper) intent.getSerializableExtra("Mapsintent");

            //intent1.putExtra("Mapsintent",intent2.getIntent());
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent1);
        } else if (intent.getStringExtra("laterbefore") != null) {
            Log.i("reciveintent", "dialoge");
            String NOTIFICATION_ID = intent.getStringExtra("laterbefore");

            intent1 = new Intent(context, reminder.class);
            intent1.putExtra("laterbefore", NOTIFICATION_ID);

            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.startActivity(intent1);
        }
    }
}
