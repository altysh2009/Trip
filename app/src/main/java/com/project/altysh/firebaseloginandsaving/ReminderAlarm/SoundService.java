package com.project.altysh.firebaseloginandsaving.ReminderAlarm;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import com.project.altysh.firebaseloginandsaving.R;

public class SoundService extends IntentService {

    MediaPlayer mp;

    public SoundService() {
        super("SoundService");
    }

    public MediaPlayer getMp() {
        return mp;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        mp = MediaPlayer.create(this, R.raw.tone);
        mp.setLooping(false);
    }

    public void onDestroy() {
        mp.stop();
    }

    public void onStart(Intent intent, int startid) {

        Log.d("tag", "On start");
        mp.start();
    }

}
