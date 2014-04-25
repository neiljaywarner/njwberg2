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
  private Uri todoUri;



  
  //TODO: Refactor to 'default projection' under table class
  private void fillData(Uri uri) {

      String[] projection = AssessmentTable.DEFAULT_PROJECTION;

    Cursor cursor = getContentResolver().query(uri, projection, null, null,null);
    int col=0;
    if (cursor != null) {
        cursor.moveToFirst();
        //TODO: SWitch to below
       // while (cursor.moveToNext()) {

        mTitleText.setText(cursor.getString(cursor
                .getColumnIndexOrThrow(AssessmentTable.COLUMN_SUMMARY)));
        String date = cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_DATE));
        mEditTextDate.setText(date);
        fillAnswerEditFields(mEditBoxes, cursor);



        // always close the cursor
      cursor.close();
    }
  }

    private void fillAnswerEditFields(ArrayList<EditText> editBoxes, Cursor cursor) {
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
    String title = mTitleText.getText().toString();
    
    if ( title.length() == 0) {
      return;
    }

    ContentValues values = new ContentValues();
    values.put(AssessmentTable.COLUMN_SUMMARY, title);
    values.put(AssessmentTable.COLUMN_Q1, mEditBoxes.get(0).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q2, mEditBoxes.get(1).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q3, mEditBoxes.get(2).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q4, mEditBoxes.get(3).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q5, mEditBoxes.get(4).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q6, mEditBoxes.get(5).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q7, mEditBoxes.get(6).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q8, mEditBoxes.get(7).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q9, mEditBoxes.get(8).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q10, mEditBoxes.get(9).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q11, mEditBoxes.get(10).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q12, mEditBoxes.get(11).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q13, mEditBoxes.get(12).getText().toString().trim());
    values.put(AssessmentTable.COLUMN_Q14, mEditBoxes.get(13).getText().toString().trim());


    if (todoUri == null) {
      // New todo
      todoUri = getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
    } else {
      // Update todo
      getContentResolver().update(todoUri, values, null, null);
    }
  }

  

  ArrayList<EditText> mEditBoxes;
  ArrayList<ImageButton> instructionButtons;
  ArrayList<String> mInstructions;
//icon from http://www.flaticon.com/free-icon/falling-man-silhouette_11015

  TextView tvTotal;

  @Override
  protected void onCreate(Bundle bundle) {
      super.onCreate(bundle);
      setContentView(R.layout.activity_main);
      mTitleText = (EditText) findViewById(R.id.todo_edit_summary);

      ImageButton buttonCalculate = (ImageButton) findViewById(R.id.buttonCalculate);
      ImageButton buttonClear = (ImageButton) findViewById(R.id.buttonClear);
      tvTotal = (TextView) findViewById(R.id.textViewTotal);
      initializeEditBoxes();
      initializeInstructionButtons();
      initializeInstructionStrings();
      //TODO: Remove calculate button, move clear button to action bar?

      buttonClear.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(AssessmentActivity.this.getApplicationContext(), "Clear", Toast.LENGTH_LONG).show();
              for (EditText editText : mEditBoxes) {
                  editText.setText("");
              }
              tvTotal.setText("");
          }
      });

      Bundle extras = getIntent().getExtras();

      // check from the saved Instance
      todoUri = (bundle == null) ? null : (Uri) bundle
          .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

      // Or passed from the other activity
      if (extras != null) {
        todoUri = extras
            .getParcelable(MyContentProvider.CONTENT_ITEM_TYPE);

        fillData(todoUri);
      }

      calculateTotalIfAllFilledIn();

  }

  private void initializeInstructionStrings() {
      mInstructions = new ArrayList<String>();
      int numButtons = instructionButtons.size();
      for (int i = 0; i < numButtons; i++) {
          mInstructions.add(this.getResources().getStringArray(R.array.instructions)[i]);

      }

  }

  private void initializeEditBoxes() {
	  	//TODO: Blank 2nd column, autofill goal column
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
      
      for ( int i=0; i < mEditBoxes.size(); i++) {
    	  final int next = i+1;
    	  final EditText e = mEditBoxes.get(i);
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
					if (next < mEditBoxes.size()) {
						EditText nextEditBox = mEditBoxes.get(next);
						nextEditBox.requestFocus();
					}
					//autocalculate

					calculateTotalIfAllFilledIn();
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
      protected void calculateTotalIfAllFilledIn() {
	
    	  int total = 0;
          int score = 0;
          try {
              for (EditText editBox : mEditBoxes) {
                  score = Integer.parseInt(editBox.getText().toString());
                  if (score > 4) {
                      throw (new Exception()); //TODO: Detail which one via tag in xml
                  }
                  total += score;
              }
              Toast.makeText(AssessmentActivity.this.getApplicationContext(), "Total Score=" + total, Toast.LENGTH_LONG).show();
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
