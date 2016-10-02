package com.sam.team.character.design;

import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel2.RPSystem;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class ActivitySystemPicker extends AppCompatActivity {
    private static final String TAG = "ActivitySystemPicker";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private FloatingActionButton mAddMiniFAB;
    private FloatingActionButton mLoadMiniFAB;
    private CoordinatorLayout.LayoutParams addLayoutParams;
    private CoordinatorLayout.LayoutParams loadLayoutParams;
    private Toolbar mToolbar;

    Animation miniFABShow;
    Animation miniFABHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_picker);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.systems_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        miniFABShow = AnimationUtils.loadAnimation(this, R.anim.mini_fab_show);
        miniFABHide = AnimationUtils.loadAnimation(this, R.anim.mini_fab_hide);

        mMainFAB = (FloatingActionButton) findViewById(R.id.fab_main);
        mAddMiniFAB = (FloatingActionButton) findViewById(R.id.fab_add);
        mLoadMiniFAB = (FloatingActionButton) findViewById(R.id.fab_load);

        addLayoutParams = (CoordinatorLayout.LayoutParams) mAddMiniFAB.getLayoutParams();
        loadLayoutParams = (CoordinatorLayout.LayoutParams) mLoadMiniFAB.getLayoutParams();

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    animateMiniFAB(mAddMiniFAB, mAddMiniFAB.isClickable(), addLayoutParams, 0.99, 0);
                    animateMiniFAB(mLoadMiniFAB, mLoadMiniFAB.isClickable(), loadLayoutParams, 0.7, 0.7);
            }
        });

        mAddMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout l = (LinearLayout) View.inflate(ActivitySystemPicker.this,
                        R.layout.dialog_new_system, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySystemPicker.this);
                builder.setView(l);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = (EditText) l.findViewById(R.id.name);
                        EditText version = (EditText) l.findViewById(R.id.version);
                        String e_name = name.getText().toString();
                        String e_version = version.getText().toString();
                        if (!e_name.isEmpty()) {
                            RPSystem rps = new RPSystem(e_name, e_version);
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        mLoadMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: system import
            }
        });

        ArrayList<RPSystem> rpsl = new ArrayList<>();

        /*debug*/
        if (BuildConfig.DEBUG) {
            for (int i = 0; i < 15; i++) {
                rpsl.add(new RPSystem("TEST", Integer.toString(i)));
            }
        }
        /*debug*/

        mAdapter = new AdapterRPSystem(this, rpsl);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void animateMiniFAB(FloatingActionButton miniFAB, Boolean hide,
                                CoordinatorLayout.LayoutParams params, double posWidth, double posHeight) {
        miniFAB.startAnimation(hide ? miniFABHide : miniFABShow);
        miniFAB.setClickable(!hide);
    }
}