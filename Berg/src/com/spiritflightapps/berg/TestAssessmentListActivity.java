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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.spiritflightapps.berg.contentprovider.AssessmentContentProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    private Gallery mListView;
    private ArrayList<EditText> mEditBoxes2;

    ArrayList<ImageButton> instructionButtons;
    ArrayList<String> mInstructions;
//icon from http://www.flaticon.com/free-icon/falling-man-silhouette_11015

    TextView tvTotal,tvTotal2;
    //TODO: Goals column

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_listview);
        mListView = (Gallery) (findViewById(R.id.galleryTests));
        mListView.setSpacing(1);
        //   mListView.setDividerHeight(2);

        registerForContextMenu(mListView);
        String name = this.getIntent().getStringExtra(EXTRA_NAME);
        mPatientId = this.getIntent().getStringExtra(EXTRA_PATIENT_ID);
        this.setTitle(mPatientId + name);
        fillData();
        mEditBoxes2 = new ArrayList<EditText>();
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ1_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ2_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ3_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ4_V2));

        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ5_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ6_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ7_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ8_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ9_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ10_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ11_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ12_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ13_V2));
        mEditBoxes2.add((EditText) findViewById(R.id.editTextQ14_V2));
        tvTotal2 = (TextView) findViewById(R.id.textViewTotalV2);
        setupEditBoxes(mEditBoxes2, tvTotal2);
    }


    public static Intent newIntent(Context context, String name, String patientId) {
        Intent i = new Intent(context, TestAssessmentListActivity.class);
        i.putExtra(EXTRA_NAME, name);
        i.putExtra(EXTRA_PATIENT_ID, patientId);
        return i;
    }


    private void createTestAssessment() {
        insertAssessment(getDateTime(), "1");
    }


    private String getDateTime() {
        //  SimpleDateFormat dateFormat = new SimpleDateFormat(
        //         "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "mm:ss", Locale.getDefault());
        //TODO: This would be just date! FIXME; Just for demo purposes.
        Date date = new Date();
        return dateFormat.format(date);

    }

    private void insertAssessment(String date, ArrayList<EditText> editBoxes) {
        ContentValues values = new ContentValues();
        values.put(AssessmentTable.COLUMN_DATE, date);
        values.put(AssessmentTable.COLUMN_PATIENT_ID, mPatientId);

        values.put(AssessmentTable.COLUMN_Q1, editBoxes.get(0).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q2, editBoxes.get(1).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q3, editBoxes.get(2).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q4, editBoxes.get(3).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q5, editBoxes.get(4).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q6, editBoxes.get(5).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q7, editBoxes.get(6).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q8, editBoxes.get(7).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q9, editBoxes.get(8).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q10, editBoxes.get(9).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q11, editBoxes.get(10).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q12, editBoxes.get(11).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q13, editBoxes.get(12).getText().toString().trim());
        values.put(AssessmentTable.COLUMN_Q14, editBoxes.get(13).getText().toString().trim());

        assessmentUri = getContentResolver().insert(AssessmentContentProvider.CONTENT_URI, values);

    }

    private void insertAssessment(String date, String a1) {
        ContentValues values = new ContentValues();
        values.put(AssessmentTable.COLUMN_DATE, date);
        values.put(AssessmentTable.COLUMN_PATIENT_ID, mPatientId);
        values.put(AssessmentTable.COLUMN_Q1, a1);
        //q1,name
        assessmentUri = getContentResolver().insert(AssessmentContentProvider.CONTENT_URI, values);
        //it has a uri so next time it can update is the idea
        //TODO: Date
    }


    private void fillData() {

        // Fields from the database (projection)
        // Must include the _id column for the adapter to work
        String[] from = new String[]{AssessmentTable.COLUMN_DATE, AssessmentTable.COLUMN_Q1, AssessmentTable.COLUMN_Q2, AssessmentTable.COLUMN_Q3};
        // Fields on the UI to which we map
        int[] to = new int[]{R.id.TextViewDate, R.id.TextViewQ1, R.id.TextViewQ2, R.id.TextViewQ3};

        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_row_test, null, from,
                to, 0);

        mListView.setAdapter(adapter);
    }


    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO: Add a createdate field here to be displayed in the listview.
        String[] projection = {AssessmentTable.COLUMN_ID, AssessmentTable.COLUMN_DATE, AssessmentTable.COLUMN_Q1, AssessmentTable.COLUMN_Q2, AssessmentTable.COLUMN_Q3};
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

    private void setupEditBoxes(ArrayList<EditText> editBoxesColumn, TextView tvTotal) {
        //TODO: autofill goal column

        final ArrayList<EditText> editBoxes = editBoxesColumn;
        final TextView textViewTotal = tvTotal;

        for (int i = 0; i < editBoxes.size(); i++) {
            final int next = i + 1;
            final EditText e = editBoxes.get(i);
            e.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int answer = Integer.MIN_VALUE;
                    String error = "";
                    if (s.length() == 0) {
                        return; //backspace to correct
                    }
                    try {
                        answer = Integer.parseInt(s.toString());
                    } catch (Exception e) {
                        error = "Please enter a number.";
                    }
                    //TODO: strings.xml
                    if (answer > 4) {
                        error = "Please enter 0-4.";
                    }

                    if (error.length() > 0) {
                        Toast.makeText(TestAssessmentListActivity.this.getApplicationContext(), error, Toast.LENGTH_LONG).show();

                    } else {
                        //autoadvance
                        if (next < editBoxes.size()) {
                            EditText nextEditBox = editBoxes.get(next);
                            nextEditBox.requestFocus();
                        }
                        //autocalculate

                        calculateTotalIfAllFilledIn(editBoxes, textViewTotal);
                    }


                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });
        }


    }

    private void calculateTotalIfAllFilledIn(ArrayList<EditText> editBoxes, TextView textViewTotal) {
        int total = 0;
        int score = 0;
        try {
            for (EditText editBox : editBoxes) {
                score = Integer.parseInt(editBox.getText().toString());
                if (score > 4) {
                    throw (new Exception()); //TODO: Detail which one via tag in xml
                }
                total += score;
            }
            //  Toast.makeText(AssessmentActivity.this.getApplicationContext(), "Total Score=" + total, Toast.LENGTH_SHORT).show();
            //TODO: Just remove? or use only the first time?
            textViewTotal.setText(String.valueOf(total));

        } catch (Exception e) {
            //Validation done in each field. if blank or one missing, do nothing

        }
    }
}