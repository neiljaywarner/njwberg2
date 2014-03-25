package com.spiritflightapps.berg;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BergDatabaseHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "todotable.db";
  private static final int DATABASE_VERSION = 1;

  public BergDatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	Log.i("NJW","dbhelper constructor");

  }

  // Method is called during creation of the database
  @Override
  public void onCreate(SQLiteDatabase database) {
		Log.i("NJW","oncreate in dbhelper");

    AssessmentTable.onCreate(database);
  }

  // Method is called during an upgrade of the database,
  // e.g. if you increase the database version
  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    AssessmentTable.onUpgrade(database, oldVersion, newVersion);
  }
}
 