/**
 * 
 */
package com.spiritflightapps.berg;

/**
 * these from http://www.vogella.com/tutorials/AndroidSQLite/article.html
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PatientTable {

  // Database table
  public static final String TABLE_PATIENTS = "patients";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_PATIENT_TITLE = "patient_ref";
  public static final String COLUMN_CITY = "city";
    public static final String COLUMN_DATE = "date";


  public static final String[] DEFAULT_PROJECTION = {
          PatientTable.COLUMN_PATIENT_TITLE,
          PatientTable.COLUMN_CITY,
          PatientTable.COLUMN_DATE

  };

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_PATIENTS
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_PATIENT_TITLE + " text not null,"
      + COLUMN_CITY + " text ,"
          + COLUMN_DATE + " text "

      + ");";

  public static void onCreate(SQLiteDatabase database) {
	  Log.i("NJW","Creating database in table helper class with actual sql statement");
      database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(PatientTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_PATIENTS);
    onCreate(database);
  }
} 