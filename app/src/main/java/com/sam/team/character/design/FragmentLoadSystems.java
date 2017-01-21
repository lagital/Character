package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
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
import android.widget.RelativeLayout;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pborisenko on 1/7/2017.s
 * This fragment is shown while loading all available systems from files into cache
 */

public class FragmentLoadSystems extends Fragment {

    private static final String TAG = "FragmentLoadSystems";

    @BindView(R.id.loader_contaner) RelativeLayout loaderContainer;
    @BindView(R.id.list_container) SwipeRefreshLayout listContainer;
    @BindView(R.id.systems_list) RecyclerView mRecyclerView;

    private AdapterSystemCharacter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_systems, null);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.load_systems_title);

        ViewCompat.setNestedScrollingEnabled(mRecyclerView, false);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        listContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.renewItems();
                mAdapter.notifyDataSetChanged();
                listContainer.setRefreshing(false);
            }
        });

        mAdapter = new AdapterSystemCharacter(this);
        mRecyclerView.setAdapter(mAdapter);

        Session.getInstance().collectAvailableSystems(this.getActivity());

        mAdapter.renewItems();
        mAdapter.notifyDataSetChanged();

        loaderContainer.setVisibility(View.GONE);
        listContainer.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_load_systems, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help_load_systems: {
                ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.HELP);
                break;
            }
            case R.id.go_to_builder: {
                ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.SYSTEM_PICKER);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
