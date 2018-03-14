package com.project.altysh.firebaseloginandsaving.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.dto.UserProfile;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.Controls;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;
import com.project.altysh.firebaseloginandsaving.ui.addTrip.EditorActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FireBaseConnection.connectToUiMain, Controls {
    private static final int RC_SIGN_IN = 123;
    final int itemAdd = 112233;
    FireBaseConnection fireBaseConnection;
    String token;
    AuthCredential credential;
    List<Trip_DTO> trip_dtos = null;
    UserProfile userProfile = null;
    private String userId;
    private SharedPreferences sharedPreferences;
    private String TAG = "MAINACTIVTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                //(intent,itemAdd);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBaseConnection = FireBaseConnection.getInstance(this, this);
        sharedPreferences = getSharedPreferences(FireBaseConnection.SHAREPREF, 0);
        //fireBaseConnection.setConnection(this);

        userId = sharedPreferences.getString("userid", "");
        if (userId.equals("")) {
            Log.i(TAG, "onStart: no user");
            fireBaseConnection.showLogin(this, RC_SIGN_IN);

        } else {
            fireBaseConnection.rejesterLisner();
        }


    }

    public void LogOut(View v) {
        fireBaseConnection.logOut();
    }

    public void LogIn(View v) {
        fireBaseConnection.showLogin(this, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            Log.i(TAG, "onActivityResult: " + response.getIdpToken());
            if (resultCode == RESULT_OK) {

                // Successfully signed in


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                //Log.i(TAG, "onActivityResult: "+user.getProviders());
                this.userId = user.getUid();
                sharedPreferences.edit().putString("userid", userId).apply();
                fireBaseConnection.setSync(true);

                Log.i(user.toString(), "onActivityResult: " + sharedPreferences.getString("userid", "null"));

            } else {

            }
        } else if (requestCode == itemAdd) {
            Trip_DTO trip_dto = (Trip_DTO) getIntent().getExtras().get("item");
            fireBaseConnection.addTrip(trip_dto);
        }

    }

    @Override
    public void updateUi(List<Trip_DTO> trip) {
        Toast.makeText(this, trip.toString(), Toast.LENGTH_SHORT).show();
        trip_dtos = trip;
        if (userProfile != null) {
            fireBaseConnection.setSync(true);
        }
        // startActivity(MaPUtil.getIntentDir(trip.get(10)));
        // startActivity(MaPUtil.get3DView(trip.get(10).getStartLatitude(),trip.get(10).getStartLongitude()));
    }

    @Override
    public void updateUser(UserProfile userProfile) {
        if (userProfile != null)
            Toast.makeText(this, userProfile.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateHistory(List<HistoryDto> history) {

    }

    public void deleteLast(View view) {
        fireBaseConnection.deleteTrip(trip_dtos.get(trip_dtos.size() - 1));
        Toast.makeText(this, trip_dtos.size() + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setData(View view) {
        Log.i(TAG, "setData: ");
        UserProfile userProfiles = new UserProfile();
        userProfiles.setEmail("qwiofqwf");
        fireBaseConnection.setUser(userProfiles);
    }

    @Override
    public void setTrip(View view) {
        Log.i(TAG, "setTrip: ");


    }

    @Override
    public void setHistory(View view) {

    }
}
