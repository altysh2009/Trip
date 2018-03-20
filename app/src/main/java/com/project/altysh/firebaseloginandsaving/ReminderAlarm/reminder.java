package com.project.altysh.firebaseloginandsaving.ReminderAlarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;
import com.project.altysh.firebaseloginandsaving.mapUtil.MaPUtil;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.FloatingWidgetService;

import java.util.Queue;

public class reminder extends Activity {

    Intent soundService;
    AlertDialog.Builder alertDialog;
    Queue<AlertDialog.Builder> queue;
    int i = 0;
    int id;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentWarrper warrper = (IntentWarrper) getIntent().getSerializableExtra("Mapsintent");
        alertDialog = new AlertDialog.Builder(reminder.this);
        //dismiss dialog
        //later
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_LONG).show();
                mServiceIntent = new Intent(getApplicationContext(), NotificationService.class);
                //check new trip later or later before
                if (getIntent().getStringExtra("newTripValue") != null) {
                    //new dialog set new i
                    i = Integer.parseInt(getIntent().getStringExtra("newTripValue"));
//                    mServiceIntent.putExtra("Mapsintent", FireBaseConnection.getInstance(getApplicationContext()).getTripById(i));
                    mServiceIntent.putExtra("newTripLater", i + "");
                } else if (getIntent().getStringExtra("laterbefore") != null) {
                    //later
                    //get NOTIFICATION_ID
                    id = Integer.parseInt(getIntent().getStringExtra("laterbefore"));
                    mServiceIntent.putExtra("Mapsintent", getIntent().getSerializableExtra("Mapsintent"));
                    mServiceIntent.putExtra("laterbefore", id + "");
                }
                mServiceIntent.setAction(FnReminder.ACTION_NOTIFY);
                Toast.makeText(getApplicationContext(), R.string.timer_start, Toast.LENGTH_SHORT).show();
                startService(mServiceIntent);
                stopService(soundService);
                finish();
            }
        });

        // Setting Dialog Title
        if (getIntent().getStringExtra("newTripValue") != null) {
            //new dialog set new i  .
            i = Integer.parseInt(getIntent().getStringExtra("newTripValue"));
            alertDialog.setTitle("planned trip no " + i);
        } else if (getIntent().getStringExtra("laterbefore") != null) {
            //later
            //get NOTIFICATION_ID
            id = Integer.parseInt(getIntent().getStringExtra("laterbefore"));
            alertDialog.setTitle("snoozed planned trip no " + id);
        }
        // Setting Dialog Message
        alertDialog.setMessage("it is time now for your Trip");
        // Setting Positive "Start" Button
        alertDialog.setPositiveButton("Start", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed START button.
                Trip_DTO trip_dto = FireBaseConnection.getInstance(getApplicationContext()).getTripById(i);
                if (trip_dto != null) {
                    Intent widgit = new Intent(getApplicationContext(), FloatingWidgetService.class);
                    widgit.putExtra("activity_background", true);
                    widgit.putExtra("trip", i);
                    startService(widgit);
                    getApplicationContext().startActivity(MaPUtil.getIntentDir(trip_dto));

                }
                //change trip object to passed & add it to history ............
                //............
                Toast.makeText(getApplicationContext(), "You clicked on start",
                        Toast.LENGTH_SHORT).show();
                if (getIntent().getStringExtra("laterbefore") != null) {
                    //later
                    //get NOTIFICATION_ID
                    id = Integer.parseInt(getIntent().getStringExtra("laterbefore"));
                    NotificationService.getmNotificationManager(getApplicationContext()).cancel(id);
                }
                stopService(soundService);
                finish();
            }
        });

        // Setting Neutral "later" Button
        alertDialog.setNeutralButton("Later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed later button.
                //check new trip later or new  trip
                Toast.makeText(getApplicationContext(), "You clicked on later",
                        Toast.LENGTH_SHORT).show();
                mServiceIntent = new Intent(getApplicationContext(), NotificationService.class);
                mServiceIntent.putExtra("Mapsintent", getIntent().getSerializableExtra("Mapsintent"));
                if (getIntent().getStringExtra("newTripValue") != null) {
                    //new dialog set new i
                    i = Integer.parseInt(getIntent().getStringExtra("newTripValue"));
                    mServiceIntent.putExtra("newTripLater", i + "");
                } else if (getIntent().getStringExtra("laterbefore") != null) {
                    //later
                    //get NOTIFICATION_ID
                    id = Integer.parseInt(getIntent().getStringExtra("laterbefore"));
                    mServiceIntent.putExtra("laterbefore", id + "");
                }

                mServiceIntent.setAction(FnReminder.ACTION_NOTIFY);
                startService(mServiceIntent);
                stopService(soundService);
                finish();

            }
        });
        // Setting Negative "Cancel" Button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // User pressed cancel button.
                //remove trip object from arraylist & set trip to cancelled trip
                if (getIntent().getStringExtra("laterbefore") != null) {
                    //later
                    //get NOTIFICATION_ID
                    id = Integer.parseInt(getIntent().getStringExtra("laterbefore"));
                    NotificationService.getmNotificationManager(getApplicationContext()).cancel(id);

                }
                FireBaseConnection fireBaseConnection = FireBaseConnection.getInstance(getApplicationContext());
                HistoryDto historyDto = new HistoryDto();
                historyDto.setTrip_dto(fireBaseConnection.getTripById(i));
                Toast.makeText(getApplicationContext(), fireBaseConnection.getTripById(i) + " data", Toast.LENGTH_LONG).show();


                historyDto.setStatus(historyDto.CANCLED);
                fireBaseConnection.addHistoryTrip(historyDto);
                fireBaseConnection.deleteTrip(fireBaseConnection.getTripById(i));
                stopService(soundService);
                Toast.makeText(getApplicationContext(), "You clicked on cancel", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        alertDialog.show();

        soundService = new Intent(this, SoundService.class);
        startService(soundService);


    }

}
