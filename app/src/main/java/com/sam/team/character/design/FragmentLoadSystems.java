package com.sam.team.character.design;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;

/**
 * Created by pborisenko on 1/7/2017.
 * This fragment is shown while loading all available systems from files into cache
 */

public class FragmentLoadSystems extends Fragment {

    private static final String TAG = "FragmentLoadSystems";

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_systems, null);

        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.load_systems_title);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        Session.getInstance().collectAvailableSystems(this.getActivity());

        // go to list of cached systems after retrieving
        ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer
                .FragmentType.SYSTEM_PICKER);

        return view;
    }
}
