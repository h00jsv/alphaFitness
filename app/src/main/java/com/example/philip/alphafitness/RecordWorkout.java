package com.example.philip.alphafitness;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.philip.alphafitness.background.RunningService;
import com.example.philip.alphafitness.database.DBHandler;
import com.example.philip.alphafitness.userProfile.UserProfile;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecordWorkout extends FragmentActivity implements OnMapReadyCallback {

    String LOG_TAG = "RecordWorkout";


    private String MAP_VIEW_BUNDLE_KEY;
    private MapView mapView;
    CameraPosition.Builder camBuilder;
    double lat;
    double lng;
    static private Boolean workoutStarted = false;

    private Button workoutButton;
    SensorManager sensorManager;
    Sensor pedometer;
    Handler handler;

    private ArrayList<LatLng> locationList = new ArrayList<>();
    private int Seconds, Minutes;
    private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L;
    private TextView duration;
    private TextView distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_workout);

        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            goToLandscape();
        }

        MAP_VIEW_BUNDLE_KEY = getResources().getString(R.string.google_maps_key);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        camBuilder = CameraPosition.builder();
        camBuilder.bearing(0);
        camBuilder.tilt(30);
        camBuilder.zoom(18);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        workoutButton = findViewById(R.id.button);
        duration = findViewById(R.id.tv_duration);
        distance = findViewById(R.id.tv_distance);
        handler = new Handler();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pedometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);


        workoutStarted = isServiceRunning();

        if(workoutStarted){
            Toast.makeText(this, "service is still ongoing", Toast.LENGTH_LONG).show();


            StartTime = RunningService.getStartTime();

            handler.postDelayed(runnable, 0);
        }


    }

    private void goToLandscape(){
        Intent intent = new Intent(this, DetailView.class);

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 13) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"sorry, but now I can't display your location", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // Add a marker in Sydney and move the camera
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);

        } else {

            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

            requestPermissions (permissions, 13);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);

            }else {
                return;
            }
        }

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();

                LatLng sydney = new LatLng(lat, lng);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


                locationList.add(new LatLng(lat,lng));
                if(workoutStarted) {
                    PolylineOptions polylineOptions = new PolylineOptions().width(9).color(Color.BLUE).geodesic(true);
                    for(int i = 0; i < locationList.size(); i++) {
                        LatLng point = locationList.get(i);
                        polylineOptions.add(point);
                    }
                    googleMap.addPolyline(polylineOptions);
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) { }
            public void onProviderEnabled(String provider) { }
            public void onProviderDisabled(String provider) { }
        });

        //TODO making actual request getting a new position

        Location location = locationManager.getLastKnownLocation(provider);

        if (location != null) {
            Toast.makeText(this, "Provider " + provider + " has been selected.", Toast.LENGTH_LONG).show();
            System.out.println("Provider " + provider + " has been selected.");
            lat = location.getLatitude();
            lng = location.getLongitude();

            LatLng sydney = new LatLng(lat, lng);
            camBuilder.target(sydney);
            //googleMap.addMarker(new MarkerOptions().position(sydney).title("You"));
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(camBuilder.build()));


        } else {
            Toast.makeText(this, "Location not available", Toast.LENGTH_LONG).show();

            System.out.println("Location not available");
        }

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setCompassEnabled(true);
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);

    }



    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }


    public void openUserProfile(View v){
        Intent intent = new Intent(getApplicationContext(), UserProfile.class);

        startActivity(intent);
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            MillisecondTime = SystemClock.uptimeMillis() - StartTime;
            UpdateTime = TimeBuff + MillisecondTime;
            Seconds = (int) (UpdateTime / 1000);
            Minutes = Seconds / 60;
            Seconds = Seconds % 60;
            //MilliSeconds = (int) (UpdateTime % 1000);

            duration.setText("" + String.format("%01d", (int)(Minutes/60)) + ":"
                    + String.format("%02d", Minutes % 60) + ":" + String.format("%02d", Seconds));

            distance.setText(String.format(java.util.Locale.US,"%.2f", Calculator.getDistance()));

            handler.postDelayed(this, 0);
        }
    };

//    public float getDistanceRun(){
//        float distance = (float)(steps*78)/(float)100000;
//        //        if(distance < 0.02f)
//        //            return 0.02f;
//        return distance;
//    }

    public void startStopWorkout(View v){


        workoutStarted = !workoutStarted;
        if (workoutStarted){
            DBHandler.createNewID();
            MillisecondTime = 0L;
            StartTime = 0L;
            TimeBuff = 0L;
            UpdateTime = 0L;
            Minutes = 0;
            Seconds = 0;

            workoutButton.setText("STOP WORKOUT");
            workoutButton.setBackgroundColor(Color.RED);
            workoutButton.setTextColor(Color.WHITE);

            StartTime = SystemClock.uptimeMillis();
            handler.postDelayed(runnable, 0);
            // TODO: following:
            //handler.removeCallbacks(writeToDatabase)
            // increment number of workouts


            String input = "Have a great workout, " + UserProfile.getName() + "!";

            Intent serviceIntent = new Intent(this, RunningService.class);
            serviceIntent.putExtra("inputExtra", input);

            ContextCompat.startForegroundService(this, serviceIntent);


        } else {
            workoutButton.setText("START WORKOUT!");
            workoutButton.setBackgroundColor(Color.GREEN);
            workoutButton.setTextColor(Color.BLACK);
            workoutStarted = false;
            handler.removeCallbacks(runnable);

            // retrieve distance
            //distance.setText(decimalFormat.format(getDistanceRun()));  // crashes when clicking 'Stop workout'

            // TODO: WRITE WORKOUT SESSION TO DATABASE
            //handler.postDelayed(WRITE TO DATABASE, 0);


            Intent serviceIntent = new Intent(this, RunningService.class);
            stopService(serviceIntent);

            duration.setText("0:00:00");

        }

    }


    private Boolean isServiceRunning() {
        final ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        boolean isServiceFound = false;

        for (int i = 0; i < services.size(); i++) {

            if ("com.example.philip.alphafitness.background.RunningService".equals(services.get(i).service.getClassName())) {

                isServiceFound = true;
            }

        }


        return isServiceFound;
    }

}
