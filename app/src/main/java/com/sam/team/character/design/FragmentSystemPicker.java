package com.sam.team.character.design;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Element;
import com.sam.team.character.viewmodel.Field;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.RPSystem;
import com.sam.team.character.viewmodel.Context;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/27/2016.
 */

public class FragmentSystemPicker extends Fragment{

    private static final String TAG = "FragmentSystemPicker";


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private FloatingActionButton mAddMiniFAB;
    private FloatingActionButton mLoadMiniFAB;
    private ArrayList<ListItem> items;
    private ArrayList<RPSystem> systems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_picker, null);

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.system_picker_title);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.systems_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainFAB = (FloatingActionButton) view.findViewById(R.id.fab_main);
        mAddMiniFAB = (FloatingActionButton) view.findViewById(R.id.fab_add);
        mLoadMiniFAB = (FloatingActionButton) view.findViewById(R.id.fab_load);

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
                final LinearLayout l = (LinearLayout) View.inflate(getActivity(),
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
                                    getColor(getActivity(), R.color.colorPrimaryText));
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
                                    getColor(getActivity(), R.color.colorPrimaryText));
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
                                    getColor(getActivity(), R.color.colorPrimaryText));
                            Log.d(TAG, "Fill copyright");
                        }
                        return false;
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                                Context.getInstance().setCurrentSystem(rps);
                            } else {
                                rps = new RPSystem(e_name, e_version, e_copyright);
                                systems.add(rps);
                            }
                            fillList();
                            Context.getInstance().setCurrentSystem(rps);
                            dialog.cancel();
                        } else {
                            Toast.makeText(getActivity(),
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
            int i;
            RPSystem rps = new RPSystem("Game", "1.0", "Bla-bla");
            Element e = new Element("Character Sheet", "CHARACTER", rps);
            e.addField(new Field("Main", "Name", Field.FieldType.SHORT_TEXT));
            e.addField(new Field("Main", "Surname", Field.FieldType.SHORT_TEXT));
            e.addField(new Field("Additional", "Knowledge", Field.FieldType.LONG_TEXT));
            Field f = new Field("Additional", "Power", Field.FieldType.NUMERIC);
            i = f.addValue("Test1");
            i = f.addValue("Test2");
            i = f.addValue("Test3");
            f.setRule(i, "Test");
            e.addField(f);
            rps.addElement(e);
            systems.add(rps);
        }
        /* DEBUG */

        mAdapter = new AdapterSystemElement(this, items);
        mRecyclerView.setAdapter(mAdapter);

        fillList();

        return view;
    }

    private void animateMiniFAB(FloatingActionButton miniFAB, Integer visibility) {
        if (visibility == View.VISIBLE) {
            miniFAB.hide();
        } else {
            miniFAB.show();
        }
    }

    public void fillList () {
        items.clear();
        for (RPSystem s : systems) {
            items.add(s);
            for (Element e : s.getElements()) {
                items.add(e);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
