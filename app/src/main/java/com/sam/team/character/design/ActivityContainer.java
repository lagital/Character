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
    FragmentEditSheet mFragmentEditSheet;
    FragmentEditField mFragmentEditField;
    FragmentHelp mFragmentHelp;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        Log.d(TAG, "onCreate");

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        container = (FrameLayout) findViewById(R.id.container);

        mFragmentManager = getSupportFragmentManager();

        //Listen for changes in the back stack
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                shouldDisplayHomeUp();
            }
        });
        //Handle when activity is recreated like on orientation Change
        shouldDisplayHomeUp();

        mFragmentSystemPicker = new FragmentSystemPicker();
        mFragmentEditSheet = new FragmentEditSheet();

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
                            mFragmentEditSheet);
                    break;
                }
                case EDIT_FIELD: {
                    // recreate because normally this fragment one-time-used
                    mFragmentEditField = new FragmentEditField();
                    fragmentTransaction.replace(R.id.container,
                            mFragmentEditField);
                    break;
                }
                case HELP: {
                    // recreate because normally this fragment one-time-used
                    mFragmentHelp = new FragmentHelp();
                    fragmentTransaction.replace(R.id.container,
                            mFragmentHelp);
                    break;
                }
            }

            Log.d(TAG, "replaceFragment to " + toFragment.name());
            fragmentTransaction.addToBackStack(toFragment.name());
            fragmentTransaction.commit();
        }
    }

    public enum FragmentType {
        SYSTEM_PICKER,
        EDIT_ELEMENT,
        EDIT_FIELD,
        HELP
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 1){
            mFragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canback = mFragmentManager.getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canback);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        mFragmentManager.popBackStack();
        return true;
    }
}
