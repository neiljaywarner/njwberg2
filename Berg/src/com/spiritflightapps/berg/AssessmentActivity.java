package com.spiritflightapps.berg;

import java.util.ArrayList;

import com.spiritflightapps.berg.contentprovider.MyToDoContentProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/*
 * TodoDetailActivity allows to enter a new todo item 
 * or to change an existing
 */
public class AssessmentActivity extends Activity {
  private EditText mTitleText;

  private Uri todoUri;

//  @Override
//  protected void onCreate(Bundle bundle) {
//    super.onCreate(bundle);
//    setContentView(R.layout.edit);
//
//    mTitleText = (EditText) findViewById(R.id.todo_edit_summary);
//    mBodyText = (EditText) findViewById(R.id.todo_edit_description);
//    Button confirmButton = (Button) findViewById(R.id.todo_edit_button);
//
//    Bundle extras = getIntent().getExtras();
//
//    // check from the saved Instance
//    todoUri = (bundle == null) ? null : (Uri) bundle
//        .getParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE);
//
//    // Or passed from the other activity
//    if (extras != null) {
//      todoUri = extras
//          .getParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE);
//
//      fillData(todoUri);
//    }
//
//    confirmButton.setOnClickListener(new View.OnClickListener() {
//      public void onClick(View view) {
//        if (TextUtils.isEmpty(mTitleText.getText().toString())) {
//          makeToast();
//        } else {
//          setResult(RESULT_OK);
//          finish();
//        }
//      }
//
//    });
//  }

  private void fillData(Uri uri) {
    String[] projection = { TodoTable.COLUMN_SUMMARY,
        TodoTable.COLUMN_Q1 };
    Cursor cursor = getContentResolver().query(uri, projection, null, null,
        null);
    if (cursor != null) {
      cursor.moveToFirst();

      mTitleText.setText(cursor.getString(cursor
          .getColumnIndexOrThrow(TodoTable.COLUMN_SUMMARY)));
      int colIndexQ1=cursor.getColumnIndexOrThrow(TodoTable.COLUMN_Q1);
      for (int i=0; i < 2; i++) {
    	  try {
    		  int answer = cursor.getInt(colIndexQ1+i);
        	  mEditBoxes.get(i).setText(String.valueOf(answer));
		} catch (Exception e) {
			// TODO: handle exception
		}
    	 
      }
     // mBodyText.setText(String.valueOf(cursor.getInt(cursor
     //     .getColumnIndexOrThrow(TodoTable.COLUMN_Q1))));

      // always close the cursor
      cursor.close();
    }
  }

  protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    saveState();
    outState.putParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE, todoUri);
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
    values.put(TodoTable.COLUMN_SUMMARY, title);
    for (int i = 1; i <= 2; i++) {
    	//TODO: Refactor!  exceptions bad, halfway puling away from constants=bad.
    	try {
    		int ans = Integer.valueOf(mEditBoxes.get(0).getText().toString().trim());
    		//if blank of not an integer, exception, don't add.
			values.put("q"+i, ans);
		} catch (Exception e) {
			// do nothing, don't put in there.
		}
    }
    

    if (todoUri == null) {
      // New todo
      todoUri = getContentResolver().insert(MyToDoContentProvider.CONTENT_URI, values);
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
      buttonCalculate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
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
                  Toast.makeText(AssessmentActivity.this.getApplicationContext(), "Please put a value  of 0-4 for every field", Toast.LENGTH_LONG).show();

              }
          }
      });

      buttonClear.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Toast.makeText(AssessmentActivity.this.getApplicationContext(), "clear", Toast.LENGTH_LONG).show();
              for (EditText editText : mEditBoxes) {
                  editText.setText("");
              }
          }
      });

      Bundle extras = getIntent().getExtras();

      // check from the saved Instance
      todoUri = (bundle == null) ? null : (Uri) bundle
          .getParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE);

      // Or passed from the other activity
      if (extras != null) {
        todoUri = extras
            .getParcelable(MyToDoContentProvider.CONTENT_ITEM_TYPE);

        fillData(todoUri);
      }


  }

  private void initializeInstructionStrings() {
      mInstructions = new ArrayList<String>();
      int numButtons = instructionButtons.size();
      for (int i = 0; i < numButtons; i++) {
          mInstructions.add(this.getResources().getStringArray(R.array.instructions)[i]);

      }

  }

  private void initializeEditBoxes() {
      //TODO: autocalculate when all of them complete, autoclear when entered, etc.
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
          Log.i("NJW", "in loop of instruction buttons.");
          final int buttonNum = i;
          final int questionNum = i + 1;
          instructionButtons.get(i).setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Log.i("NJW","in onclick");
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
