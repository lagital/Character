package com.sam.team.character.viewmodel;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.sam.team.character.R;

/**
 * Created by pborisenko on 11/7/2016.
 */

public class CleanOnTouchListener implements View.OnTouchListener{

    private static final String TAG = "CleanOnTouchListener";

    private EditText editText;
    private Context context;

    public CleanOnTouchListener (Context context, EditText editText) {
        this.editText    = editText;
        this.context     = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (editText.getText().toString().equalsIgnoreCase(
                context.getResources().getString(R.string.new_element_dflt_name))) {
            editText.setText("");
            editText.setTextColor(ContextCompat.
                    getColor(context, R.color.colorPrimaryText));
            Log.d(TAG, "Value replaced");
        }

        return false;
    }
}
