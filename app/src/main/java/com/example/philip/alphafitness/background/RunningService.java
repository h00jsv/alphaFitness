package com.example.philip.alphafitness.background;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.philip.alphafitness.R;
import com.example.philip.alphafitness.RecordWorkout;
import com.example.philip.alphafitness.database.DBHandler;


import static com.example.philip.alphafitness.background.App.CHANNEL_ID;

public class RunningService extends Service {

    private StepCounter stepCounter;
    private LocationTracker locationTracker;
    private LocationManager locationManager;
    private SensorManager sensorManager;
    static private long startTime;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("RunningService", "Service started");
        startTime = SystemClock.uptimeMillis();
        stepCounter = new StepCounter();
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        setListeners(sensorManager, stepCounter);

        locationTracker = new LocationTracker(getContentResolver(), this);

        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationManager.getBestProvider(criteria, false);

        setLocationListeners(locationManager, locationTracker);

    }



    private void setListeners(SensorManager sensorManager, SensorEventListener mEventListener){
        sensorManager.registerListener(mEventListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void setLocationListeners(LocationManager locationManager, LocationTracker locationTracker){

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationTracker);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");

        Intent notificationIntent = new Intent(this, RecordWorkout.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Alpha Fitness")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        //do heavy work on a background thread
        //stopSelf();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        DBHandler handler = new DBHandler(this);
        handler.safeWorkoutDetails();

        Log.d("service is ","Destroyed");

        sensorManager.unregisterListener(stepCounter);
        locationManager.removeUpdates(locationTracker);
        locationManager = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static long getStartTime() {
        return startTime;
    }
}
