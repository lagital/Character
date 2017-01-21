package com.sam.team.character.design;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sam.team.character.R;
import com.sam.team.character.corev2.SB_Field;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelField;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Parent class for mutable activities that are dialogs on large screen devices.
 * Created by pborisenko on 1/14/2017.
 */

public class ActivityFieldPicker extends AppCompatActivity {

    private static final String TAG = "ActivityFieldPicker";

    static final String RESULT_FIELD = "RESULT_FIELD";
    static final String EXTRA_FILTER = "EXTRA_FILTER";
    static final int    REQUEST_CODE = 1;

    private ArrayList<SB_Field.FieldType> typeFilter = new ArrayList<>();

    FragmentManager mFragmentManager;
    private ViewModelElementType element = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_picker);
        Log.d(TAG, "onCreate");

        // by default activity allows to choose any field, but you can override the filter
        for (SB_Field.FieldType t : Arrays.asList(SB_Field.FieldType.values())) {
            typeFilter.add(t);
        }

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_FILTER)) {
            typeFilter.clear();
            for (String s : Arrays.asList(intent.getStringArrayExtra(EXTRA_FILTER))) {
                typeFilter.add(SB_Field.FieldType.valueOf(s));
            }
        }

        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            // on first run
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            // add to container
            fragmentTransaction.add(R.id.container, new FragmentFieldPicker());
            fragmentTransaction.commit();
        }
    }

    void returnLinkOrMention(ViewModelField field) {
        Intent intent = new Intent();
        intent.putExtra(RESULT_FIELD, field.getPath());
        setResult(RESULT_OK, intent);
        finish();
    }

    void nextStep (ViewModelElementType element) {
        this.element = element;

        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();
        fragmentTransaction.replace(R.id.container, new FragmentFieldPicker());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public ViewModelElementType getElement() {
        return element;
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 1){
            mFragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        mFragmentManager.popBackStack();
        return true;
    }
}