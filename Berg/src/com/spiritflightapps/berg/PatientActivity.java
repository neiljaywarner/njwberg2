package com.spiritflightapps.berg;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.widget.Toast;


import com.spiritflightapps.berg.contentprovider.MyPatientContentProvider;
import com.spiritflightapps.berg.contentprovider.AssessmentContentProvider;


public class PatientActivity extends Activity {
    private EditText mTitleText;
    private EditText mBodyText;

    private Uri uri;

    /**
     * TODO: :Perhaps use for edit
     * @param context
     * @param name
     * @param patientId
     * @return
     */
    public static Intent newIntent(Context context, String name, String patientId) {
        Intent i = new Intent(context, PatientActivity.class);
        return i;
    }

    /**
     * For now it's just InsertPatient
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        Intent i = new Intent(context, PatientActivity.class);
        return i;
    }
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_patient);

        mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
        Button confirmButton = (Button) findViewById(R.id.todo_edit_button);

        Bundle extras = getIntent().getExtras();

        // check from the saved Instance
        uri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyPatientContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            uri = extras
                    .getParcelable(MyPatientContentProvider.CONTENT_ITEM_TYPE);

           // fillData(uri);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TextUtils.isEmpty(mTitleText.getText().toString())) {
                    makeToast();
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            }

        });
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyPatientContentProvider.CONTENT_ITEM_TYPE, uri);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String summary = mTitleText.getText().toString();

        if ( summary.length() == 0) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(PatientTable.COLUMN_PATIENT_TITLE, summary);

        if (uri == null) {
            // New patient
            uri = getContentResolver().insert(MyPatientContentProvider.CONTENT_URI, values);
            String patientId =  uri.getLastPathSegment();
            insertBlankVisit(patientId);


        } else {
            // Update todo - NOT IMPLEMENTED as of 5-26-2014 7:20pm, although probably no reason not to with longpress.
            getContentResolver().update(uri, values, null, null);
        }
    }

    private void insertBlankVisit(String patientId) {
        BergDatabaseHelper database;
        database = new BergDatabaseHelper(getApplicationContext());

        SQLiteDatabase sqlDB = database.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AssessmentTable.COLUMN_PATIENT_ID, patientId);
        sqlDB.insert(AssessmentTable.TABLE_ASSESSMENTS, null, contentValues);
        sqlDB.close();

    }
    //TODO: FIx to use contentsresolvers properly or not at all.

    private void makeToast() {
        Toast.makeText(PatientActivity.this, "Please provide a name or identifying field like initials+city",
                Toast.LENGTH_LONG).show();
    }
}