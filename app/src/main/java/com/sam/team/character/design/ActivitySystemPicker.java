package com.sam.team.character.design;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel2.RPSystem;

import java.util.ArrayList;

public class ActivitySystemPicker extends AppCompatActivity {
    private static final String TAG = "ActivitySystemPicker";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_picker);

        mRecyclerView = (RecyclerView) findViewById(R.id.systems_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<RPSystem> rpsl = new ArrayList<>();

        /*debug*/
        if (BuildConfig.DEBUG) {
            for (int i = 0; i < 15; i++) {
                rpsl.add(new RPSystem("TEST", Integer.toString(i)));
            }
        }
        /*debug*/

        mAdapter = new AdapterRPSystem(this, rpsl);
        mRecyclerView.setAdapter(mAdapter);
    }
}