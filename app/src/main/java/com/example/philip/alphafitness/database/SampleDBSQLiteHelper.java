package com.example.philip.alphafitness.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SampleDBSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "workoutData";

    public SampleDBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DBConfig.RunningData.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBConfig.WorkoutDetails.CREATE_TABLE);
        sqLiteDatabase.execSQL(DBConfig.StopWatch.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.RunningData.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.WorkoutDetails.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DBConfig.StopWatch.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
