//package com.spiritflightapps.berg;
//
//import android.app.Activity;
//import android.content.ContentValues;
//import android.content.CursorLoader;
//import android.content.Loader;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.SimpleCursorAdapter;
//
//import com.spiritflightapps.berg.contentprovider.MyContentProvider;
//import com.spiritflightapps.berg.contentprovider.MyPatientContentProvider;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//
//public class TestItemListActivity extends Activity {
//    private ListView mListView;
//    private EditText mEditTextQ1; //test purposes
//    private EditText mEditTextDate;
//    private Uri testURI;
//    private SimpleCursorAdapter adapter;
//
//    //for test purposes
//    private static int sAutoA1 = -1;
//
//  //NOTE: BEHAVIOUR TO CREATE AN ITEM is by NOT leaving q1 blank (for TestItemList 5-1-2014 1:30pm
//
//
//  @Override
//  protected void onCreate(Bundle bundle) {
//      super.onCreate(bundle);
//      setContentView(R.layout.test_listview);
//    //  mEditTextQ1 = (EditText) findViewById(R.id.todo_edit_summary);
//    //  mEditTextDate = (EditText) findViewById(R.id.todo_edit_summary);
//       mListView = (ListView) findViewById(R.id.listViewTests);
//
//      Bundle extras = getIntent().getExtras();
//
//      // check from the saved Instance
//      testURI = (bundle == null) ? null : (Uri) bundle
//          .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);
//
//      // Or passed from the other activity
//      if (extras != null) {
//        testURI = extras
//            .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);
//            String name = extras.getString("name");
//        this.setTitle(name);
//      }
//
//      //FIXME: for test purposes only
//      createFakeTest();
//      mListView.setAdapter(adapter);
//    private void fillData() {
//
//        // Fields from the database (projection)
//        // Must include the _id column for the adapter to work
//        String[] from = new String[] { PatientTable.COLUMN_PATIENT_TITLE};
//        // Fields on the UI to which we map
//        int[] to = new int[] { R.id.tvItemDate, R.id.tvQ1Ans };
//
//        getLoaderManager().initLoader(0, null, this);
//        adapter = new SimpleCursorAdapter(this, R.layout.list_row_test, null, from,
//                to, 0);
//
//        mListView.setAdapter(adapter);
//    }
//  }
//
//
//    /**
//     * TODO: DELETE, for dev/test purposes
//     */
//    private void createFakeTest() {
//        saveTest();
//    }
//
//    /**
//     * always save but only advance if all fields are filled out.
//     * TODO: Change back to save with edittexts as parameter, or arraylist of strings or
//     * test model object better yet
//     *
//     */
//    private void saveTest() {
//        //  if ( q1.length() == 0) {
//        //       return;
//        //   }
//        Log.i("NJW", "trying to save");
//
//      //  String a1 = mEditTextQ1.getText().toString().trim();
//        String date = getDateTime();
//        //Test1 - issue32 ONLY
//        sAutoA1++;
//        if (sAutoA1 > 4) {
//            sAutoA1 = 0;
//        }
//        String q1 = String.valueOf(sAutoA1);
//
//        //****
//     //   String date = mEditTextDate.getText().toString();
//
//        ContentValues values = new ContentValues();
//        // values.put(AssessmentTable.COLUMN_PATIENT_ID, mPatientId);
//        values.put(AssessmentTable.COLUMN_DATE, date);
//        values.put(AssessmentTable.COLUMN_Q1, q1);
//
//
//        if (testURI == null) {
//            // New berg test
//            Log.i("NJW", "about to insert assessment");
//            testURI = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
//        } else {
//            // Update berg test. NOTE: updating hardly ever will happen.
//            Log.i("NJW", "about to update assessment");
//            getContentResolver().update(testURI, values, null, null);
//        }
//    }
//
//
//
//    // creates a new loader after the initLoader () call
//    @Override
//    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        //TODO: Add a createdate field here to be displayed in the listview.
//        String[] projection = { AssessmentTable.COLUMN_ID, AssessmentTable.COLUMN_DATE, AssessmentTable.COLUMN_Q1};
//        CursorLoader cursorLoader = new CursorLoader(this,
//                MyPatientContentProvider.CONTENT_URI, projection, null, null, null);
//        return cursorLoader;
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        adapter.swapCursor(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> loader) {
//        // data is not available anymore, delete reference
//        adapter.swapCursor(null);
//    }
//
//
//
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        saveTest();
//        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, testURI);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        saveTest();
//    }
//
//
//    /**
//     * get datetime
//     * possible todo; date only?
//     * */
//    private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat(
//                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//        Date date = new Date();
//        return dateFormat.format(date);
//    }
//    //TODO: Gallery control only deprecated in Jellybean, will work indefinitely...
//
//}
