package com.example.philip.alphafitness.background;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class StepCounter implements SensorEventListener {

    static private Integer steps = 0;

    StepCounter(){
        super();
        steps = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Sensor sensor = sensorEvent.sensor;
        float[] values = sensorEvent.values;
        int val = -1;

        if(values.length > 0) {
            val = (int) values[0];
        }

        Log.d("Sensor(18?) ", Integer.toString(sensor.getType()));

        if(sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps += 1;
            //Log.d("Number of steps: ", Long.toString(steps));
        }
    }
    public static double getDistance() {
        return (double)steps / 2000.0;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public static Integer getSteps() {
        return steps;
    }
}
