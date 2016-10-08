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
import com.sam.team.character.viewmodel2.Element;
import com.sam.team.character.viewmodel2.RPSystem;
import com.sam.team.character.viewmodel2.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ActivityElementPicker extends AppCompatActivity {

    private static final String TAG = "ActivityElementPicker";

    public static final String ELEMENT_NAME_EXTRA = "ELEMENT_NAME_EXTRA";
    public static final String ELEMENT_TYPE_EXTRA = "ELEMENT_TYPE_EXTRA";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private Toolbar mToolbar;

    Animation miniFABShow;
    Animation miniFABHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_picker);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.elements_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        miniFABShow = AnimationUtils.loadAnimation(this, R.anim.mini_fab_show);
        miniFABHide = AnimationUtils.loadAnimation(this, R.anim.mini_fab_hide);

        mMainFAB = (FloatingActionButton) findViewById(R.id.fab_main);

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout l = (LinearLayout) View.inflate(ActivityElementPicker.this,
                        R.layout.dialog_new_element, null);
                final EditText name = (EditText) l.findViewById(R.id.name);
                name.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (name.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_element_dflt_name))) {
                            name.setText("");
                            name.setTextColor(ContextCompat.
                                    getColor(ActivityElementPicker.this, R.color.colorPrimary));
                            Log.d(TAG, "Fill element name");
                        }
                        return false;
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityElementPicker.this);
                builder.setView(l);
                builder.setTitle(R.string.new_element_dialog_title);
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
                        String e_name = name.getText().toString();
                        if (!e_name.isEmpty() && !e_name.equalsIgnoreCase(
                                getResources().getString(R.string.new_element_dflt_name))) {

                            Intent intent = new Intent(ActivityElementPicker.this, ActivityEditElement.class);

                            //TODO: fix type filling

                            Element e = new Element(e_name, "Character");
                            Session.getInstance().getCurrentSystem().addElement(e);
                            intent.putExtra(ELEMENT_NAME_EXTRA, e.getName());
                            intent.putExtra(ELEMENT_TYPE_EXTRA, e.getType());
                            startActivity(intent);
                        } else {
                            Toast.makeText(ActivityElementPicker.this,
                                    getResources().getString(R.string.new_element_empty_name),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        mAdapter = new AdapterElement(this, Session.getInstance().getCurrentSystem().getElements());
        mRecyclerView.setAdapter(mAdapter);
    }

    private void animateMiniFAB(FloatingActionButton miniFAB, Boolean hide) {
        miniFAB.startAnimation(hide ? miniFABHide : miniFABShow);
        miniFAB.setClickable(!hide);
    }
}