package com.sam.team.character.design;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.sam.team.character.viewmodel2.RPSystem;

import java.util.ArrayList;

public class ActivitySystemPicker extends AppCompatActivity {
    private static final String TAG = "ActivitySystemPicker";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private FloatingActionButton mAddMiniFAB;
    private FloatingActionButton mLoadMiniFAB;
    private FrameLayout.LayoutParams addLayoutParams;
    private FrameLayout.LayoutParams loadLayoutParams;

    Animation miniFABShow;
    Animation miniFABHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_picker);

        mRecyclerView = (RecyclerView) findViewById(R.id.systems_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        miniFABShow = AnimationUtils.loadAnimation(this, R.anim.mini_fab_show);
        miniFABHide = AnimationUtils.loadAnimation(this, R.anim.mini_fab_hide);

        mMainFAB = (FloatingActionButton) findViewById(R.id.fab_main);
        mAddMiniFAB = (FloatingActionButton) findViewById(R.id.fab_add);
        mLoadMiniFAB = (FloatingActionButton) findViewById(R.id.fab_load);

        addLayoutParams = (FrameLayout.LayoutParams) mAddMiniFAB.getLayoutParams();
        loadLayoutParams = (FrameLayout.LayoutParams) mLoadMiniFAB.getLayoutParams();

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAddMiniFAB.isClickable()) {
                    animateMiniFAB(mAddMiniFAB, true, addLayoutParams, 1.7, 0.25);
                    animateMiniFAB(mLoadMiniFAB, true, loadLayoutParams, 1.5, 1.5);
                } else {
                    animateMiniFAB(mAddMiniFAB, false, addLayoutParams, 1.7, 0.25);
                    animateMiniFAB(mLoadMiniFAB, false, loadLayoutParams, 1.5, 1.5);
                }
            }
        });

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

    private void animateMiniFAB(FloatingActionButton miniFAB, Boolean hide,
                                FrameLayout.LayoutParams params, double posWidth, double posHeight) {
            params.rightMargin = params.rightMargin + (hide ? -1 : 1) * ((int) (miniFAB.getWidth() * posWidth));
            params.bottomMargin = params.bottomMargin + (hide ? -1 : 1) * ((int) (miniFAB.getHeight() * posHeight));
            miniFAB.setLayoutParams(params);

            miniFAB.startAnimation(hide ? miniFABHide : miniFABShow);
            miniFAB.setClickable(!hide);
    }
}