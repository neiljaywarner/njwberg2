package com.spiritflightapps.berg.model;

import android.database.Cursor;

import com.spiritflightapps.berg.AssessmentTable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by neil on 6/16/14.
 */
public class Assessment {
    private String visitId,date,total;


    public ArrayList<String> answers;
    public Assessment(Cursor cursor) {
        date = cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_DATE));
        answers = new ArrayList<String>();
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q1)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q2)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q3)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q4)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q5)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q6)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q7)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q8)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q9)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q10)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q11)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q12)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q13)));
        answers.add(cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_Q14)));
        total = cursor.getString(cursor.getColumnIndex(AssessmentTable.COLUMN_TOTAL));

        visitId = cursor.getString(0);


    }

    public String getVisitId() {
        return visitId;
    }

    public String getDate() {
        return date;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
