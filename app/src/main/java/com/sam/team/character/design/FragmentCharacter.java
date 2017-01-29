package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main fragment for interacting with chosen character.
 * Created by pborisenko on 1/15/2017.
 */

public class FragmentCharacter extends Fragment {

    private static final String TAG = "FragmentCharacter";

    @BindView(R.id.categories_list) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;

    private AdapterCharacterParms mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_character, null);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Session.getInstance().getElementFromCache().getName());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.renewItems();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        mAdapter = new AdapterCharacterParms(this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}
