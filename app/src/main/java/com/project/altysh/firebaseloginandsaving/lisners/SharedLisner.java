package com.project.altysh.firebaseloginandsaving.lisners;

import android.content.SharedPreferences;

/**
 * Created by Altysh on 3/7/2018.
 * made to keep reffrance with the interface OnSharedPreferenceChangeListener
 */

public class SharedLisner {
    private SharedPreferences.OnSharedPreferenceChangeListener mylsiner;

    public SharedLisner(SharedPreferences.OnSharedPreferenceChangeListener mylsiner) {
        this.mylsiner = mylsiner;
    }

    public SharedPreferences.OnSharedPreferenceChangeListener getMylsiner() {
        return mylsiner;
    }
}
