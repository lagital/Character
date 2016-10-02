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
import android.widget.Toast;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel2.RPSystem;

import java.util.ArrayList;

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
                final EditText name = (EditText) l.findViewById(R.id.name);
                final EditText version = (EditText) l.findViewById(R.id.version);
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_name))) {
                            name.setText("");
                        }
                    }
                });

                version.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (name.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_version))) {
                            name.setText("");
                        }
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySystemPicker.this);
                builder.setView(l);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        EditText name = (EditText) l.findViewById(R.id.name);
                        EditText version = (EditText) l.findViewById(R.id.version);
                        String e_name = name.getText().toString();
                        String e_version = version.getText().toString();
                        RPSystem rps;
                        if (!e_name.isEmpty() && !e_name.equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_name))) {
                            if (e_version.isEmpty() || e_version.equalsIgnoreCase(
                                    getResources().getString(R.string.new_system_dflt_version))) {
                                rps = new RPSystem(e_name);
                            }
                            rps = new RPSystem(e_name, e_version);
                            //TODO: going to system creation
                        } else {
                            Toast.makeText(ActivitySystemPicker.this,
                                    getResources().getString(R.string.new_system_empty_name),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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