package com.sam.team.character.design;

/**
 * Dialogs are constructed based on text parameters.
 * Created by pborisenko on 11/26/2016.
 */

class TextParameter {
    private static final String TAG = "TextParameter";

    private String dfltValue;
    private String currentValue;
    private Boolean mandatory;
    private String[] dropDown;

    TextParameter(String dfltValue, String currentValue, Boolean mandatory, String[] dropDown) {
        this.currentValue = currentValue;
        this.mandatory = mandatory;
        this.dfltValue = dfltValue;
        this.dropDown = dropDown;

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

    String[] getDropDown() {
        return dropDown;
    }
}