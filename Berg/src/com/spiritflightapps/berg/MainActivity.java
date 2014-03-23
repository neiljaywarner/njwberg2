package com.spiritflightapps.berg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
public class MainActivity extends Activity {

    ArrayList<EditText> editBoxes;
    ArrayList<ImageButton> instructionButtons;
    ArrayList<String> mInstructions;


    TextView tvTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    for (EditText editBox : editBoxes) {
                        score = Integer.parseInt(editBox.getText().toString());
                        if (score > 4) {
                            throw (new Exception()); //TODO: Detail which one via tag in xml
                        }
                        total += score;
                    }
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Total Score=" + total, Toast.LENGTH_LONG).show();
                    tvTotal.setText(String.valueOf(total));

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Please put a value  of 0-4 for every field", Toast.LENGTH_LONG).show();

                }
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "clear", Toast.LENGTH_LONG).show();
                for (EditText editText : editBoxes) {
                    editText.setText("");
                }
            }
        });



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
        editBoxes = new ArrayList<EditText>();
        editBoxes.add((EditText) findViewById(R.id.editTextQ1));
        editBoxes.add((EditText) findViewById(R.id.editTextQ2));
        editBoxes.add((EditText) findViewById(R.id.editTextQ3));
        editBoxes.add((EditText) findViewById(R.id.editTextQ4));

        editBoxes.add((EditText) findViewById(R.id.editTextQ5));
        editBoxes.add((EditText) findViewById(R.id.editTextQ6));
        editBoxes.add((EditText) findViewById(R.id.editTextQ7));
        editBoxes.add((EditText) findViewById(R.id.editTextQ8));
        editBoxes.add((EditText) findViewById(R.id.editTextQ9));
        editBoxes.add((EditText) findViewById(R.id.editTextQ10));
        editBoxes.add((EditText) findViewById(R.id.editTextQ11));
        editBoxes.add((EditText) findViewById(R.id.editTextQ12));
        editBoxes.add((EditText) findViewById(R.id.editTextQ13));
        editBoxes.add((EditText) findViewById(R.id.editTextQ14));

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
