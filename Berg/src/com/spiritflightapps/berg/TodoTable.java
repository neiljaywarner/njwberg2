/**
 * 
 */
package com.spiritflightapps.berg;

/**
 * these from http://www.vogella.com/tutorials/AndroidSQLite/article.html
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoTable {

  // Database table
  public static final String TABLE_TODO = "todo";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_SUMMARY = "patient_ref"; //ie initials+city
  public static final String COLUMN_Q1 = "q1";
  public static final String COLUMN_Q2 = "q2";
  public static final String COLUMN_Q3 = "q3";
  public static final String COLUMN_Q4 = "q4";
  public static final String COLUMN_Q5 = "q5";
  public static final String COLUMN_Q6 = "q6";
  public static final String COLUMN_Q7 = "q7";
  public static final String COLUMN_Q8 = "q8";
  public static final String COLUMN_Q9 = "q9";
  public static final String COLUMN_Q10 = "q10";
  public static final String COLUMN_Q11 = "q11";
  public static final String COLUMN_Q12 = "q12";
  public static final String COLUMN_Q13 = "q13";
  public static final String COLUMN_Q14 = "q14";


  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_TODO
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_SUMMARY + " text not null," 
      + COLUMN_Q1 + " integer,"
      + COLUMN_Q2 + " integer,"
      + COLUMN_Q3 + " integer,"
      + COLUMN_Q4 + " integer,"
      + COLUMN_Q5 + " integer,"
      + COLUMN_Q6 + " integer,"
      + COLUMN_Q7 + " integer,"
      + COLUMN_Q8 + " integer,"
      + COLUMN_Q9 + " integer,"
      + COLUMN_Q10 + " integer,"
      + COLUMN_Q11 + " integer,"
      + COLUMN_Q12 + " integer,"
      + COLUMN_Q13 + " integer,"
      + COLUMN_Q14 + " integer"
      + ");";

  public static void onCreate(SQLiteDatabase database) {
	  Log.i("NJW","Creating database in table helper class with actual sql statement");
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(TodoTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
    onCreate(database);
  }
} 