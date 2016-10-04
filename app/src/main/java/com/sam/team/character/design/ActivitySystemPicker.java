package com.sam.team.character.design;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
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

    public static final String SYSTEM_VERSION_DFLT_EXTRA = "SYSTEM_VERSION_EXTRA";
    public static final String SYSTEM_PARCELABLE_EXTRA = "SYSTEM_PARCELABLE_EXTRA";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private FloatingActionButton mAddMiniFAB;
    private FloatingActionButton mLoadMiniFAB;
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

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    animateMiniFAB(mAddMiniFAB, mAddMiniFAB.isClickable());
                    animateMiniFAB(mLoadMiniFAB, mLoadMiniFAB.isClickable());
            }
        });

        mAddMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout l = (LinearLayout) View.inflate(ActivitySystemPicker.this,
                        R.layout.dialog_new_system, null);
                final EditText name = (EditText) l.findViewById(R.id.name);
                final EditText version = (EditText) l.findViewById(R.id.version);
                name.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (name.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_name))) {
                            name.setText("");
                            name.setTextColor(ContextCompat.
                                    getColor(ActivitySystemPicker.this, R.color.colorPrimary));
                            Log.d(TAG, "Fill system name");
                        }
                        return false;
                    }
                });

                version.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (version.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_version))) {
                            version.setText("");
                            version.setTextColor(ContextCompat.
                                    getColor(ActivitySystemPicker.this, R.color.colorPrimary));
                            Log.d(TAG, "Fill version");
                        }
                        return false;
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySystemPicker.this);
                builder.setView(l);
                builder.setTitle(R.string.new_system_dialog_title);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing, see dialog.getButton
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
                        if (!e_name.isEmpty() && !e_name.equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_name))) {

                            Intent intent = new Intent(ActivitySystemPicker.this, ActivityEditSystem.class);

                            if (e_version.isEmpty() || e_version.equalsIgnoreCase(
                                    getResources().getString(R.string.new_system_dflt_version))) {
                                intent.putExtra(SYSTEM_PARCELABLE_EXTRA,
                                        new RPSystem(e_name, SYSTEM_VERSION_DFLT_EXTRA));
                            } else {
                                intent.putExtra(SYSTEM_PARCELABLE_EXTRA,
                                        new RPSystem(e_name, e_version));
                            }
                            startActivity(intent);
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
            for (int i = 0; i < 5; i++) {
                rpsl.add(new RPSystem("TEST", Integer.toString(i)));
            }
        }
        /*debug*/

        mAdapter = new AdapterRPSystem(this, rpsl);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void animateMiniFAB(FloatingActionButton miniFAB, Boolean hide) {
        miniFAB.startAnimation(hide ? miniFABHide : miniFABShow);
        miniFAB.setClickable(!hide);
    }
}