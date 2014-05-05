package com.spiritflightapps.berg.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.spiritflightapps.berg.PatientTable;
import com.spiritflightapps.berg.BergDatabaseHelper;

import java.util.Arrays;
import java.util.HashSet;

public class MyPatientContentProvider extends ContentProvider {

  // database
  private BergDatabaseHelper database;

  // used for the UriMacher
  private static final int PATIENTS = 10;
  private static final int PATIENT_ID = 20;

  private static final String AUTHORITY = "com.spiritflightapps.berg.contentprovider.MyPatientContentProvider";

  private static final String BASE_PATH = "patients";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/patients";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/patient";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, PATIENTS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PATIENT_ID);
  }

  @Override
  public boolean onCreate() {
	Log.i("NJW","in oncreate of contetprovider");
    database = new BergDatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

    // check if the caller has requested a column which does not exists
//    checkColumns(projection); //? Refactor so that the column list is NOT in 2 places.

    // Set the table
    queryBuilder.setTables(PatientTable.TABLE_PATIENTS);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case PATIENTS:
      break;
    case PATIENT_ID:
      // adding the ID to the original query
      queryBuilder.appendWhere(PatientTable.COLUMN_ID + "="
          + uri.getLastPathSegment());
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    SQLiteDatabase db = database.getWritableDatabase();
    Cursor cursor = queryBuilder.query(db, projection, selection,
        selectionArgs, null, null, sortOrder);
    // make sure that potential listeners are getting notified
    cursor.setNotificationUri(getContext().getContentResolver(), uri);

    return cursor;
  }

  @Override
  public String getType(Uri uri) {
    return null;
  }

    //NOTE: WE could combine this into one contentprovider... sicne differnet switch statements
    //and different URI.
  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case PATIENTS:
      id = sqlDB.insert(PatientTable.TABLE_PATIENTS, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }

  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case PATIENTS:
      rowsDeleted = sqlDB.delete(PatientTable.TABLE_PATIENTS, selection,
          selectionArgs);
      break;
    case PATIENT_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(PatientTable.TABLE_PATIENTS,
            PatientTable.COLUMN_ID + "=" + id,
            null);
      } else {
        rowsDeleted = sqlDB.delete(PatientTable.TABLE_PATIENTS,
            PatientTable.COLUMN_ID + "=" + id
            + " and " + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsDeleted;
  }

  @Override
  public int update(Uri uri, ContentValues values, String selection,
      String[] selectionArgs) {

    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsUpdated = 0;
    switch (uriType) {
    case PATIENTS:
      rowsUpdated = sqlDB.update(PatientTable.TABLE_PATIENTS,
          values, 
          selection,
          selectionArgs);
      break;
    case PATIENT_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(PatientTable.TABLE_PATIENTS,
            values,
            PatientTable.COLUMN_ID + "=" + id,
            null);
      } else {
        rowsUpdated = sqlDB.update(PatientTable.TABLE_PATIENTS,
            values,
            PatientTable.COLUMN_ID + "=" + id
            + " and " 
            + selection,
            selectionArgs);
      }
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return rowsUpdated;
  }


} 

