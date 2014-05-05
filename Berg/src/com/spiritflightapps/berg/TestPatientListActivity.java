package com.spiritflightapps.berg;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.spiritflightapps.berg.contentprovider.MyPatientContentProvider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 *
 * You can create new ones via the ActionBar entry "Insert"
 * You can delete existing ones via a long press on the item
 */

public class TestPatientListActivity extends ListActivity implements
    LoaderManager.LoaderCallbacks<Cursor> {
  private static final int ACTIVITY_CREATE = 0;
  private static final int ACTIVITY_EDIT = 1;
  private static final int DELETE_ID = Menu.FIRST + 1;
  // private Cursor cursor;
  private SimpleCursorAdapter adapter;
    private Uri patientUri;

  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.patient_list);
    this.getListView().setDividerHeight(2);
    fillData();
    registerForContextMenu(getListView());
   // if (this.adapter.getCount() == 0) {
  //  	createItem();
   // }
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

  @Override
  public boolean onContextItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case DELETE_ID:
      AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
          .getMenuInfo();
      Uri uri = Uri.parse(MyPatientContentProvider.CONTENT_URI + "/"
          + info.id);
      getContentResolver().delete(uri, null, null);
      fillData();
      return true;
    }
    return super.onContextItemSelected(item);
  }

  private void createItem() {
      //TODO: Pass to testPatientListactivity
  //  Intent i = new Intent(this, AssessmentActivity.class);
  //  startActivity(i);
      createTestPatient();
  }

  private void createTestPatient() {
        insertPatient("Joe " + getDateTime());
  }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);

    }
  private void insertPatient(String name) {
      ContentValues values = new ContentValues();
      values.put(PatientTable.COLUMN_PATIENT_TITLE, name);
      patientUri = getContentResolver().insert(MyPatientContentProvider.CONTENT_URI, values);
                //it has a uri so next time it can update is the idea
      //TODO: Date
  }
  /* TODO for better design, and put the cursor stuff in the patient model object
  private void insertPatient(Patient patient) {
      ContentValues values = new ContentValues();
      values.put(PatientTable.COLUMN_CATEGORY, category);
      values.put(TodoTable.COLUMN_SUMMARY, summary);
      values.put(TodoTable.COLUMN_DESCRIPTION, description);
  }
  */

  // Opens the second activity if an entry is clicked
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Intent i = new Intent(this, TestAssessmentListActivity.class);
    TextView tvName = (TextView) v.findViewById(R.id.label);
      Log.i("NJW", tvName.getText().toString());
  //  Uri uri = Uri.parse(MyPatientContentProvider.CONTENT_URI + "/" + id);
   // i.putExtra(MyPatientContentProvider.CONTENT_ITEM_TYPE, uri);
 //   i.putStringExtra("name", tvName.getText().toString(),"");
 //   i.putStringExtra("patient_id", String.valueOf(id),"");
      String patientId = String.valueOf(id);
      i.putExtra("name",tvName.getText());
      i.putExtra("patient_id", patientId);
    startActivity(i);
  }

  

  private void fillData() {

    // Fields from the database (projection)
    // Must include the _id column for the adapter to work
    String[] from = new String[] { PatientTable.COLUMN_PATIENT_TITLE};
    // Fields on the UI to which we map
    int[] to = new int[] { R.id.label };

    getLoaderManager().initLoader(0, null, this);
    adapter = new SimpleCursorAdapter(this, R.layout.list_row, null, from,
        to, 0);

    setListAdapter(adapter);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v,
      ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.add(0, DELETE_ID, 0, "delete");
    //TODO: are we sitl going to do this this way? if so, put in strings.xml
  }

  // creates a new loader after the initLoader () call
  @Override
  public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO: Add a createdate field here to be displayed in the listview.
    String[] projection = { PatientTable.COLUMN_ID, PatientTable.COLUMN_PATIENT_TITLE};
    CursorLoader cursorLoader = new CursorLoader(this,
        MyPatientContentProvider.CONTENT_URI, projection, null, null, null);
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