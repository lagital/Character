package com.sam.team.character.design;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.FrameLayout;

import com.sam.team.character.R;

/**
 * Main Activity is container for fragments
 * Created by pborisenko on 10/27/2016.
 */

public class ActivityContainer extends AppCompatActivity {

    private static final String TAG = "ActivityContainer";

    FrameLayout container;
    FragmentManager mFragmentManager;
    FragmentSystemPicker mFragmentSystemPicker;
    FragmentEditElement mFragmentEditElement;

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
        mFragmentEditElement = new FragmentEditElement();

        if (savedInstanceState == null) {
            // on first run
            FragmentTransaction fragmentTransaction = mFragmentManager
                    .beginTransaction();
            // add to container
            fragmentTransaction.add(R.id.container, new FragmentLoadSystems());
            fragmentTransaction.commit();
        }
    }

    void replaceFragment (FragmentType toFragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager
                .beginTransaction();

        fragmentTransaction.replace(R.id.container,
                getFragmentByType(toFragment));

        Log.d(TAG, "replaceFragment to " + toFragment.name());
        fragmentTransaction.addToBackStack(toFragment.name());
        fragmentTransaction.commit();
    }

    Fragment getFragmentByType(FragmentType type) {
        switch (type) {
            case LOAD_SYSTEMS: {
                return new FragmentLoadSystems();
            }
            case SYSTEM_PICKER: {
                return mFragmentSystemPicker;
            }
            case EDIT_ELEMENT: {
                return mFragmentEditElement;
            }
            case EDIT_FIELD: {
                return new FragmentEditField();
            }
            case HELP: {
                return new FragmentHelp();
            }
            default: {
                return new FragmentLoadSystems();
            }

        }
    }

    public enum FragmentType {
        LOAD_SYSTEMS,
        SYSTEM_PICKER,
        EDIT_ELEMENT,
        EDIT_FIELD,
        HELP
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 1){
            mFragmentManager.popBackStack();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only if there are entries in the back stack
        Log.d(TAG, "shouldDisplayHomeUp - " + Integer.toString(mFragmentManager.getBackStackEntryCount()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(mFragmentManager.getBackStackEntryCount() > 1);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        mFragmentManager.popBackStack();
        return true;
    }
}
