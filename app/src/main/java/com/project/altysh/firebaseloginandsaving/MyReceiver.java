package com.project.altysh.firebaseloginandsaving;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("internert", "onReceive: " + intent.getAction());
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo netInfoMob = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo netInfoWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netInfoMob != null && netInfoMob.isConnectedOrConnecting()) {
            Log.v("TAG", "Mobile Internet connected");
            context.getSharedPreferences(FireBaseConnection.SHAREPREF, 0).edit().putBoolean(FireBaseConnection.MOBILECONNECTION, true).apply();

        } else {
            context.getSharedPreferences(FireBaseConnection.SHAREPREF, 0).edit().putBoolean(FireBaseConnection.MOBILECONNECTION, false).apply();
        }
        if (netInfoWifi != null && netInfoWifi.isConnectedOrConnecting()) {
            Log.v("TAG", "Wifi Internet connected");
            context.getSharedPreferences(FireBaseConnection.SHAREPREF, 0).edit().putBoolean(FireBaseConnection.WIFICONNECTION, true).apply();
            FireBaseConnection fireBaseConnection = FireBaseConnection.getInstance();
            if (fireBaseConnection != null)
                fireBaseConnection.setSync(netInfoWifi.isConnectedOrConnecting());

        } else {
            context.getSharedPreferences(FireBaseConnection.SHAREPREF, 0).edit().putBoolean(FireBaseConnection.WIFICONNECTION, false).apply();
        }


    }
}

