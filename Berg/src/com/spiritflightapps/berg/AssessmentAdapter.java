package com.spiritflightapps.berg;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;

/**
 * Created by neil on 5/4/14.
 */
public class AssessmentAdapter extends CursorAdapter {

  // TODO: Our version of
  //  http://stackoverflow.com/questions/5300787/how-do-i-create-a-custom-cursor-adapter-for-a-listview-for-use-with-images-and-t
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    public AssessmentAdapter(Context context, Cursor c) {
        super(context, c);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
       // View v = mLayoutInflater.inflate(R.layout.items_row, parent, false);
            //NOTE: This shoudl be the thing with all 14 items and date in it...
        View v = null;
        return v;
    }

    @Override
    public void bindView(View v, Context context, Cursor c) {
       // String title = c.getString(c.getColumnIndexOrThrow(ItemDbAdapter.KEY_TITLE));

    }
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    // Override this method according to your need
    public View getView(int index, View view, ViewGroup viewGroup)
    {

        View root = view;

        return root;
    }
}
