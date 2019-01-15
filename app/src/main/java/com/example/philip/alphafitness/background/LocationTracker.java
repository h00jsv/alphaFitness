package com.example.philip.alphafitness.background;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import com.example.philip.alphafitness.Calculator;
import com.example.philip.alphafitness.database.DBContentProvider;
import com.example.philip.alphafitness.database.DBHandler;
import com.google.android.gms.maps.model.LatLng;

public class LocationTracker implements LocationListener {

    private ContentResolver resolver;
    private Location lastSavedLocation;
    private Context context;
    private final Double ONE_METER = 1.0;
    private final Double ONE_KM = 1000.0;
    DBHandler dbHandler;


    private Long stopWatchStartTime;
    private LatLng stopWatchLocationStart;

    LocationTracker(ContentResolver resolver, Context context){
        this.resolver = resolver;
        this.context = context;
        dbHandler = new DBHandler(context);
    }

    @Override
    public void onLocationChanged(Location location) {

        double lat = location.getLatitude();
        double lng = location.getLongitude();
        ContentValues values = new ContentValues();
        Calculator calculator = new Calculator(context);


        if(stopWatchLocationStart == null){
            stopWatchLocationStart = new LatLng(lat, lng);
            stopWatchStartTime = SystemClock.uptimeMillis();
        }

        if(lastSavedLocation == null){
            values.put("longitude", lng);
            values.put("latitude", lat);
            this.lastSavedLocation = location;
            resolver.insert(DBContentProvider.CONTENT_URI, values);

        }else{
            Double d = calculator.getDistanceFromLatLonInMeters(lng, lat, lastSavedLocation.getLongitude(),
                    lastSavedLocation.getLatitude());

            if(d >= ONE_METER){
                values.put("longitude", lng);
                values.put("latitude", lat);
                this.lastSavedLocation = location;
                resolver.insert(DBContentProvider.CONTENT_URI, values);
            }

        }


        Double distanceStopWatch = calculator.getDistanceFromLatLonInMeters(
                stopWatchLocationStart.longitude,
                stopWatchLocationStart.latitude,
                lng,
                lat);

        distanceStopWatch = Math.abs(distanceStopWatch);

        if(distanceStopWatch >= ONE_KM){
            onStopWatchStop();
            stopWatchLocationStart = new LatLng(lat, lng);
        }

    }


    private void onStopWatchStop(){

        long time = SystemClock.uptimeMillis() - stopWatchStartTime;

        dbHandler.safeTimeForKM(time);

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
