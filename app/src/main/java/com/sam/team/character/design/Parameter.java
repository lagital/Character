package com.sam.team.character.design;

import android.content.Context;

/**
 * Created by pborisenko on 11/26/2016.
 */

public class Parameter {
    private static final String TAG = "Parameter";

    private String dfltValue;
    private String currentValue;
    private Boolean mandatory;
    private Context context;

    Parameter (Context context, int dfltValueRes, String currentValue, Boolean mandatory) {
        this.context = context;
        dfltValue = context.getResources().getString(dfltValueRes);
        this.currentValue = currentValue;
        this.mandatory = mandatory;
    }

    public String getCurrentValue() {
        return currentValue;
    }
}
