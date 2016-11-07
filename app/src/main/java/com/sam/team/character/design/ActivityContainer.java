package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.sam.team.character.R;

/**
 * Created by pborisenko on 10/27/2016.
 */

public class ActivityContainer extends AppCompatActivity {

    private static final String TAG = "ActivityContainer";

    FrameLayout container;
    FragmentManager mFragmentManager;
    FragmentSystemPicker mFragmentSystemPicker;
    FragmentEditElement mFragmentEditElement;
    FragmentEditField mFragmentEditField;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        //Listen for changes in the back stack
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                shouldDisplayHomeUp();
            }
        });
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

        container = (FrameLayout) findViewById(R.id.container);

        mFragmentManager = getSupportFragmentManager();
        mFragmentSystemPicker = new FragmentSystemPicker();
        mFragmentEditElement = new FragmentEditElement();
        mFragmentEditField = new FragmentEditField();

        if (savedInstanceState == null) {
            // on first run
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            // add to container
            fragmentTransaction.add(R.id.container, mFragmentSystemPicker);
            fragmentTransaction.commit();
        }
    }

    void replaceFragment (FragmentType toFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();

        if (toFragment != null) {
            switch (toFragment) {
                case SYSTEM_PICKER: {
                    fragmentTransaction.replace(R.id.container,
                            mFragmentSystemPicker);
                    break;
                }
                case EDIT_ELEMENT: {
                    fragmentTransaction.replace(R.id.container,
                            mFragmentEditElement);
                    break;
                }
                case EDIT_FIELD: {
                    fragmentTransaction.replace(R.id.container,
                            mFragmentEditField);
                    break;
                }
            }

            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }

    public enum FragmentType {
        SYSTEM_PICKER,
        EDIT_ELEMENT,
        EDIT_FIELD
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 1){
            mFragmentManager.popBackStackImmediate();
            mFragmentManager.beginTransaction().commit();
        } else {
            super.onBackPressed();
        }
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = getSupportFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getSupportFragmentManager().popBackStack();
        return true;
    }
}
