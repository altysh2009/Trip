package com.project.altysh.firebaseloginandsaving.lisners;

import com.google.firebase.database.ValueEventListener;

/**
 * Created by Altysh on 3/7/2018.
 */

public class ReffranceLisner {
    private ValueEventListener listener;

    public ReffranceLisner(ValueEventListener valueEventListener) {
        listener = valueEventListener;
    }

    public ValueEventListener getListener() {
        return listener;
    }
}
