package com.example.philip.alphafitness;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        SensorEventListener {

    private GoogleMap mMap;
    Button startStopWorkout;

    private Long steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startStopWorkout = (Button) findViewById(R.id.button);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }





    /// REQUIRED TO IMPLEMENT SENSOREVENTLISTENER
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int val = -1;

        if(values.length > 0) {
            val = (int) values[0];
        }

        if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps += 1;
            Log.d("Number of steps: ", Long.toString(steps));
        }
    }
    public float getDistance() {
        return (float)(steps * 50) / (float) 10000;
    }



    /// REQUIRED TO IMPLEMENT SENSOREVENTLISTENER
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }





}
