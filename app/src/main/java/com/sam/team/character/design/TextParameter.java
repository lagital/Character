package com.sam.team.character.design;

import static com.sam.team.character.design.TextParameter.TextParmMode.DROP_DOWN;
import static com.sam.team.character.design.TextParameter.TextParmMode.LIST;

/**
 * Dialogs are constructed based on text parameters.
 * Created by pborisenko on 11/26/2016.
 */

class TextParameter {
    private static final String TAG = "TextParameter";

    private String dfltValue;
    private String currentValue;
    private Boolean mandatory = false;
    private String[] valueList;

    private TextParmMode mode;

    TextParameter(String dfltValue, String currentValue, Boolean mandatory,
                  TextParmMode mode, String[] valueList) {
        this.currentValue = currentValue;
        this.mandatory = mandatory;
        this.dfltValue = dfltValue;
        this.valueList = valueList;
        this.mode = mode;

        if ((mode == DROP_DOWN || mode == LIST) && valueList == null) {
            throw new UnsupportedOperationException();
        }

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

    String[] getValueList() {
        return valueList;
    }

    TextParmMode getMode() {
        return mode;
    }

    enum TextParmMode {
        LIST,
        DROP_DOWN,
        SINGLE
    }
}