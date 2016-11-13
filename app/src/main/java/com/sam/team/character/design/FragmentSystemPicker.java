package com.sam.team.character.design;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel.CleanOnTouchListener;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysField;
import com.sam.team.character.viewmodel.SysRPSystem;

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
    private ArrayList<SysRPSystem> systems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_picker, null);

        Log.d(TAG, "onCreateView");

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
                Log.d(TAG, "mAddMiniFAB - add new game");

                final LinearLayout l = (LinearLayout) View.inflate(getActivity(),
                        R.layout.dialog_new_system, null);
                final EditText name = (EditText) l.findViewById(R.id.name);
                final EditText version = (EditText) l.findViewById(R.id.version);
                final EditText copyright = (EditText) l.findViewById(R.id.copyright);
                name.setOnTouchListener(new CleanOnTouchListener(getActivity(), name,
                        R.string.new_system_dflt_name));
                version.setOnTouchListener(new CleanOnTouchListener(getActivity(), version,
                        R.string.new_system_dflt_version));
                copyright.setOnTouchListener(new CleanOnTouchListener(getActivity(), copyright,
                        R.string.new_system_dflt_copyright));

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
                            SysRPSystem rps;

                            if (e_version.isEmpty()) {
                                rps = new SysRPSystem(e_name,
                                        getResources().getString(R.string.new_system_dflt_version),
                                        e_copyright);
                                systems.add(rps);
                                Session.getInstance().setCurrentSystem(rps);
                            } else {
                                rps = new SysRPSystem(e_name, e_version, e_copyright);
                                systems.add(rps);
                            }
                            fillList();
                            Session.getInstance().setCurrentSystem(rps);
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
                Log.d(TAG, "mLoadMiniFAB - load new game");
                //TODO: system import
            }
        });

        /* DEBUG */
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "fill debug values");

            int i;
            SysRPSystem rps = new SysRPSystem("Game", "1.0", "Bla-bla");
            SysElement e = new SysElement("Character Sheet", "CHARACTER", rps);
            e.addField(new SysField("Main", "Name", SysField.FieldType.SHORT_TEXT, e));
            e.addField(new SysField("Main", "Surname", SysField.FieldType.SHORT_TEXT, e));
            e.addField(new SysField("Additional", "Knowledge", SysField.FieldType.LONG_TEXT, e));
            SysField f = new SysField("Additional", "Power", SysField.FieldType.NUMERIC, e);
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
        Log.d(TAG, "fillList");

        items.clear();
        for (SysRPSystem s : systems) {
            items.add(s);
            for (SysElement e : s.getElements()) {
                items.add(e);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");

        super.onCreate(savedInstanceState);
    }
}
