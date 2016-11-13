package com.sam.team.character.design;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import java.util.ArrayList;

/**
 * Created by pborisenko on 11/12/2016.
 */

class Step {

    private static final String TAG = "Step";

    private CheckBox toCheck;
    private View container;
    private ArrayList<Step> toControl;
    private Boolean valid = false;

    Step (CheckBox toCheck, View container) {
        this.toCheck   = toCheck;
        this.container = container;
        toControl = new ArrayList<>();
    }

    void enable () {
        Log.d(TAG, "enable");
        container.setVisibility(View.VISIBLE);
    }

    void disable () {
        Log.d(TAG, "disable");
        setValid(false);
        container.setVisibility(View.GONE);
    }

    void setValid (Boolean v) {
        Log.d(TAG, "setValid - " + v.toString());
        if (v == valid) {
            return;
        }
        valid = v;
        if (v) {
            if (toCheck != null) {
                toCheck.setChecked(true);
            }
            for (Step s : toControl) {
                s.enable();
            }
        } else {
            if (toCheck != null) {
                toCheck.setChecked(false);
            }
            for (Step s : toControl) {
                s.disable();
            }
        }
    }

    Boolean isValid () {
        return valid;
    }

    void addControlChild (Step s) {
        Log.d(TAG, "addControlChild");
        this.toControl.add(s);
    }
}
