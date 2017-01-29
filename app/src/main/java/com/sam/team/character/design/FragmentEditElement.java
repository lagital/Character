package com.sam.team.character.design;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pborisenko on 10/27/2016.
 */

public class FragmentEditElement extends Fragment {

    private static final String TAG = "FragmentEditElement";

    @BindView(R.id.categories_list) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fab_main) FloatingActionButton mMainFAB;
    @BindView(R.id.search_box) EditTextEndCursor searchBox;

    private AdapterCategoryField mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_sheet, null);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                Session.getInstance().getElementFromCache().getName());

        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.renewItems();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.setNamePart(s.toString());
                // change left icon to Clear after the first typed symbol
                if (start == 0 && before == 0 && count > 0) {
                    searchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_clear_black_36dp, 0, 0, 0);
                }
                // change left icon to Search after the first typed symbol
                if (start == 0 && count == 0) {
                    searchBox.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search_black_36dp, 0, 0, 0);
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        });
        searchBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() <= searchBox.getCompoundDrawables()[0].getBounds().width() +
                            getActivity().getResources().getDimension(R.dimen.search_margin_left)) {
                        Log.d(TAG, "Clear search box");
                        // clear search box after clicking Cancel
                        if (!searchBox.getText().toString().equals("")) {
                            searchBox.setText("");
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Main FAB click");

                ArrayList<TextParameter> tpl = new ArrayList<>();
                tpl.add(new TextParameter(FragmentEditElement.this.getResources().getString(R.string.new_category_dflt_name), null, true, null));
                new TextParmsDialogBuilder(
                        getActivity(),
                        R.layout.dialog_settings_container,
                        R.layout.dialog_settings_parameter,
                        R.string.new_category_dialog_title,
                        tpl) {
                    @Override
                    void applySettings() {
                        Session.getInstance().getElementFromCache().addCategory(getResults().get(0));
                        mAdapter.renewItems();
                    }
                };
            }
        });

        mAdapter = new AdapterCategoryField(this);
        mRecyclerView.setAdapter(mAdapter);

        ButterKnife.bind(this, view);
        return view;
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
