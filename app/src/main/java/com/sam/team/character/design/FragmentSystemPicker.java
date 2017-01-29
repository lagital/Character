package com.sam.team.character.design;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelSystem;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

/**
 * Fragment to work with systems.
 * Created by pborisenko on 10/27/2016.
 */

public class FragmentSystemPicker extends Fragment{

    private static final String TAG = "FragmentSystemPicker";

    private static final int REQUEST_CODE_PICK_FILE = 10007;

    @BindView(R.id.systems_list) RecyclerView mRecyclerView;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fab_main) FloatingActionButton mMainFAB;
    @BindView(R.id.fab_add) FloatingActionButton mAddMiniFAB;
    @BindView(R.id.fab_load) FloatingActionButton mLoadMiniFAB;

    private AdapterSystemElement mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_system_picker, null);
        ButterKnife.bind(this, view);
        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.system_picker_title);

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

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateMiniFAB(mAddMiniFAB, mAddMiniFAB.getVisibility());
                animateMiniFAB(mLoadMiniFAB, mAddMiniFAB.getVisibility());
            }
        });

        mAddMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "mAddMiniFAB - add new game");

                ArrayList<TextParameter> tpl = new ArrayList<>();
                tpl.add(new TextParameter(FragmentSystemPicker.this.getResources().getString(R.string.new_system_dflt_name), null, true, null));
                tpl.add(new TextParameter(FragmentSystemPicker.this.getResources().getString(R.string.new_system_dflt_version), null, false, null));
                tpl.add(new TextParameter(FragmentSystemPicker.this.getResources().getString(R.string.new_system_dflt_copyright), null, false, null));
                TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                        getActivity(),
                        R.layout.dialog_settings_container,
                        R.layout.dialog_settings_parameter,
                        R.string.edit_system_dialog_title,
                        tpl) {
                    @Override
                    void applySettings() {
                        // instantiate new System and create new ViewModel envelope for it
                        ViewModelSystem tmp = new ViewModelSystem(
                                getResults().get(0),
                                getResults().get(1),
                                getResults().get(2));
                        Session.getInstance().getSystemStorage().add(tmp);
                        mAdapter.renewItems();
                    }
                };
            }
        });

        mLoadMiniFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                //intent.addCategory(Intent.CATEGORY_);
                startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
            }
        });

        mAdapter = new AdapterSystemElement(this);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void animateMiniFAB(FloatingActionButton miniFAB, Integer visibility) {
        if (visibility == View.VISIBLE) {
            miniFAB.hide();
        } else {
            miniFAB.show();
        }
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
            case R.id.go_to_load_systems: {
                ((ActivityContainer) getActivity()).replaceFragment(ActivityContainer.FragmentType.LOAD_SYSTEMS);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_PICK_FILE) {
                try {
                    ViewModelSystem tmp = new ViewModelSystem();
                    Log.d(TAG, readTextFromURI(getActivity(), data.getData()));
                    tmp.fillFromXML(readTextFromURI(getActivity(), data.getData()));
                    Session.getInstance().getSystemStorage().add(tmp);
                    mAdapter.renewItems();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(
                            FragmentSystemPicker.this.getActivity(),
                            FragmentSystemPicker.this.getResources()
                                    .getString(R.string.load_system_failed),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String readTextFromURI(Context context, Uri contentUri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(contentUri);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}
