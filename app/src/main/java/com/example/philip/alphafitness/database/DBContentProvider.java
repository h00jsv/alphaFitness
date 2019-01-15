package com.example.philip.alphafitness.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class DBContentProvider extends ContentProvider {

    DBHandler dbHandler;

    static final String PROVIDER_NAME = "com.threeAmigos.alphaFitness.DBContentProvider";
    static final String URL = "content://" + PROVIDER_NAME;
    public static final Uri CONTENT_URI = Uri.parse(URL);


    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext());
        DBHandler.createNewID();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        Double longitude = values.getAsDouble("longitude");
        Double latitude = values.getAsDouble("latitude");

        Log.e("Content Provider:", "insert");

        dbHandler.saveToDB(longitude, latitude);

        return CONTENT_URI;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
