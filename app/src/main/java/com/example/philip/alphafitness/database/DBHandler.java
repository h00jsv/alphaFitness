package com.example.philip.alphafitness.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.philip.alphafitness.Calculator;
import com.example.philip.alphafitness.background.RunningService;
import com.example.philip.alphafitness.background.StepCounter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DBHandler {

    private Context context;
    private static long workoutID = 0;

    public DBHandler(Context context){
        this.context = context;
    }

    public static void createNewID(){
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        workoutID = date.getTime();
    }

    public static long getCurrentWorkoutID() {
        return workoutID;
    }

    public void saveToDB(Double longitude, Double latitude) {
        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConfig.RunningData.LONGITUDE, longitude);
        values.put(DBConfig.RunningData.LATITUDE, latitude);
        values.put(DBConfig.RunningData.WORKOUT_ID, workoutID);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();

        values.put(DBConfig.RunningData.TIMESTAMP, date.getTime());

        long newRowId = database.insert(DBConfig.RunningData.TABLE_NAME, null, values);

        Toast.makeText(context, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }


    public ArrayList<DataHolder> readFromDB() {

        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getReadableDatabase();

        String[] projection = {
                DBConfig.RunningData._ID,
                DBConfig.RunningData.LONGITUDE,
                DBConfig.RunningData.LATITUDE,
                DBConfig.RunningData.TIMESTAMP,
                DBConfig.RunningData.WORKOUT_ID
        };

        Cursor cursor = database.query(
                DBConfig.RunningData.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        Log.d("read sql", "The total cursor count is " + cursor.getCount());


        cursor.moveToFirst();
        ArrayList<DataHolder> dataHolder = new ArrayList<>();

        while(cursor.moveToNext()) {
            Double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConfig.RunningData.LONGITUDE));
            Double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConfig.RunningData.LATITUDE));
            long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DBConfig.RunningData.TIMESTAMP));
            Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(DBConfig.RunningData.WORKOUT_ID));

            Date date = new Date(timestamp);

            DataHolder data = new DataHolder(longitude, latitude, date, id);
            dataHolder.add(data);
        }


        return dataHolder;
    }

    public Integer getWorkoutCount(){

        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getReadableDatabase();
        String[] projection = {
                DBConfig.WorkoutDetails.WORKOUT_ID
        };

        Cursor cursor = database.query(true, DBConfig.WorkoutDetails.TABLE_NAME, projection, null, null, DBConfig.WorkoutDetails.WORKOUT_ID, null, null, null);

        return cursor.getCount();
    }


    public void safeWorkoutDetails(){
        Integer steps = StepCounter.getSteps();
        Log.e("stepsToSafe", Integer.toString(steps));

        long currentTime = SystemClock.uptimeMillis();
        long duration = currentTime - RunningService.getStartTime();
        Double distance = Calculator.getDistance();

        Log.e("distancesToSafe", Double.toString(distance));

        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConfig.WorkoutDetails.DISTANCE, distance);
        values.put(DBConfig.WorkoutDetails.STEP_COUNT, steps);
        values.put(DBConfig.WorkoutDetails.DURATION, duration);
        values.put(DBConfig.WorkoutDetails.WORKOUT_ID, workoutID);

        database.insert(DBConfig.WorkoutDetails.TABLE_NAME, null, values);

    }

    public ArrayList<WorkoutData> getWorkoutData(){
        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getReadableDatabase();

        String[] projection = {
                DBConfig.WorkoutDetails.DISTANCE,
                DBConfig.WorkoutDetails.DURATION,
                DBConfig.WorkoutDetails.STEP_COUNT,
                DBConfig.WorkoutDetails.WORKOUT_ID,
        };

        Cursor cursor = database.query(
                DBConfig.WorkoutDetails.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        cursor.moveToFirst();
        ArrayList<WorkoutData> dataHolder = new ArrayList<>();

        Log.e("read sql", "The total cursor count is " + cursor.getCount());

        Integer index = 0;

        if (cursor.getCount() <= 0){
            return dataHolder;
        }

        do {
            Double distance = cursor.getDouble(cursor.getColumnIndexOrThrow(DBConfig.WorkoutDetails.DISTANCE));
            long duration = cursor.getLong(cursor.getColumnIndexOrThrow(DBConfig.WorkoutDetails.DURATION));
            Integer steps = cursor.getInt(cursor.getColumnIndexOrThrow(DBConfig.WorkoutDetails.STEP_COUNT));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBConfig.WorkoutDetails.WORKOUT_ID));

            Log.e("read sql", "The steps of a workout are: " + Integer.toString(steps));

            WorkoutData data = new WorkoutData(steps, duration, distance, id);
            dataHolder.add(data);
            System.out.println(index);
            index++;
        }while(cursor.moveToNext());

        Log.e("Total steps", "The steps of first the workouts is: " + Integer.toString(dataHolder.get(0).getSteps()));

        return dataHolder;
    }



    public void safeTimeForKM(long time){

        Log.e("stopSafe", "safe - "+Long.toString(time));

        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DBConfig.StopWatch.TIME_PER_KM, time);
        values.put(DBConfig.StopWatch.WORKOUT_ID, workoutID);

        database.insert(DBConfig.StopWatch.TABLE_NAME, null, values);

    }

    public ArrayList<StopWatch> getStopWatchData(){
        SQLiteDatabase database = new SampleDBSQLiteHelper(context).getReadableDatabase();

        String[] projection = {
                DBConfig.StopWatch.TIME_PER_KM,
                DBConfig.StopWatch.WORKOUT_ID,
        };

        Cursor cursor = database.query(
                DBConfig.StopWatch.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // don't sort
        );

        cursor.moveToFirst();
        ArrayList<StopWatch> stops = new ArrayList<>();

        Log.e("read sql", "The total cursor count is " + cursor.getCount());

        if (cursor.getCount() <= 0){
            return stops;
        }

        do {
            long time = cursor.getLong(cursor.getColumnIndexOrThrow(DBConfig.StopWatch.TIME_PER_KM));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBConfig.StopWatch.WORKOUT_ID));

            StopWatch data = new StopWatch(id, time);
            stops.add(data);

        }while(cursor.moveToNext());

        return stops;
    }



}
