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

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.SysCategory;
import com.sam.team.character.viewmodel.SysSheet;
import com.sam.team.character.viewmodel.SysField;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/27/2016.
 */

public class FragmentEditSheet extends Fragment {

    private static final String TAG = "FragmentEditSheet";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    /* Categories and fields: */
    private ArrayList<ListItem> items;

    private SysSheet element;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_sheet, null);

        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_sheet_title);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.categories_list);
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

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Main FAB click");

                ArrayList<TextParameter> tpl = new ArrayList<>();
                tpl.add(new TextParameter(FragmentEditSheet.this.getResources().getString(R.string.new_category_dflt_name), null, true));
                TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                        getActivity(),
                        R.layout.dialog_settings_container,
                        R.layout.dialog_settings_parameter,
                        R.string.new_category_dialog_title,
                        tpl) {
                    @Override
                    void applySettings() {
                        items.add(new SysCategory(getResults().get(0)));
                        mAdapter.notifyDataSetChanged();
                    }
                };
            }
        });

        items = new ArrayList<>();
        element = Session.getInstance().getSheetFromCache();
        fillList();

        mAdapter = new AdapterCategoryField(this, items);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    public void fillList () {
        Log.d(TAG, "fillList");

        items.clear();
        if (element != null) {
            for (String s : element.getCategories()) {
                items.add(new SysCategory(s, element));
                for (SysField f : element.getFieldsByCategory(s)) {
                    items.add(f);
                }
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_edit_element, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_edit_element: {
                ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.HELP);
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
