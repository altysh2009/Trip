package com.project.altysh.firebaseloginandsaving.ui.floatingWidgit;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.project.altysh.firebaseloginandsaving.R;
import com.project.altysh.firebaseloginandsaving.dto.HistoryDto;
import com.project.altysh.firebaseloginandsaving.dto.Trip_DTO;
import com.project.altysh.firebaseloginandsaving.firebaseUtails.FireBaseConnection;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Created by anupamchugh on 01/08/17.
 */

public class FloatingWidgetService extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private static final int LOCATION_INTERVAL = 100;
    private static final float LOCATION_DISTANCE = 20f;
    int mWidth;
    CounterFab counterFab;
    boolean activity_background;
    LocationListener locationListener;
    private LocationManager mLocationManager = null;
    private WindowManager mWindowManager;
    private View mOverlayView;
    private long startTime;
    private long endTime;
    private double distance;
    private double avgSpeed;
    private List<PointDara> points;
    private int tripId;
    private List<Boolean> checked;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {


        if (intent != null) {
            activity_background = intent.getBooleanExtra("activity_background", false);
            Toast.makeText(getApplicationContext(), "not null", Toast.LENGTH_LONG).show();
            tripId = intent.getIntExtra("trip", 0);

        }
        final FireBaseConnection fireBaseConnection = FireBaseConnection.getInstance(getApplicationContext());
        final Trip_DTO trip_dto = fireBaseConnection.getTripById(tripId);

        if (mOverlayView == null) {

            mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);

            startTime = new Date().getTime();
            final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    PixelFormat.TRANSLUCENT);


            //Specify the view position
            params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
            params.x = 0;
            params.y = 100;


            mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            mWindowManager.addView(mOverlayView, params);

            Display display = mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);

            counterFab = mOverlayView.findViewById(R.id.fabHead);
            // counterFab.setCount(s.size());
            final LinearLayout linearLayout = mOverlayView.findViewById(R.id.mylayout);
            final ListView listView = mOverlayView.findViewById(R.id.listview);
            points = new ArrayList<>();
            Button button = mOverlayView.findViewById(R.id.end);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long t = points.get(points.size() - 1).getTime() - points.get(0).getTime();
                    endTime = new Date().getTime();
                    double dis = distance;
                    Log.i(TAG, "onClick: " + dis + " " + t);
                    avgSpeed = dis / ((t / 3600.0) / 1000.0);
                    HistoryDto historyDto = new HistoryDto();
                    historyDto.setTrip_dto(fireBaseConnection.getTripById(tripId));
                    historyDto.setAvgSpeed(avgSpeed);
                    historyDto.setDistance(distance);
                    historyDto.setDurtation(endTime - startTime);
                    historyDto.setEndTime(endTime);
                    historyDto.setStartTime(startTime);
                    historyDto.setPoints(points);
                    historyDto.setChecked(checked);
                    historyDto.setStatus(historyDto.DONE);
                    // Log.i
                    // (TAG, "onClick: "+avgSpeed+" "+distance+" "+startTime+" "+endTime+" "+points+" "+checked);
                    fireBaseConnection.addHistoryTrip(historyDto);
                    fireBaseConnection.deleteTrip(trip_dto);

