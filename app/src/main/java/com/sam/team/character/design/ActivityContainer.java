package com.sam.team.character.design;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.sam.team.character.R;

import java.util.Arrays;

import static com.sam.team.character.design.ActivityContainer.FragmentType.CHARACTER;

/**
 * Main Activity is container for fragments
 * Created by pborisenko on 10/27/2016.
 */

public class ActivityContainer extends AppCompatActivity {

    private static final String TAG = "ActivityContainer";

    FrameLayout container;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private boolean isDrawerEnabled = true;
    private FragmentType[] drawerCompatibleFragments = {CHARACTER};

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

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_item, new String[] {"1", "2"}));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mFragmentManager = getSupportFragmentManager();

        //Listen for changes in the back stack
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged");
                shouldDisplayHomeUp();
                if (Arrays.asList(drawerCompatibleFragments).contains(getTopFragmentType())) {
                    setDrawerEnabled(true);
                } else {
                    setDrawerEnabled(false);
                }
            }
        });

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
                getFragmentToShow(toFragment));

        Log.d(TAG, "replaceFragment to " + toFragment.name());
        fragmentTransaction.addToBackStack(toFragment.name());
        fragmentTransaction.commit();
    }

    Fragment getFragmentToShow(FragmentType type) {
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
            case CHARACTER: {
                return new FragmentCharacter();
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
        CHARACTER,
        HELP
    }

    @Override
    public void onBackPressed() {
        if (mFragmentManager.getBackStackEntryCount() > 0){
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(mFragmentManager.getBackStackEntryCount() > 0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        if (Arrays.asList(drawerCompatibleFragments).contains(getTopFragmentType())) {
            closeOpenDrawer();
        } else {
            mFragmentManager.popBackStack();
        }
        return true;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(TAG, "Item clicked");
        }
    }

    /*
     * Unlock navigation drawer for Character fragment
    **/
    void setDrawerEnabled(boolean enabled) {
        Log.d(TAG, "setDrawerEnabled - " + Boolean.toString(enabled));
        if (isDrawerEnabled != enabled) {
            int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
            mDrawerLayout.setDrawerLockMode(lockMode);
            mDrawerToggle.setDrawerIndicatorEnabled(enabled);
            mDrawerToggle.syncState();
            isDrawerEnabled = enabled;
        }
    }

    private FragmentType getTopFragmentType() {
        int count = mFragmentManager.getBackStackEntryCount() - 1;
        if (count != -1) {
            FragmentManager.BackStackEntry tmp = mFragmentManager.getBackStackEntryAt(count);
            return FragmentType.valueOf(tmp.getName());
        }
        return null;
    }

    private void closeOpenDrawer() {
        Log.d(TAG, "closeOpenDrawer");
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            mDrawerLayout.openDrawer(Gravity.LEFT);
        }
    }
}
