package com.project.altysh.firebaseloginandsaving.lisners;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Altysh on 3/7/2018.
 * made to keep reffrance with the interface AuthStateListener
 */

public class FireBaseLisner {
    private FirebaseAuth.AuthStateListener lisner;

    public FireBaseLisner(FirebaseAuth.AuthStateListener lisner) {
        this.lisner = lisner;
    }

    public FirebaseAuth.AuthStateListener getLisner() {
        return lisner;
    }
}
