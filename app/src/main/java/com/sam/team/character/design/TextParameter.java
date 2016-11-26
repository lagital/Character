package com.sam.team.character.design;

/**
 * Created by pborisenko on 11/26/2016.
 */

class TextParameter {
    private static final String TAG = "Parameter";

    private String dfltValue;
    private String currentValue;
    private Boolean mandatory;

    TextParameter(String dfltValue, String currentValue, Boolean mandatory) {
        this.currentValue = currentValue;
        this.mandatory = mandatory;
        this.dfltValue = dfltValue;

        if (this.currentValue == null || this.currentValue.equals("")) {
            this.currentValue = this.dfltValue;
        }
    }

    String getDfltValue () {
        return dfltValue;
    }

    String getCurrentValue() {
        return currentValue;
    }

    Boolean isMandatory () {
        return mandatory;
    }
}