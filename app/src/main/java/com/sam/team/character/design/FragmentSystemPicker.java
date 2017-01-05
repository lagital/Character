package com.sam.team.character.design;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.core.Sheet;
import com.sam.team.character.corev2.SB_ElementType;
import com.sam.team.character.corev2.SB_Field;
import com.sam.team.character.corev2.SB_System;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.ViewModelCategory;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelField;
import com.sam.team.character.viewmodel.ViewModelSystem;

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
    private ArrayList<SB_System> systems;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fillList();
                mAdapter.notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

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
                        // instantiate new System and create new ViewModel envelope for it
                        ViewModelSystem tmp = new ViewModelSystem(new SB_System(
                                getResults().get(0),
                                getResults().get(1),
                                getResults().get(2))
                        );
                        items.add(tmp);
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

            SB_System rps = new SB_System("Game", "1.0", "Bla-bla");
            rps.addElement("Character Sheet");
            rps.getElement("Character Sheet").addCategory("Main");
            rps.getElement("Character Sheet").addCategory("Additional");
            rps.addField("Character Sheet", "Main", "Name");
            rps.getField("Character Sheet", "Main", "Name").setType(SB_Field.FieldType.SHORT_TEXT);
            rps.addField("Character Sheet", "Main", "Age");
            rps.getField("Character Sheet", "Main", "Age").setType(SB_Field.FieldType.NUMERIC);
            rps.addField("Character Sheet", "Additional", "Knowledge");
            rps.getField("Character Sheet", "Additional", "Knowledge").setType(SB_Field.FieldType.LONG_TEXT);
            rps.addField("Character Sheet", "Additional", "Power");
            rps.getField("Character Sheet", "Additional", "Power").setType(SB_Field.FieldType.CALCULATED);
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

        // put model objects into ViewModel envelopes
        for (SB_System s : systems) {
            items.add(new ViewModelSystem(s));
            for (String se : s.getElements()) {
                items.add(new ViewModelElementType(s.getElement(se)));
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
