package com.spiritflightapps.berg;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.net.Uri;
import android.widget.Toast;


import com.spiritflightapps.berg.contentprovider.MyPatientContentProvider;

public class PatientActivity extends Activity {
    private EditText mTitleText;
    private EditText mBodyText;

    private Uri todoUri;

    /**
     * TODO: :Perhaps use for edit
     * @param context
     * @param name
     * @param patientId
     * @return
     */
    public static Intent newIntent(Context context, String name, String patientId) {
        Intent i = new Intent(context, PatientActivity.class);
      //  i.putExtra(EXTRA_NAME, name);
      //  i.putExtra(EXTRA_PATIENT_ID, patientId);
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
        todoUri = (bundle == null) ? null : (Uri) bundle
                .getParcelable(MyPatientContentProvider.CONTENT_ITEM_TYPE);

        // Or passed from the other activity
        if (extras != null) {
            todoUri = extras
                    .getParcelable(MyPatientContentProvider.CONTENT_ITEM_TYPE);

           // fillData(todoUri);
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
    //Below todo if requested.
/*
    private void fillData(Uri uri) {
        String[] projection = { TodoTable.COLUMN_SUMMARY,
                TodoTable.COLUMN_DESCRIPTION, TodoTable.COLUMN_CATEGORY };
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String category = cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_CATEGORY));

            for (int i = 0; i < mCategory.getCount(); i++) {

                String s = (String) mCategory.getItemAtPosition(i);
                if (s.equalsIgnoreCase(category)) {
                    mCategory.setSelection(i);
                }
            }

            mTitleText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));
            mBodyText.setText(cursor.getString(cursor
                    .getColumnIndexOrThrow(TodoTable.COLUMN_DESCRIPTION)));

            // always close the cursor
            cursor.close();
        }
    }

*/
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyPatientContentProvider.CONTENT_ITEM_TYPE, todoUri);
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

        if (todoUri == null) {
            // New patient
            todoUri = getContentResolver().insert(MyPatientContentProvider.CONTENT_URI, values);
        } else {
            // Update todo - NOT IMPLEMENTED as of 5-26-2014 7:20pm, although probably no reason not to with longpress.
            getContentResolver().update(todoUri, values, null, null);
        }
    }

    private void makeToast() {
        Toast.makeText(PatientActivity.this, "Please provide a name or identifying field like initials+city",
                Toast.LENGTH_LONG).show();
    }
}