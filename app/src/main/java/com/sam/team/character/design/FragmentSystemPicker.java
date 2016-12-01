package com.sam.team.character.design;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.core.Element;
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

                ArrayList<TextParameter> tpl = new ArrayList<>();
                tpl.add(new TextParameter(FragmentSystemPicker.this.getResources().getString(R.string.new_system_dflt_name), null, true));
                tpl.add(new TextParameter(FragmentSystemPicker.this.getResources().getString(R.string.new_system_dflt_version), null, false));
                tpl.add(new TextParameter(FragmentSystemPicker.this.getResources().getString(R.string.new_system_dflt_copyright), null, false));
                TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                        getActivity(),
                        R.layout.dialog_settings_container,
                        R.layout.dialog_settings_parameter,
                        R.string.edit_system_dialog_title,
                        tpl) {
                    @Override
                    void applySettings() {
                        systems.add(new SysRPSystem(
                                getResults().get(0),
                                getResults().get(1),
                                getResults().get(2)));
                        fillList();
                    }
                };
            }
        });

        mLoadMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mLoadMiniFAB - load new game");

            }
        });

        /* DEBUG */
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "fill debug values");

            int i;
            SysRPSystem rps = new SysRPSystem("Game", "1.0", "Bla-bla");
            SysElement e = new SysElement("Character Sheet", Element.ElementType.CHARACTER_SHEET, rps);
            e.addField(new SysField("Main", "Name", SysField.FieldType.SHORT_TEXT, e));
            e.addField(new SysField("Additional", "Knowledge", SysField.FieldType.LONG_TEXT, e));
            SysField f = new SysField("Additional", "Power", SysField.FieldType.CALCULATED, e);
            f.setValue("Test1");
            f.setRule("Test");
            e.addField(f);
            e.addField(new SysField("Additional", "Ololo", SysField.FieldType.NUMERIC, e));
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


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_system_picker, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_system_picker: {
                ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.HELP);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
