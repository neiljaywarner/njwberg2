package com.spiritflightapps.berg;

import java.util.ArrayList;
import java.util.HashMap;

import com.spiritflightapps.berg.contentprovider.AssessmentContentProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class AssessmentActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor> {
    private EditText mTitleText;
    private EditText mEditTextDate;
    private EditText mEditTextDate2;
    private TextView mTextViewCurrentVisitId, mTextViewPreviousVisitId;
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_PATIENT_ID = "patient_id";

    public static Intent newIntent(Context context, String name, String patientId) {
        Intent i = new Intent(context, AssessmentActivity.class);
        i.putExtra(EXTRA_NAME, name);
        i.putExtra(EXTRA_PATIENT_ID, patientId);
        return i;
    }

    /**
     *
     * Basic idea:
     * Enter page with patientId, if there are unfinished tests, unfinished one is on the left
     * else new one on the left.  oncreate, initloader, onFinished.
     */
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        mTitleText = (EditText) findViewById(R.id.todo_edit_summary);


        tvTotal = (EditText) findViewById(R.id.textViewTotal);
        tvTotal2 = (EditText) findViewById(R.id.textViewTotalV2);

        mEditTextDate = (EditText) findViewById(R.id.editTextDateV1);
        mEditTextDate2 = (EditText) findViewById(R.id.editTextDateV2);

        mTextViewCurrentVisitId = (TextView) findViewById(R.id.textViewCurrentVisitId);
        mTextViewPreviousVisitId = (TextView) findViewById(R.id.textViewOtherVisitId);

        initializeEditBoxes();
        initializeInstructionButtons();
        initializeInstructionStrings();

        Bundle extras = getIntent().getExtras();


        mPatientId = extras.getString("patient_id");

        // Or passed from the other activity
        if (extras != null) {


            String name = extras.getString("name");
            mTitleText.setText(name);
            getLoaderManager().initLoader(0, null, this);

        }

        calculateTotalIfAllFilledIn(mNewEntryEditBoxes, tvTotal);
        calculateTotalIfAllFilledIn(mPreviousEntryEditBoxes, tvTotal2);


    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAssessments = new HashMap<String, ArrayList<String>>();
        mDates = new ArrayList<String>();
        mVisitIds = new ArrayList<Integer>();
        String date="";
        while (cursor.moveToNext()) {
            ArrayList<String> assessment = new ArrayList<String>();
//TODO: Fix -> brittle - skips 0 and 1 b/c id and date, id is at the end.
            for (int i=2; i < cursor.getColumnCount()-1; i++) {
                assessment.add(cursor.getString(i));
            }
            date = cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_DATE));
            mDates.add(date);
            mAssessments.put(date,assessment);
            String visitId=cursor.getString(0);
            Log.i("NJW","***field0="+visitId);
            mVisitIds.add(Integer.valueOf(visitId)); //in this case 0 is id
        }
        Log.i("NJW", "loaded" + mAssessments.size());
        mCurrentDateIndex = mDates.size() - 1;
        fillAssessmentColumn(date);
        setupEditBoxes(mNewEntryEditBoxes, tvTotal, mEditTextDate);
        setupEditBoxes(mPreviousEntryEditBoxes, tvTotal2, mEditTextDate2);
        //NOTE: if it's not all filled in, make it be current column on the left.
    }


    private void fillAnswerEditFields(ArrayList<EditText> editBoxes, EditText editTextDate, String date, ArrayList<String> answers) {
        editTextDate.setText(date);
        for (int i=0;i < answers.size() ; i++) {
            editBoxes.get(i).setText(answers.get(i));
        }
    }







    private void saveColumn(String visitId, ArrayList<EditText> editBoxes, EditText editTextDate) {
        if (visitId.trim().length()==0) {
            insertColumn(editBoxes, editTextDate);
        } else {
            int intVisitId = Integer.parseInt(visitId);
            updateColumn(intVisitId, editBoxes, editTextDate);

        }
    }

    private void updateColumn(int visitId, ArrayList<EditText> editBoxes, EditText editTextDate) {
        Log.i("NJW", "trying to update column");
        String title = mTitleText.getText().toString(); //TODO: Maybe allow them to save title here! (eventually..)
        String date = editTextDate.getText().toString();
        if ( title.length() == 0) {
            return;
        }
        //TODO: Make title so   it cannot be saved in the middle, etc
        ContentValues values = new ContentValues();
        values.put(AssessmentTable.COLUMN_PATIENT_ID, mPatientId);
        values.put(AssessmentTable.COLUMN_DATE, date);
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



        // Update berg test
        Log.i("NJW", "about to update assessment");
        Uri uri = Uri.parse(AssessmentContentProvider.CONTENT_URI + "/" + visitId);

        getContentResolver().update(uri, values, null, null);

    }




    private void insertColumn(ArrayList<EditText> editBoxes, EditText editTextDate) {
        Log.i("NJW", "trying to save/insert column");
        String title = mTitleText.getText().toString(); //TODO: Maybe allow them to save title here! (eventually..)
        String date = editTextDate.getText().toString();
        if ( title.length() == 0) {
            return;
        }
        //TODO: Make title so   it cannot be saved in the middle, etc
        ContentValues values = new ContentValues();
        values.put(AssessmentTable.COLUMN_PATIENT_ID, mPatientId);
        values.put(AssessmentTable.COLUMN_DATE, date);
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



        // New berg test
        Log.i("NJW", "about to insert assessment");
        getContentResolver().insert(AssessmentContentProvider.CONTENT_URI, values);

    }


    ArrayList<EditText> mNewEntryEditBoxes;
    ArrayList<EditText> mPreviousEntryEditBoxes;

    ArrayList<View> instructionButtons;
    ArrayList<String> mInstructions;
    //icon from http://www.flaticon.com/free-icon/falling-man-silhouette_11015
    private SimpleCursorAdapter adapter;

    TextView tvTotal,tvTotal2;
    String mPatientId;
    //TODO: Make this into a custom control...?


    // creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //TODO: Add a createdate field here to be displayed in the listview.
        String[] selectionArgs = {mPatientId};
        CursorLoader cursorLoader = new CursorLoader(this,
                AssessmentContentProvider.CONTENT_URI, AssessmentTable.LOADTEST_PROJECTION, "patient_id=?", selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_assessment, menu);
        return true;
    }

    // Reaction to the menu selection - next/previous
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.next:
                if (mCurrentDateIndex < mDates.size() - 1) {
                    mCurrentDateIndex = mCurrentDateIndex + 1;
                    fillAssessmentColumn(mDates.get(mCurrentDateIndex));
                    mIntPreviousVisitId = mVisitIds.get(mCurrentDateIndex);
                }
                return true;
            case R.id.previous:
                if (mCurrentDateIndex > 0) {
                    mCurrentDateIndex = mCurrentDateIndex - 1;
                    fillAssessmentColumn(mDates.get(mCurrentDateIndex));
                    mIntPreviousVisitId = mVisitIds.get(mCurrentDateIndex);

                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void fillAssessmentColumn(String date) {
        if (!TextUtils.isEmpty(date)) {
            //NOTE: In this method the left column is always new.

            ArrayList<String> answers = mAssessments.get(date);
            fillAnswerEditFields(mPreviousEntryEditBoxes, mEditTextDate2, date, answers);
            mEditTextDate.setText(""); //TODO: Autofill this with something reasonable like 2e if 2nd one, etc.
            mEditTextDate2.setTag(mIntPreviousVisitId);
            calculateTotalIfAllFilledIn(mPreviousEntryEditBoxes, tvTotal2);

        } else {
            ViewGroup viewGroup2 = (ViewGroup) findViewById(R.id.vgLastVisit2);
            viewGroup2.setVisibility(View.GONE); //don't show if empty
        }
    }



    HashMap<String,ArrayList<String>> mAssessments; //date then questions
    //TOOD: FIX< THIS IS HORRIBLE!  Give it honest to goodness classes!!!!
    // a visit class.
    ArrayList<String> mDates;
    ArrayList<Integer> mVisitIds;

    int mCurrentDateIndex;
    private int mIntPreviousVisitId; //the id of the visit on teh right, the non-current one.

    //TODO: Abstract some of this out into classes!!!

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // do nothing?
    }


    private void initializeInstructionStrings() {
        mInstructions = new ArrayList<String>();
        int numButtons = instructionButtons.size();
        for (int i = 0; i < numButtons; i++) {
            mInstructions.add(this.getResources().getStringArray(R.array.instructions)[i]);

        }

    }
    private void initializeEditBoxes() {
        mNewEntryEditBoxes = new ArrayList<EditText>();
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ1));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ2));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ3));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ4));

        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ5));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ6));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ7));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ8));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ9));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ10));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ11));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ12));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ13));
        mNewEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ14));

        mPreviousEntryEditBoxes = new ArrayList<EditText>();
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ1_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ2_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ3_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ4_V2));

        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ5_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ6_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ7_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ8_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ9_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ10_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ11_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ12_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ13_V2));
        mPreviousEntryEditBoxes.add((EditText) findViewById(R.id.editTextQ14_V2));

        //TODO: Refactor, either into sep GUI structure, sep control, arraylist of arraylists, something


    }
    private void setupEditBoxes(ArrayList<EditText> editBoxesColumn, TextView tvTotal, EditText edtDate) {
        //TODO: autofill goal column

        final ArrayList<EditText> editBoxes = editBoxesColumn;
        final TextView textViewTotal = tvTotal;
        final EditText editTextDate = edtDate;
        //TODO: Make final in parameters. OR figure out something smarter.

        for ( int i=0; i < editBoxes.size(); i++) {
            final int next = i+1;
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
                        Toast.makeText(AssessmentActivity.this.getApplicationContext(), error, Toast.LENGTH_LONG).show();

                    } else {
                        //autoadvance
                        if (next < editBoxes.size()) {
                            EditText nextEditBox = editBoxes.get(next);
                            nextEditBox.requestFocus();
                        }
                        //autocalculate

                        calculateTotalIfAllFilledIn(editBoxes, textViewTotal);
                        Object tag = editTextDate.getTag();
                        if (tag == null) {
                            insertColumn(editBoxes,editTextDate);
                        } else {
                            String visitId = editTextDate.getTag().toString(); //TODO: BETTER PLACE TO GET THIS!
                            updateColumn(Integer.parseInt(visitId), editBoxes, editTextDate);
                        }
                        //saveColumn(editTextDate.getTag(), editBoxes, editTextDate);
                            //remove save if not needed, cleanup..
                        //horrible spaghetti mess, have to keep track of what it all does and then fix.
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
    protected void calculateTotalIfAllFilledIn(ArrayList<EditText> editBoxes, TextView tvTotal) {

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
            tvTotal.setText(String.valueOf(total));

        } catch (Exception e) {
            //Validation done in each field. if blank or one missing, do nothing

        }

    }

    //would an action bar ? icon be better if in the field?
    //TODO: Find by tag for more readable code, or child or something?
    private void initializeInstructionButtons() {
        instructionButtons = new ArrayList<View>();
        instructionButtons.add((View) findViewById(R.id.buttonQ1));

        instructionButtons.add((View) findViewById(R.id.buttonQ2));
        instructionButtons.add((View) findViewById(R.id.buttonQ3));
        instructionButtons.add((View) findViewById(R.id.buttonQ4));

        instructionButtons.add((View) findViewById(R.id.buttonQ5));
        instructionButtons.add((View) findViewById(R.id.buttonQ6));
        instructionButtons.add((View) findViewById(R.id.buttonQ7));
        instructionButtons.add((View) findViewById(R.id.buttonQ8));
        instructionButtons.add((View) findViewById(R.id.buttonQ9));
        instructionButtons.add((View) findViewById(R.id.buttonQ10));
        instructionButtons.add((View) findViewById(R.id.buttonQ11));
        instructionButtons.add((View) findViewById(R.id.buttonQ12));
        instructionButtons.add((View) findViewById(R.id.buttonQ13));
        instructionButtons.add((View) findViewById(R.id.buttonQ14));



        for (int i = 0; i < instructionButtons.size(); i++) {
            final int buttonNum = i;
            final int questionNum = i + 1;
            instructionButtons.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: Use Fragments
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            view.getContext());

                    // set title
                    alertDialogBuilder.setTitle("Instructions for Question " + questionNum);

                    // set dialog message
                    alertDialogBuilder
                            .setMessage( mInstructions.get(buttonNum))
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    dialog.dismiss();;
                                }
                            })
                    ;

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }

            });
        }

    }




}
