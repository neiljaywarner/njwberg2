package com.spiritflightapps.berg.contentprovider;

import com.spiritflightapps.berg.BergDatabaseHelper;
import com.spiritflightapps.berg.AssessmentTable;

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

/**
 * content provider for Assessments not todos...
 */
public class AssessmentContentProvider extends ContentProvider {

  // database
  private BergDatabaseHelper database;

  // used for the UriMacher
  private static final int ASSESSMENTS = 10;
  private static final int ASSESSMENT_ID = 20;

  private static final String AUTHORITY = "com.spiritflightapps.berg.contentprovider.AssessmentContentProvider";

  private static final String BASE_PATH = "tests";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/tests";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/tests";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, ASSESSMENTS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ASSESSMENT_ID);
  }

  @Override
  public boolean onCreate() {
    database = new BergDatabaseHelper(getContext());
    return false;
  }

  @Override
  public Cursor query(Uri uri, String[] projection, String selection,
      String[] selectionArgs, String sortOrder) {

    // Uisng SQLiteQueryBuilder instead of query() method
    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


    // Set the table
    queryBuilder.setTables(AssessmentTable.TABLE_ASSESSMENTS);
    Log.i("NJW","***" + uri.toString());


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

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case ASSESSMENTS:
      id = sqlDB.insert(AssessmentTable.TABLE_ASSESSMENTS, null, values);
      break;
    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return Uri.parse(BASE_PATH + "/" + id);
  }
            //DO NOthing?
  @Override
  public int delete(Uri uri, String selection, String[] selectionArgs) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    switch (uriType) {
    case ASSESSMENTS:
      rowsDeleted = sqlDB.delete(AssessmentTable.TABLE_ASSESSMENTS, selection,
          selectionArgs);
      break;
    case ASSESSMENT_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(AssessmentTable.TABLE_ASSESSMENTS,
            AssessmentTable.COLUMN_ID + "=" + id,
            null);
      } else {
        rowsDeleted = sqlDB.delete(AssessmentTable.TABLE_ASSESSMENTS,
            AssessmentTable.COLUMN_ID + "=" + id
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
    case ASSESSMENTS:
      rowsUpdated = sqlDB.update(AssessmentTable.TABLE_ASSESSMENTS,
          values, 
          selection,
          selectionArgs);
      break;
    case ASSESSMENT_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(AssessmentTable.TABLE_ASSESSMENTS,
            values,
            AssessmentTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(AssessmentTable.TABLE_ASSESSMENTS,
            values,
            AssessmentTable.COLUMN_ID + "=" + id 
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

