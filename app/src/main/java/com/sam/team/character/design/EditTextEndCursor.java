package com.sam.team.character.design;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.TintContextWrapper;
import android.util.AttributeSet;

/**
 * EditText view with default cursor at the end of the string
 * Created by pborisenko on 1/10/2017.
 */

public class EditTextEndCursor extends AppCompatEditText {

    public EditTextEndCursor(Context context) {
        this(context, null);
    }

    public EditTextEndCursor(Context context, AttributeSet attrs) {
        this(context, attrs, android.support.v7.appcompat.R.attr.editTextStyle);
    }

    public EditTextEndCursor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(TintContextWrapper.wrap(context), attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        // put cursor at the end of the string by default
        this.setSelection(getText().length());
    }
}
