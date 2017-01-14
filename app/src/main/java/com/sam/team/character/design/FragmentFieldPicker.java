package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;

/**
 * Fragment to choose field.
 * Created by pborisenko on 1/14/2017.
 */

public class FragmentFieldPicker extends Fragment {


    private static final String TAG = "FragmentFieldPicker";

    private RecyclerView mRecyclerView;
    private AdapterPicker mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple_picker, null);

        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.element_fragment_title);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.systems_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterPicker(this, ((ActivityFieldPicker) getActivity()).getElement());
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
}
