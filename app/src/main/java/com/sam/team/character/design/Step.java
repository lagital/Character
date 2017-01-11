package com.sam.team.character.design;

import android.graphics.drawable.TransitionDrawable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

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

    Step(View container) {
        this.container = container;
        toControl = new ArrayList<>();

        if ( container.getBackground() instanceof TransitionDrawable) {
            transition = (TransitionDrawable) container.getBackground();
        }
    }

    void enable() {
        Log.d(TAG, "enable");
        container.setVisibility(View.VISIBLE);
        validate();
    }

    void disable() {
        Log.d(TAG, "disable");
        for (Step s : toControl) {
            s.disable();
        }
        container.setVisibility(View.GONE);
    }

    /*
    * method to Override for each step specifically
    */
    boolean prevalidate() {
        Log.d(TAG, "validate");
        return true;
    }

    void validate() {
        Log.d(TAG, "validate");
        boolean result = prevalidate();
        setValidUI(result);
        valid = result;
        if (result) {
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
        if (valid == v) {
            return;
        }
        if (v) {
            transition.startTransition(transitionTime);
        } else {
            transition.reverseTransition(transitionTime);
        }
    }

    Boolean isValid () {
        return valid;
    }

    void addControlChildren (Step[] children) {
        Log.d(TAG, "addControlChildren");
        for (Step s : Arrays.asList(children)) {
            this.toControl.add(s);
        }
    }

    static void setTransitionTime(Integer transitionTime) {
        Step.transitionTime = transitionTime;
    }
}
