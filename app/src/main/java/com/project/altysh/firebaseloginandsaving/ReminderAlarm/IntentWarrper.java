package com.project.altysh.firebaseloginandsaving.ReminderAlarm;

import android.content.Intent;

/**
 * Created by Altysh on 3/18/2018.
 */

public class IntentWarrper {
    private Intent intent;
    private int id;

    public IntentWarrper(Intent intent, int id) {
        this.intent = intent;
        this.id = id;
    }

    public Intent getIntent() {
        return intent;
    }
}
