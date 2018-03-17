package com.project.altysh.firebaseloginandsaving.firebaseUtails;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.dto.UserProfile;
import com.project.altysh.firebaseloginandsaving.lisners.FireBaseLisner;
import com.project.altysh.firebaseloginandsaving.lisners.ReffranceLisner;
import com.project.altysh.firebaseloginandsaving.lisners.SharedLisner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Altysh on 3/7/2018.
 */

public class FireBaseConnection {
    public static String SHAREPREF = "myshare";
    public static String MOBILECONNECTION = "mobile";
    public static String WIFICONNECTION = "wifi";
    private static FireBaseConnection myobject = null;
    private final String BASE = "planMyTrip";
    private final String USER = "user";
    private final String TRIPS = "trips";
    private final String HISTORY = "history";
    private final String TAG = "FireBaseCCtionOBJECT";
    private SharedPreferences sharedPreferences;
    private List<HistoryDto> historyDtosList;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private connectToUiMain connection;
    private ReffranceLisner user;
    private ReffranceLisner trips;
    private ReffranceLisner history;
    private Context context;
    private SharedLisner sharedLisner;
    private String userId = "";
    private boolean lisner = false;
    private boolean fireLisner = false;
    private List<Trip_DTO> trip_dtoList;
    private List<HistoryDto> historyDtos;
    private UserProfile profile;
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
            new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build());
    private FireBaseLisner fireBaseLisner;

    private FireBaseConnection(Context context) {
        sharedLisner = new SharedLisner(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                Log.i(TAG, "onSharedPreferenceChanged: " + key);
                if (key.equals("userid")) {
                    userId = sharedPreferences.getString(key, null);
                }
            }
        });
        fireBaseLisner = new FireBaseLisner(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "onAuthStateChanged: ");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userId = user.getUid();
                    sharedPreferences.edit().putString("userid", userId);
                    if (!lisner) {
                        rejesterLisner();
                        lisner = true;
                    }

                }

            }
        });
        sharedPreferences = context.getSharedPreferences(SHAREPREF, 0);
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedLisner.getMylsiner());

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseInstance.setPersistenceEnabled(true);
        mFirebaseDatabase = mFirebaseInstance.getReference().child(BASE);

        userId = sharedPreferences.getString("userid", "");
        this.context = context;


    }

    public static FireBaseConnection getInstance(Context context) {
        if (myobject != null)
            return myobject;
        else return new FireBaseConnection(context);
    }

    public static FireBaseConnection getInstance(Context context, connectToUiMain connectToUiMain) {

        if (myobject != null) {
            myobject.setContext(context);
            myobject.setConnection(connectToUiMain);
            return myobject;
        } else {
            myobject = new FireBaseConnection(context);
            //smyobject.setContext(context);
            myobject.setConnection(connectToUiMain);
        }
        return myobject;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setConnection(connectToUiMain connection) {
        this.connection = connection;
    }


    public void rejesterLisner() {
        if (!lisner) {
            Log.i(TAG, "rejesterLisner: ");
            if (!fireLisner)
                FirebaseAuth.getInstance().addAuthStateListener(fireBaseLisner.getLisner());
            fireLisner = true;
            if (!userId.equals("")) {
                Toast.makeText(context, "rejster lisner", Toast.LENGTH_LONG).show();
                addUserChangeListenerToUser();
                addUserChangeListenerToTrips();
                addUserChangeListenerToHistory();
                lisner = true;
            } else lisner = false;
        } else {

        }

    }


    public boolean checkConnection() {
        return !userId.equals("");

    }

    public void signInWithAnthor(FragmentActivity con, int RC_SIGN_IN) {
        con.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public void showLogin(FragmentActivity con, int RC_SIGN_IN) {
        rejesterLisner();
        con.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    private void addUserChangeListenerToUser() {
        // User data change listener
        user = new ReffranceLisner(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // GenericTypeIndicator<List<MainActivity.User>> t = new GenericTypeIndicator<List<MainActivity.User>>() {};
                UserProfile user = dataSnapshot.getValue(UserProfile.class);
                profile = user;
                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    connection.updateUser(null);
                    //return;
                } else {
                    profile = user;
                    connection.updateUser(user);
                }

                //Log.e(TAG, "User data is changed!" + user.toString());


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
        mFirebaseDatabase.child(USER).child(userId).addValueEventListener(user.getListener());
    }

    private void addUserChangeListenerToTrips() {
        Log.i(TAG, "addUserChangeListenerToHistory: " + "history");
        System.out.println("addUserChangeListenerToHistory");
        // User data change listener
        trips = new ReffranceLisner(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Trip_DTO>> t = new GenericTypeIndicator<List<Trip_DTO>>() {
                };
                List<Trip_DTO> user = dataSnapshot.getValue(t);
                trip_dtoList = user;
                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    connection.updateUi(new ArrayList<Trip_DTO>());
                } else {
                    connection.updateUi(user);
                }

                // Log.e(TAG, "User data is changed!" + user.toString());


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
        mFirebaseDatabase.child(TRIPS).child(userId).addValueEventListener(trips.getListener());
    }

    private void addUserChangeListenerToHistory() {
        // User data change listener
        Log.i(TAG, "addUserChangeListenerToHistory: " + "history");
        System.out.println("addUserChangeListenerToHistory");
        history = new ReffranceLisner(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<HistoryDto>> t = new GenericTypeIndicator<List<HistoryDto>>() {
                };
                List<HistoryDto> history = dataSnapshot.getValue(t);
                historyDtosList = history;
                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    connection.updateHistory(history);
                } else {
                    connection.updateHistory(history);
                }

                // Log.e(TAG, "User data is changed!" + user.toString());


            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
        mFirebaseDatabase.child(HISTORY).child(userId).addValueEventListener(history.getListener());
    }

    public void startTripLisner() {
        if (connection != null)
            addUserChangeListenerToTrips();
    }

    public void startUserLisner() {
        if (connection != null)
            addUserChangeListenerToUser();
    }

    /**
     * called to remove all lisner made to the actitvty when it is paused
     */
    public void removeLisner() {
        if (lisner) {
            if (trips != null)
                mFirebaseDatabase.child(TRIPS).child(userId).removeEventListener(trips.getListener());
            if (user != null)
                mFirebaseDatabase.child(USER).child(userId).removeEventListener(user.getListener());
            if (sharedLisner != null) {
                sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedLisner.getMylsiner());
            }
            if (history != null) {
                mFirebaseDatabase.child(HISTORY).child(userId).removeEventListener(history.getListener());
            }
            if (fireBaseLisner != null) {
                FirebaseAuth.getInstance().removeAuthStateListener(fireBaseLisner.getLisner());
            }
            lisner = false;
            fireLisner = false;
        } else if (fireLisner) {
            FirebaseAuth.getInstance().removeAuthStateListener(fireBaseLisner.getLisner());
            fireLisner = false;
        }
        Toast.makeText(context, "lisnerRemoved", Toast.LENGTH_LONG).show();
    }

    /**
     * @param trip the new list
     *             update the trip list on firebase
     */
    public void setList(List<Trip_DTO> trip) {
        Log.i(TAG, "setList: " + userId);
        mFirebaseDatabase.child(TRIPS).child(userId).setValue(trip);
    }

    /**
     * @param userProfile the modified user profile
     *                    update the user profile on firebase
     */
    public void setUser(UserProfile userProfile) {
        Log.i(TAG, "setList: " + userId);
        mFirebaseDatabase.child(USER).child(userId).setValue(userProfile);
    }

    /**
     * @param context the activtiy
     * @return true if you have google service else false
     * doesn't detect the version problem
     */
    public boolean checkGoogleService(Activity context) {
        boolean good = false;
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(context);
        // Showing status
        if (status == ConnectionResult.SUCCESS) {
            Toast.makeText(context, "Google Play Services are available", Toast.LENGTH_SHORT).show();
            good = true;
        } else {
            //tvStatus.setText("Google Play Services are not available");
            int requestCode = 10;

            Dialog dialog = googleApiAvailability.getErrorDialog(context, status, requestCode);
            if (dialog != null)
                dialog.show();
        }
        return good;
    }

    /**
     * @param on wifi state
     *           if true firebase goes online
     *           else it goes offline
     */
    public void setSync(boolean on) {
        Log.i(TAG, "setSync: " + on);
        if (on)
            mFirebaseInstance.goOnline();
        else
            mFirebaseInstance.goOffline();
    }

    /**
     * @param trip_dto the trip to be deleted
     *                 remove it from firebase
     */
    public void deleteTrip(Trip_DTO trip_dto) {

        trip_dtoList.remove(trip_dto);
        setList(trip_dtoList);
    }

    /**
     * @param trip_dto
     */
    public void addTrip(Trip_DTO trip_dto) {
        if (trip_dtoList != null)
            trip_dtoList.add(trip_dto);
        else {
            trip_dtoList = new ArrayList<>();
            trip_dtoList.add(trip_dto);
        }
        setList(trip_dtoList);
    }

    public void addHistoryTrip(HistoryDto historyDto) {
        if (historyDtosList != null)
            historyDtosList.add(historyDto);
        else {
            historyDtosList = new ArrayList<>();
            historyDtosList.add(historyDto);
        }
        setHistory(historyDtosList);
    }

    public void setHistory(List<HistoryDto> historyDtosList) {
        mFirebaseDatabase.child(HISTORY).child(userId).setValue(historyDtosList);
    }
    public void updateProfile(UserProfile profile) {
        this.profile = profile;
        setUser(profile);

    }

    public void logOut() {
        removeLisner();
        userId = "";
        sharedPreferences.edit().putString("userid", "");
        AuthUI.getInstance().signOut(context);


    }

    public interface connectToUiMain {
        void updateUi(List<Trip_DTO> trip);

        void updateUser(UserProfile userProfile);

        void updateHistory(List<HistoryDto> history);


    }

    public interface connectToHistory {
        void updateUi();

        void updateUser(UserProfile userProfile);

    }


}
