/**
 * 
 */
package com.spiritflightapps.berg;

/**
 * these from http://www.vogella.com/tutorials/AndroidSQLite/article.html
 */
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AssessmentTable {

  // Database table
  public static final String TABLE_ASSESSMENTS = "assessments";
  public static final String COLUMN_ID = "_id";

    public static final String COLUMN_PATIENT_ID = "patient_id"; //ie FK to patient_id table with ref+city in name
  public static final String COLUMN_DATE = "date";
  public static final String COLUMN_COMMENT = "date"; //optional, v 1.1?
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

  public static final String[] DEFAULT_PROJECTION = {
          AssessmentTable.COLUMN_PATIENT_ID,
          AssessmentTable.COLUMN_DATE,
          AssessmentTable.COLUMN_Q1,
          AssessmentTable.COLUMN_Q2,
          AssessmentTable.COLUMN_Q3,
          AssessmentTable.COLUMN_Q4,
          AssessmentTable.COLUMN_Q5,
          AssessmentTable.COLUMN_Q6,
          AssessmentTable.COLUMN_Q7,
          AssessmentTable.COLUMN_Q8,
          AssessmentTable.COLUMN_Q9,
          AssessmentTable.COLUMN_Q10,
          AssessmentTable.COLUMN_Q11,
          AssessmentTable.COLUMN_Q12,
          AssessmentTable.COLUMN_Q13,
          AssessmentTable.COLUMN_Q14,
  };

    //TODO: Fix/combine these 2...
    public static final String[] LOADTEST_PROJECTION = {
            AssessmentTable.COLUMN_PATIENT_ID,
            AssessmentTable.COLUMN_DATE,
            AssessmentTable.COLUMN_Q1,
            AssessmentTable.COLUMN_Q2,
            AssessmentTable.COLUMN_Q3,
            AssessmentTable.COLUMN_Q4,
            AssessmentTable.COLUMN_Q5,
            AssessmentTable.COLUMN_Q6,
            AssessmentTable.COLUMN_Q7,
            AssessmentTable.COLUMN_Q8,
            AssessmentTable.COLUMN_Q9,
            AssessmentTable.COLUMN_Q10,
            AssessmentTable.COLUMN_Q11,
            AssessmentTable.COLUMN_Q12,
            AssessmentTable.COLUMN_Q13,
            AssessmentTable.COLUMN_Q14,
    };

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_ASSESSMENTS
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_PATIENT_ID + " text not null,"
      + COLUMN_DATE + " text ,"
      + COLUMN_Q1 + " text ,"
      + COLUMN_Q2 + " text,"
      + COLUMN_Q3 + " text,"
      + COLUMN_Q4 + " text,"
      + COLUMN_Q5 + " text,"
      + COLUMN_Q6 + " text,"
      + COLUMN_Q7 + " text,"
      + COLUMN_Q8 + " text,"
      + COLUMN_Q9 + " text,"
      + COLUMN_Q10 + " text,"
      + COLUMN_Q11 + " text,"
      + COLUMN_Q12 + " text,"
      + COLUMN_Q13 + " text,"
      + COLUMN_Q14 + " text"
      + ");";

    public static void onCreate(SQLiteDatabase database) {
	  Log.i("NJW","Creating database in table helper class with actual sql statement");
      database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    Log.w(AssessmentTable.class.getName(), "Upgrading database from version "
        + oldVersion + " to " + newVersion
        + ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
    onCreate(database);
  }
} 