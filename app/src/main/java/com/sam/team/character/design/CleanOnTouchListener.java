package com.sam.team.character.design;

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
    private String  dfltText;

    public CleanOnTouchListener (EditText editText, String dfltText) {
        this.editText = editText;
        this.dfltText = dfltText;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (editText.getText().toString().equals(dfltText) || dfltText == null) {
            editText.setText("");
            editText.setTextColor(ContextCompat.
                    getColor(editText.getContext(), R.color.colorPrimaryText));
            Log.d(TAG, "Value replaced");
        }

        return false;
    }

    public static Boolean isValidString (Context c, String s, Integer dfltTextRes) {
        Log.d(TAG, "isValidString");
        return !(s.equals(c.getResources().getString(dfltTextRes)) || s.equals(""));
    }
}
