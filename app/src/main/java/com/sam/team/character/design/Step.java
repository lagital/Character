package com.sam.team.character.design;

import android.graphics.Interpolator;
import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;

import com.sam.team.character.R;

import java.util.ArrayList;

/**
 * Created by pborisenko on 11/12/2016.
 */

class Step {

    private static final String TAG = "Step";

    private static Integer transitionTime;

    private View container;
    private ArrayList<Step> toControl;
    private Boolean valid = false;
    private TransitionDrawable transition;

    Step (View container) {
        this.container = container;
        toControl = new ArrayList<>();
        transition = (TransitionDrawable) container.getBackground();
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
        setValidUI(v);
        valid = v;
        if (v) {
            for (Step s : toControl) {
                s.enable();
            }
        } else {
            for (Step s : toControl) {
                s.disable();
            }
        }
    }

    void setValidUI (Boolean v) {
        if (v == valid) {
            return;
        }
        if (v) {
            transition.startTransition(250);
        } else {
            transition.reverseTransition(250);
        }
    }

    Boolean isValid () {
        return valid;
    }

    void addControlChild (Step s) {
        Log.d(TAG, "addControlChild");
        this.toControl.add(s);
    }

    public static Integer getTransitionTime() {
        return transitionTime;
    }

    public static void setTransitionTime(Integer transitionTime) {
        Step.transitionTime = transitionTime;
    }
}