//                    Intent intent = new Intent(FloatingWidgetService.this, details.class);
//                                    intent.putExtra("array", (Serializable) points);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
                    stopSelf();
                }
            });
            final ArrayList<NoteObj> s = new ArrayList<>();
            checked = new ArrayList<>();
            for (int i = 0; i < trip_dto.getTripNotes().size(); i++) {
                s.add(new NoteObj(false, trip_dto.getTripNotes().get(i)));
                checked.add(false);
            }


            MySimpleArrayAdapter mySimpleArrayAdapter = new MySimpleArrayAdapter(getApplicationContext(), s);
            listView.setAdapter(mySimpleArrayAdapter);
            counterFab.setCount(s.size());
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NoteObj noteObj = s.get(position);
                    LinearLayout linearLayout2 = view.findViewById(R.id.itemselct);
                    if (!noteObj.selected) {
                        linearLayout2.setBackground(getApplicationContext().getDrawable(R.drawable.roundednotsel));
                        noteObj.selected = true;
                        checked.remove(position);
                        checked.add(position, Boolean.TRUE);
                        counterFab.setCount(counterFab.getCount() - 1);
                    } else {
                        linearLayout2.setBackground(getApplicationContext().getDrawable(R.drawable.rounded));
                        noteObj.selected = false;
                        checked.remove(position);
                        checked.add(position, Boolean.FALSE);
                        counterFab.setCount(counterFab.getCount() + 1);
                    }
                }
            });
            final LinearLayout layout = mOverlayView.findViewById(R.id.layout);
            ViewTreeObserver vto = layout.getViewTreeObserver();
            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = layout.getMeasuredWidth();

                    //To get the accurate middle of the screen we subtract the width of the floating widget.
                    mWidth = size.x - width;

                }
            });

            counterFab.setOnTouchListener(new View.OnTouchListener() {
                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Log.i(TAG, "onTouch: ");
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            //remember the initial position.
                            initialX = params.x;
                            initialY = params.y;


                            //get the touch location
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();


                            return true;
                        case MotionEvent.ACTION_UP:

                            //Only start the activity if the application is in background. Pass the current badge_count to the activity
                            if (activity_background) {


                                float xDiff = event.getRawX() - initialTouchX;
                                float yDiff = event.getRawY() - initialTouchY;

                                if ((Math.abs(xDiff) < 5) && (Math.abs(yDiff) < 5)) {
//                                    Intent intent = new Intent(FloatingWidgetService.this, MainActivity.class);
//                                    intent.putExtra("badge_count", counterFab.getCount());
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//
//                                    //close the service and remove the fab view
//                                    stopSelf();
                                    if (linearLayout.getVisibility() == View.GONE)
                                        linearLayout.setVisibility(View.VISIBLE);
                                    else linearLayout.setVisibility(View.GONE);
                                }

                            }
                            //Logic to auto-position the widget based on where it is positioned currently w.r.t middle of the screen.
                            int middle = mWidth / 2;
                            float nearestXWall = params.x >= middle ? mWidth : 0;
                            params.x = (int) nearestXWall;


                            mWindowManager.updateViewLayout(mOverlayView, params);


                            return true;
                        case MotionEvent.ACTION_MOVE:


                            int xDiff = Math.round(event.getRawX() - initialTouchX);
                            int yDiff = Math.round(event.getRawY() - initialTouchY);


                            //Calculate the X and Y coordinates of the view.
                            params.x = initialX + xDiff;
                            params.y = initialY + yDiff;

                            //Update the layout with new X & Y coordinates
                            mWindowManager.updateViewLayout(mOverlayView, params);


                            return true;
                    }
                    return false;
                }
            });
        } else {

            counterFab.increase();

        }


        return super.onStartCommand(intent, START_FLAG_REDELIVERY, startId);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        setTheme(R.style.AppTheme);
        initializeLocationManager();
        locationListener = new LocationListener(LocationManager.GPS_PROVIDER);
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    locationListener);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }


    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null)
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
        if (mLocationManager != null) {

            mLocationManager.removeUpdates(locationListener);

        }
        Log.i(TAG, "onDestroy: ");
    }

    double distance_on_geoid(double lat1, double lon1, double lat2, double lon2) {
        float M_PI = 3.14f;
        // Convert degrees to radians
        lat1 = lat1 * M_PI / 180.0;
        lon1 = lon1 * M_PI / 180.0;

        lat2 = lat2 * M_PI / 180.0;
        lon2 = lon2 * M_PI / 180.0;

        // radius of earth in metres
        double r = 6378100;

        // P
        double rho1 = r * cos(lat1);
        double z1 = r * sin(lat1);
        double x1 = rho1 * cos(lon1);
        double y1 = rho1 * sin(lon1);

        // Q
        double rho2 = r * cos(lat2);
        double z2 = r * sin(lat2);
        double x2 = rho2 * cos(lon2);
        double y2 = rho2 * sin(lon2);

        // Dot product
        double dot = (x1 * x2 + y1 * y2 + z1 * z2);
        double cos_theta = dot / (r * r);

        double theta = acos(cos_theta);

        // Distance in Metres
        return r * theta;
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);

        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            points.add(new PointDara(location.getLongitude(), location.getLatitude(), new Date().getTime()));
            if (points.size() > 1)
                distance = distance_on_geoid(location.getLatitude(), location.getLongitude(), points.get(points.size() - 2).getLatitue(), points.get(points.size() - 2).getLongtute());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(TAG, "onStatusChanged: OUT_OF_SERVICE");
                    break;
                case LocationProvider.AVAILABLE:
                    Log.i(TAG, "onStatusChanged:AVAILABLE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(TAG, "onStatusChanged: TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

}
