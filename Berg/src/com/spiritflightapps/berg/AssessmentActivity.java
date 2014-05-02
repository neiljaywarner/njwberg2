package com.spiritflightapps.berg;

import java.util.ArrayList;

import com.spiritflightapps.berg.contentprovider.MyContentProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class AssessmentActivity extends Activity {
  private EditText mTitleText;
  private EditText mEditTextDate;
  private EditText mEditTextDate2;

    private Uri todoUri;



  
  private void fillData(Uri uri) {

      String[] projection = AssessmentTable.DEFAULT_PROJECTION;

    Cursor cursor = getContentResolver().query(uri, projection, null, null,null);
    int col=0;
    if (cursor != null) {
        cursor.moveToFirst();
        //TODO: SWitch to below
       // while (cursor.moveToNext()) {

      //  mTitleText.setText(cursor.getString(cursor
       //         .getColumnIndexOrThrow(AssessmentTable.COLUMN_PATIENT_TITLE)));


        fillAnswerEditFields(mEditBoxes, mEditTextDate, cursor);

        cursor.moveToNext();
        if (!cursor.isAfterLast()) {
            fillAnswerEditFields(mEditBoxes2, mEditTextDate2, cursor);
        }



        // always close the cursor
        cursor.close();
    }
  }

    /**
     *
     * @param editBoxes fields with the questions
     * @param editTextDate - 2 digit date identified (1E for initial eval, 2E for second, etc
     * @param cursor
     */
    private void fillAnswerEditFields(ArrayList<EditText> editBoxes, EditText editTextDate, Cursor cursor) {
        editTextDate.setText(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_DATE)));
        editBoxes.get(0).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q1)));
        editBoxes.get(1).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q2)));
        editBoxes.get(2).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q3)));
        editBoxes.get(3).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q4)));
        editBoxes.get(4).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q5)));
        editBoxes.get(5).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q6)));
        editBoxes.get(6).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q7)));
        editBoxes.get(7).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q8)));
        editBoxes.get(8).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q9)));
        editBoxes.get(9).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q10)));
        editBoxes.get(10).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q11)));
        editBoxes.get(11).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q12)));
        editBoxes.get(12).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q13)));
        editBoxes.get(13).setText(cursor.getString(cursor.getColumnIndexOrThrow(AssessmentTable.COLUMN_Q14)));
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
        outState.putParcelable(MyContentProvider.CONTENT_ITEM_TYPE, todoUri);
    }

  @Override
  protected void onPause() {
    super.onPause();
    saveState();
  }

  private void saveState() {
      saveColumn(mEditBoxes, mEditTextDate);
  }


    //TODO: Gallery control only deprecated in Jellybean, will work indefinitely...

  private void saveColumn(ArrayList<EditText> editBoxes, EditText editTextDate) {
    Log.i("NJW", "trying to save");
    String title = mTitleText.getText().toString();
    String date = editTextDate.getText().toString();
    if ( title.length() == 0) {
      return;
    }
        //TODO: Make title so   it cannot be saved in the middle, etc
    ContentValues values = new ContentValues();
    values.put(AssessmentTable.COLUMN_PATIENT_TITLE, title);
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


    if (todoUri == null) {
      // New berg test
      todoUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
    } else {
      // Update berg test
        Log.i("NJW", "about to save");

        getContentResolver().update(todoUri, values, null, null);
    }
  }

  
            //array of arraylists for 4/N column prototype
  ArrayList<EditText> mEditBoxes;
  ArrayList<EditText> mEditBoxes2;

    ArrayList<ImageButton> instructionButtons;
  ArrayList<String> mInstructions;
//icon from http://www.flaticon.com/free-icon/falling-man-silhouette_11015

  TextView tvTotal,tvTotal2;
        //TODO: Make this into a custom control...
    // first make into a viewGroup for the 'row' with testEntries.

  @Override
  protected void onCreate(Bundle bundle) {
      super.onCreate(bundle);
      setContentView(R.layout.activity_main);
      mTitleText = (EditText) findViewById(R.id.todo_edit_summary);

      ImageButton buttonCalculate = (ImageButton) findViewById(R.id.buttonCalculate);
      ImageButton buttonClear = (ImageButton) findViewById(R.id.buttonClear);
      tvTotal = (TextView) findViewById(R.id.textViewTotal);
      tvTotal2 = (TextView) findViewById(R.id.textViewTotalV2);

      mEditTextDate = (EditText) findViewById(R.id.editTextDateV1);
      initializeEditBoxes();
      initializeInstructionButtons();
      initializeInstructionStrings();
      //TODO: Remove calculate button, move clear button to action bar (if needed, could clear current row...)
      //TODO: Make next and previous buttons.

      Bundle extras = getIntent().getExtras();

      // check from the saved Instance
      todoUri = (bundle == null) ? null : (Uri) bundle
          .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

      // Or passed from the other activity
      if (extras != null) {
        todoUri = extras
            .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

        String name = extras.getString("name");
        mTitleText.setText("***" + name);
        fillData(todoUri);
      }

      calculateTotalIfAllFilledIn(mEditBoxes, tvTotal);
      calculateTotalIfAllFilledIn(mEditBoxes2, tvTotal2);


  }

  private void initializeInstructionStrings() {
      mInstructions = new ArrayList<String>();
      int numButtons = instructionButtons.size();
      for (int i = 0; i < numButtons; i++) {
          mInstructions.add(this.getResources().getStringArray(R.array.instructions)[i]);

      }

  }
  private void initializeEditBoxes() {
      mEditBoxes = new ArrayList<EditText>();
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ1));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ2));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ3));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ4));

      mEditBoxes.add((EditText) findViewById(R.id.editTextQ5));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ6));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ7));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ8));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ9));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ10));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ11));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ12));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ13));
      mEditBoxes.add((EditText) findViewById(R.id.editTextQ14));

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

      //TODO: Refactor, either into sep GUI structure, sep control, arraylist of arraylists, something
      setupEditBoxes(mEditBoxes, tvTotal);

  }
  private void setupEditBoxes(ArrayList<EditText> editBoxesColumn, TextView tvTotal) {
	  	//TODO: autofill goal column

      final ArrayList<EditText> editBoxes = editBoxesColumn;
      final TextView textViewTotal = tvTotal;

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
      instructionButtons = new ArrayList<ImageButton>();
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ1));

      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ2));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ3));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ4));

      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ5));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ6));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ7));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ8));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ9));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ10));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ11));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ12));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ13));
      instructionButtons.add((ImageButton) findViewById(R.id.buttonQ14));



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
