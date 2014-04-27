package com.spiritflightapps.berg.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

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

public class MyContentProvider extends ContentProvider {

  // database
  private BergDatabaseHelper database;

  // used for the UriMacher
  private static final int TODOS = 10;
  private static final int TODO_ID = 20;

            //TODO: Rename stuff away from the example to actual berg stuff!!!
  private static final String AUTHORITY = "com.spritflightapps.berg.contentprovider.MyToDoContentProvider";

  private static final String BASE_PATH = "todos";
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
      + "/" + BASE_PATH);

  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
      + "/todos";
  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
      + "/todo";

  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  static {
    sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
    sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
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
    queryBuilder.setTables(AssessmentTable.TABLE_TODO);

    int uriType = sURIMatcher.match(uri);
    switch (uriType) {
    case TODOS:
      break;
    case TODO_ID:
      // adding the ID to the original query
      queryBuilder.appendWhere(AssessmentTable.COLUMN_ID + "="
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

  @Override
  public Uri insert(Uri uri, ContentValues values) {
    int uriType = sURIMatcher.match(uri);
    SQLiteDatabase sqlDB = database.getWritableDatabase();
    int rowsDeleted = 0;
    long id = 0;
    switch (uriType) {
    case TODOS:
      id = sqlDB.insert(AssessmentTable.TABLE_TODO, null, values);
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
    case TODOS:
      rowsDeleted = sqlDB.delete(AssessmentTable.TABLE_TODO, selection,
          selectionArgs);
      break;
    case TODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsDeleted = sqlDB.delete(AssessmentTable.TABLE_TODO,
            AssessmentTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsDeleted = sqlDB.delete(AssessmentTable.TABLE_TODO,
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
    case TODOS:
      rowsUpdated = sqlDB.update(AssessmentTable.TABLE_TODO, 
          values, 
          selection,
          selectionArgs);
      break;
    case TODO_ID:
      String id = uri.getLastPathSegment();
      if (TextUtils.isEmpty(selection)) {
        rowsUpdated = sqlDB.update(AssessmentTable.TABLE_TODO, 
            values,
            AssessmentTable.COLUMN_ID + "=" + id, 
            null);
      } else {
        rowsUpdated = sqlDB.update(AssessmentTable.TABLE_TODO, 
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
//TODO: IS this really  needed.. TODO: Delete or refactor so that this list is not in 2 places!
  private void checkColumns(String[] projection) {
    String[] available = { 
        AssessmentTable.COLUMN_SUMMARY, AssessmentTable.COLUMN_Q1, AssessmentTable.COLUMN_Q2, AssessmentTable.COLUMN_Q3, AssessmentTable.COLUMN_Q4, AssessmentTable.COLUMN_Q5,
        AssessmentTable.COLUMN_Q6, AssessmentTable.COLUMN_Q7, AssessmentTable.COLUMN_Q8, AssessmentTable.COLUMN_Q9, AssessmentTable.COLUMN_Q10, AssessmentTable.COLUMN_Q11,
        AssessmentTable.COLUMN_Q12,
        AssessmentTable.COLUMN_Q13,
        AssessmentTable.COLUMN_Q14,
        AssessmentTable.COLUMN_ID };
    if (projection != null) {
      HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
      HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));
      // check if all columns which are requested are available
      if (!availableColumns.containsAll(requestedColumns)) {
        throw new IllegalArgumentException("Unknown columns in projection");
      }
    }
  }

} 
