package com.spiritflightapps.berg;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.spiritflightapps.berg.contentprovider.AssessmentContentProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
*
* You can create new ones via the ActionBar entry "Insert"
*/

public class TestAssessmentListActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int DELETE_ID = Menu.FIRST + 1;
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PATIENT_ID = "patient_id";
    private SimpleCursorAdapter adapter;
    private Uri assessmentUri;
    private String mPatientId;

    private ListView mListView;

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_listview);
        mListView = (ListView) (findViewById(R.id.listViewTests));
        mListView.setDividerHeight(2);

        registerForContextMenu(mListView);
        String name = this.getIntent().getStringExtra(EXTRA_NAME);
        mPatientId = this.getIntent().getStringExtra(EXTRA_PATIENT_ID);
        this.setTitle(mPatientId + name);
        fillData();

    }


    public static Intent newIntent(Context context, String name, String patientId) {
        Intent i = new Intent(context, TestAssessmentListActivity.class);
        i.putExtra(EXTRA_NAME, name);
        i.putExtra(EXTRA_PATIENT_ID, patientId);
        return i;
    }

    // create the menu based on the XML defintion
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    // Reaction to the menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert:
                createItem();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void createItem() {
        createTestAssessment();
    }

    private void createTestAssessment() {
        insertAssessment("Visit from " + getDateTime());
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }

    private void insertAssessment(String date) {
        ContentValues values = new ContentValues();
        values.put(AssessmentTable.COLUMN_DATE, date);
        values.put(AssessmentTable.COLUMN_PATIENT_ID, mPatientId);
        //q1,name
        assessmentUri = getContentResolver().insert(AssessmentContentProvider.CONTENT_URI, values);
        //it has a uri so next time it can update is the idea
        //TODO: Date
    }


    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{AssessmentTable.COLUMN_DATE};
        // Fields on the UI to which we map
        int[] to = new int[]{R.id.label};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from,
                to, 0);

        mListView.setAdapter(adapter);
    }


    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO: Add a createdate field here to be displayed in the listview.
        String[] projection = {AssessmentTable.COLUMN_ID, AssessmentTable.COLUMN_DATE};
        String[] selectionArgs = {mPatientId};
        CursorLoader cursorLoader = new CursorLoader(this,
                AssessmentContentProvider.CONTENT_URI, projection, "patient_id=?", selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

}