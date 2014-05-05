package com.spiritflightapps.berg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BergDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "berg.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "BergDatabaseHelper" ;

    public BergDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method is called during creation of the database
    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.i(TAG, "onCreate Tables in Database helper");

        AssessmentTable.onCreate(database);
        PatientTable.onCreate(database);
    }

    // Method is called during an upgrade of the database,
    // e.g. if you increase the database version
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion,
                          int newVersion) {
        AssessmentTable.onUpgrade(database, oldVersion, newVersion);
        PatientTable.onUpgrade(database, oldVersion, newVersion);

    }
}
 