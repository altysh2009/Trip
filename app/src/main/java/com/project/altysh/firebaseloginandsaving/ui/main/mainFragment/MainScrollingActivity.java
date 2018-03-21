package com.project.altysh.firebaseloginandsaving.ui.main.mainFragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.ReminderAlarm.FnReminder;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.dto.UserProfile;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.Controls;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;
import com.project.altysh.firebaseloginandsaving.mapUtil.MaPUtil;
import com.project.altysh.firebaseloginandsaving.ui.addTrip.EditorActivity;
import com.project.altysh.firebaseloginandsaving.ui.floatingWidgit.FloatingWidgetService;
import com.project.altysh.firebaseloginandsaving.ui.history.History_Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainScrollingActivity extends AppCompatActivity implements FireBaseConnection.connectToUiMain, Controls {
    private static final int RC_SIGN_IN = 123;
    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 1234;
    final int itemAdd = 12;
    FireBaseConnection fireBaseConnection;
    String token;
    AuthCredential credential;
    List<Trip_DTO> trip_dtos = null;
    UserProfile userProfile = null;
    HomeScreen homeScreen;
    private String userId;
    private SharedPreferences sharedPreferences;
    private String TAG = "MAINACTIVTY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        homeScreen = (HomeScreen) getSupportFragmentManager().findFragmentByTag("mainfrag");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainScrollingActivity.this, EditorActivity.class);
                //(intent,itemAdd);
                startActivity(intent);
            }
        });
        EditText searchView = findViewById(R.id.searchUpcomingTrips);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                List<Trip_DTO> upcomingTrips = new ArrayList<>();
                if (trip_dtos != null) {
                    for (Trip_DTO trip_dto : trip_dtos) {
                        if (trip_dto.getTripName().contains(s.toString()))
                            upcomingTrips.add(trip_dto);

                    }
                    homeScreen.setDate(upcomingTrips);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_history:
                openHistory(null);
                break;
            case R.id.action_settings:
                break;


        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_scrolling, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireBaseConnection = FireBaseConnection.getInstance(this, this);
        sharedPreferences = getSharedPreferences(FireBaseConnection.SHAREPREF, 0);
        //fireBaseConnection.setConnection(this);
        fireBaseConnection = FireBaseConnection.getInstance(this, this);
        sharedPreferences = getSharedPreferences(FireBaseConnection.SHAREPREF, 0);
        userId = sharedPreferences.getString("userid", "");
        if (userId.equals("")) {
            Log.i(TAG, "onStart: no user");
            fireBaseConnection.showLogin(this, RC_SIGN_IN);

        } else {
            fireBaseConnection.rejesterLisner();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                        itemAdd);


            } else {
                askForSystemOverlayPermission();
            }

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
        Log.i(TAG, "onActivityResult: " + requestCode);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (response != null)
                Log.i(TAG, "onActivityResult: " + response.getIdpToken());
            if (resultCode == RESULT_OK) {

                // Successfully signed in


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user.getEmail() != null && user.getEmail().length() == 0)
                    user.updateEmail("user");

                //Log.i(TAG, "onActivityResult: "+user.getProviders());
                this.userId = user.getUid();
                Log.i(TAG, "onActivityResult: " + userId);
                sharedPreferences.edit().putString("userid", userId).commit();
                fireBaseConnection.setSync(true);
                fireBaseConnection.rejesterLisner();
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                            itemAdd);


                }

                Log.i(user.toString(), "onActivityResult: " + sharedPreferences.getString("userid", "null"));

            }
        } else if (requestCode == itemAdd) {
            if (resultCode != RESULT_OK) {
                Log.i(TAG, "onActivityResult: ");
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        } else if (DRAW_OVER_OTHER_APP_PERMISSION == requestCode) {
            if (askForSystemOverlayPermission()) {
                Log.i(TAG, "onActivityResult: " + "not premited");
                Toast.makeText(getApplicationContext(), "we need this to show note when you are navegationg", Toast.LENGTH_LONG).show();
                //askForSystemOverlayPermission();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == itemAdd) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Permission request ");
                alertDialog.setMessage("we need gps to show you where did you go and store it for you");
                alertDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainScrollingActivity.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
                                itemAdd);
                    }
                }).show();


            } else {
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET},
//                            itemAdd);
//                }
                askForSystemOverlayPermission();
            }

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        fireBaseConnection.removeLisner();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //fireBaseConnection.removeLisner();

    }

    @Override
    public void updateUi(List<Trip_DTO> trip) {
        //  Toast.makeText(this, trip.toString(), Toast.LENGTH_SHORT).show();
        trip_dtos = trip;
        if (userProfile != null) {
            fireBaseConnection.setSync(true);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("firsttime", 0);
        boolean frist = sharedPreferences.getBoolean("isFrist", true);

        if (trip != null) {

            FnReminder fnReminder = FnReminder.getInstance(getApplicationContext());
            for (int i = 0; i < trip_dtos.size(); i++) {
                //fnReminder.RemoveReminder(trip_dto);
                Trip_DTO trip_dto = trip_dtos.get(i);

                if (trip_dto.getDateTime() > new Date().getTime()) {
                    if (frist)
                        fnReminder.AddReminder(trip_dto, MaPUtil.getIntentDir(trip_dto));
                } else {
                    HistoryDto historyDto = new HistoryDto();
                    historyDto.setTrip_dto(trip_dto);
                    historyDto.setStatus(historyDto.CANCLED);
                    trip_dtos.remove(trip_dto);
                    fireBaseConnection.addHistoryTrip(historyDto);
                    fireBaseConnection.setList(trip_dtos);
                    i--;

                }


                Log.i(TAG, "updateUi: " + trip_dto.getId());
            }

            sharedPreferences.edit().putBoolean("isFrist", false).apply();
        }
        homeScreen.setDate(trip_dtos);

        // startActivity(MaPUtil.getIntentDir(trip.get(10)));
        // startActivity(MaPUtil.get3DView(trip.get(10).getStartLatitude(),trip.get(10).getStartLongitude()));
    }

    @Override
    public void updateUser(UserProfile userProfile) {
        // if (userProfile != null)
        //   Toast.makeText(this, userProfile.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateHistory(List<HistoryDto> history) {
        //Toast.makeText(this, "histir", Toast.LENGTH_LONG).show();
        // if (history!=null)
        // Toast.makeText(this, history.toString(), Toast.LENGTH_SHORT).show();
    }

    public void deleteLast(View view) {
        fireBaseConnection.deleteTrip(trip_dtos.get(trip_dtos.size() - 1));
        // Toast.makeText(this, trip_dtos.size() + "", Toast.LENGTH_LONG).show();
    }

    @Override
    public void setData(View view) {
        Log.i(TAG, "setData: ");
        UserProfile userProfiles = new UserProfile();
        userProfiles.setEmail("qwiofqwf");
        fireBaseConnection.setUser(userProfiles);
    }

    public void showBuble(View view) {
        askForSystemOverlayPermission();
        startService(new Intent(MainScrollingActivity.this, FloatingWidgetService.class).putExtra("activity_background", true));
    }

    public boolean askForSystemOverlayPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            AlertDialog.Builder dia = new AlertDialog.Builder(this);
            dia.setTitle("Permission to draw floating icon");
            dia.setMessage("we need you to allow us to draw over your apps so we can show you floating icon");
            dia.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, DRAW_OVER_OTHER_APP_PERMISSION);
                }
            }).show();
        }
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this));
    }

    @Override
    public void setTrip(View view) {
        Log.i(TAG, "setTrip: ");


    }

    public void openHistory(View view) {
        Intent intent = new Intent(getApplicationContext(), History_Activity.class);
        startActivity(intent);


    }

    @Override
    public void setHistory(View view) {

    }

    public void startMap(View view) {
        MaPUtil.dir(this);
    }
}
