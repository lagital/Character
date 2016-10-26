package com.sam.team.character.design;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Element;
import com.sam.team.character.viewmodel.Field;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.RPSystem;
import com.sam.team.character.viewmodel.Session;

import java.util.ArrayList;

public class ActivitySystemPicker extends AppCompatActivity {

    private static final String TAG = "ActivitySystemPicker";


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private FloatingActionButton mAddMiniFAB;
    private FloatingActionButton mLoadMiniFAB;
    private Toolbar mToolbar;
    private ArrayList<ListItem> items;
    private ArrayList<RPSystem> systems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_picker);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.systems_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainFAB = (FloatingActionButton) findViewById(R.id.fab_main);
        mAddMiniFAB = (FloatingActionButton) findViewById(R.id.fab_add);
        mLoadMiniFAB = (FloatingActionButton) findViewById(R.id.fab_load);

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    animateMiniFAB(mAddMiniFAB, mAddMiniFAB.getVisibility());
                    animateMiniFAB(mLoadMiniFAB, mAddMiniFAB.getVisibility());
            }
        });

        items = new ArrayList<>();
        systems = new ArrayList<>();

        mAddMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout l = (LinearLayout) View.inflate(ActivitySystemPicker.this,
                        R.layout.dialog_new_system, null);
                final EditText name = (EditText) l.findViewById(R.id.name);
                final EditText version = (EditText) l.findViewById(R.id.version);
                final EditText copyright = (EditText) l.findViewById(R.id.copyright);
                name.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (name.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_name))) {
                            name.setText("");
                            name.setTextColor(ContextCompat.
                                    getColor(ActivitySystemPicker.this, R.color.colorPrimaryText));
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
                                    getColor(ActivitySystemPicker.this, R.color.colorPrimaryText));
                            Log.d(TAG, "Fill version");
                        }
                        return false;
                    }
                });

                copyright.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (copyright.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_copyright))) {
                            copyright.setText("");
                            copyright.setTextColor(ContextCompat.
                                    getColor(ActivitySystemPicker.this, R.color.colorPrimaryText));
                            Log.d(TAG, "Fill copyright");
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

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        EditText name = (EditText) l.findViewById(R.id.name);
                        EditText version = (EditText) l.findViewById(R.id.version);
                        EditText copyright = (EditText) l.findViewById(R.id.copyright);
                        String e_name = name.getText().toString();
                        String e_version = version.getText().toString();
                        String e_copyright = copyright.getText().toString();
                        if (!e_name.isEmpty() && !e_name.equalsIgnoreCase(
                                getResources().getString(R.string.new_system_dflt_name))) {
                            RPSystem rps;

                            if (e_version.isEmpty()) {
                                rps = new RPSystem(e_name,
                                        getResources().getString(R.string.new_system_dflt_version),
                                        e_copyright);
                                systems.add(rps);
                                Session.getInstance().setCurrentSystem(rps);
                            } else {
                                rps = new RPSystem(e_name, e_version, e_copyright);
                                systems.add(rps);
                            }
                            fillList();
                            mAdapter.notifyDataSetChanged();
                            Session.getInstance().setCurrentSystem(rps);
                            dialog.cancel();
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

        /* DEBUG */
        if (BuildConfig.DEBUG) {
            RPSystem rps = new RPSystem("Game", "1.0", "Bla-bla");
            Element e = new Element("Character Sheet", "CHARACTER", rps);
            e.addField(new Field("Main", "Name"));
            e.addField(new Field("Main", "Surname"));
            e.addField(new Field("Additional", "Knowledge"));
            rps.addElement(e);
            systems.add(rps);
        }
        /* DEBUG */

        fillList();

        mAdapter = new AdapterSystemElement(this, items);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void animateMiniFAB(FloatingActionButton miniFAB, Integer visibility) {
        if (visibility == View.VISIBLE) {
            miniFAB.hide();
        } else {
            miniFAB.show();
        }
    }

    private void fillList () {
        items.clear();
        for (RPSystem s : systems) {
            items.add(s);
            for (Element e : s.getElements()) {
                items.add(e);
            }
        }
    }
}