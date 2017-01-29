package com.sam.team.character.design;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ListView;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.DrawerItem;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sam.team.character.design.ActivityContainer.FragmentType.CHARACTER;
import static com.sam.team.character.design.ActivityContainer.FragmentType.LOAD_SYSTEMS;
import static com.sam.team.character.design.ActivityContainer.FragmentType.SYSTEM_PICKER;

/**
 * Main Activity is container for fragments
 * Created by pborisenko on 10/27/2016.
 */

public class ActivityContainer extends AppCompatActivity {

    private static final String TAG = "ActivityContainer";

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.drawer) ListView mDrawerList;
    @BindView(R.id.appbar) AppBarLayout mAppBar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsing;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    boolean isDrawerEnabled = true;
    private FragmentType[] drawerCompatibleFragments = {CHARACTER};

    FragmentManager mFragmentManager;
    FragmentSystemPicker mFragmentSystemPicker;
    FragmentEditElement mFragmentEditElement;

    private AdapterCharacterDrawer mAdapterCharacterDrawer;

    private AppBarLayout.LayoutParams mToolbarLayoutParams;
    private FragmentType currentFragmentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        ButterKnife.bind(this);
        Log.d(TAG, "onCreate");

        setSupportActionBar(mToolbar);

        mCollapsing.setTitleEnabled(false);
        mToolbarLayoutParams = (AppBarLayout.LayoutParams) mCollapsing.getLayoutParams();

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

        mAdapterCharacterDrawer = new AdapterCharacterDrawer(
                this, R.layout.drawer_item_page, new DrawerItem[] {});

        // Set the adapter for the list view
        mDrawerList.setAdapter(mAdapterCharacterDrawer);

        //Listen for changes in the back stack
        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                Log.d(TAG, "onBackStackChanged");
                currentFragmentType = getTopFragmentType();
                shouldDisplayHomeUp();
                shouldParallax();
                if (Arrays.asList(drawerCompatibleFragments).contains(currentFragmentType)) {
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
            fragmentTransaction.add(R.id.container, new FragmentLoadSystems(),LOAD_SYSTEMS.name());
            fragmentTransaction.commit();
            setDrawerEnabled(false);
            currentFragmentType = LOAD_SYSTEMS;
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
            currentFragmentType = getTopFragmentType();
            shouldDisplayHomeUp();
            shouldParallax();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only if there are entries in the back stack
        Log.d(TAG, "shouldDisplayHomeUp");
        if (mFragmentManager.getBackStackEntryCount() > 0
                && currentFragmentType != SYSTEM_PICKER
                && currentFragmentType != LOAD_SYSTEMS) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public void shouldParallax(){
        //Enable parallax scrolling only if it is character screen
        Log.d(TAG, "shouldParallax");
        if (currentFragmentType == CHARACTER) {
            mAppBar.setExpanded(true, true);
            mToolbarLayoutParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        } else {
            mAppBar.setExpanded(false, true);
        }
        mCollapsing.setLayoutParams(mToolbarLayoutParams);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        if (Arrays.asList(drawerCompatibleFragments).contains(currentFragmentType)) {
            closeOpenDrawer();
            return false;
        } else {
            mFragmentManager.popBackStack();
            return true;
        }
    }

    /*
     * Unlock navigation drawer for Character fragment
    **/
    void setDrawerEnabled(boolean enabled) {
        Log.d(TAG, "setDrawerEnabled");
        if (isDrawerEnabled != enabled) {
            int lockMode = enabled ? DrawerLayout.LOCK_MODE_UNLOCKED :
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
            mDrawerLayout.setDrawerLockMode(lockMode);
            mDrawerToggle.setDrawerIndicatorEnabled(enabled);
            mDrawerToggle.syncState();
            isDrawerEnabled = enabled;
        }
        if (enabled) {
            mAdapterCharacterDrawer.renewItems();
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