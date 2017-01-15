package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;

/**
 * Created by pborisenko on 1/7/2017.
 * This fragment is shown while loading all available systems from files into cache
 */

public class FragmentLoadSystems extends Fragment {

    private static final String TAG = "FragmentLoadSystems";

    private RelativeLayout loaderContainer;
    private SwipeRefreshLayout listContainer;
    private RecyclerView mRecyclerView;
    private AdapterSystemCharacter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_systems, null);

        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.load_systems_title);

        loaderContainer = (RelativeLayout) view.findViewById(R.id.loader_contaner);
        listContainer = (SwipeRefreshLayout) view.findViewById(R.id.list_container);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.systems_list);
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

        // go to list of cached systems after retrieving
        //((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.SYSTEM_PICKER);

        return view;
    }
}
