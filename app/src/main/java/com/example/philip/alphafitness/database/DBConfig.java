package com.example.philip.alphafitness.database;

import android.provider.BaseColumns;

class DBConfig {

    static class RunningData implements BaseColumns {
        static final String TABLE_NAME = "runningData";
        static final String LONGITUDE = "longitude";
        static final String LATITUDE = "latitude";
        static final String TIMESTAMP = "timestamp";
        static final String WORKOUT_ID = "workout_id";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LONGITUDE + " REAL, " +
                LATITUDE + " REAL, " +
                TIMESTAMP + " INTEGER," +
                WORKOUT_ID + " INTEGER)";
    }

    static class WorkoutDetails implements BaseColumns {
        static final String TABLE_NAME = "workoutDetails";
        static final String WORKOUT_ID = "workout_id";
        static final String STEP_COUNT = "step_count";
        static final String DURATION = "duration";
        static final String DISTANCE = "distance";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STEP_COUNT + " INTEGER, " +
                DURATION + " INTEGER, " +
                DISTANCE + " REAL, " +
                WORKOUT_ID + " INTEGER)";
    }

    static class StopWatch implements BaseColumns {
        static final String TABLE_NAME = "stopWatch";
        static final String WORKOUT_ID = "workout_id";
        static final String TIME_PER_KM = "time_per_km";

        static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIME_PER_KM + " INTEGER, " +
                WORKOUT_ID + " INTEGER)";
    }


}
