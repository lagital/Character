package com.sam.team.character.design;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel2.Category;
import com.sam.team.character.viewmodel2.Element;
import com.sam.team.character.viewmodel2.Field;
import com.sam.team.character.viewmodel2.ListItem;
import com.sam.team.character.viewmodel2.Session;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class ActivityEditElement extends AppCompatActivity{

    private static final String TAG = "ActivityEditElement";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    private Toolbar mToolbar;

    Animation miniFABShow;
    Animation miniFABHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_element);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.categories_list);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        miniFABShow = AnimationUtils.loadAnimation(this, R.anim.mini_fab_show);
        miniFABHide = AnimationUtils.loadAnimation(this, R.anim.mini_fab_hide);

        mMainFAB = (FloatingActionButton) findViewById(R.id.fab_main);

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout l = (LinearLayout) View.inflate(ActivityEditElement.this,
                        R.layout.dialog_new_category, null);
                final EditText name = (EditText) l.findViewById(R.id.name);
                name.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (name.getText().toString().equalsIgnoreCase(
                                getResources().getString(R.string.new_category_dflt_name))) {
                            name.setText("");
                            name.setTextColor(ContextCompat.
                                    getColor(ActivityEditElement.this, R.color.colorPrimary));
                            Log.d(TAG, "Fill category name");
                        }
                        return false;
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEditElement.this);
                builder.setView(l);
                builder.setTitle(R.string.new_category_dialog_title);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing, see dialog.getButton
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        EditText name = (EditText) l.findViewById(R.id.name);
                        String e_name = name.getText().toString();
                        if (!e_name.isEmpty() && !e_name.equalsIgnoreCase(
                                getResources().getString(R.string.new_category_dflt_name))) {
                            //TODO: list updating
                        } else {
                            Toast.makeText(ActivityEditElement.this,
                                    getResources().getString(R.string.new_category_empty_name),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        Intent intent = getIntent();
        ArrayList<ListItem> li;
        li = new ArrayList<>();

        if (intent != null) {
            if (intent.hasExtra(ActivityElementPicker.ELEMENT_NAME_EXTRA) &&
                    intent.hasExtra(ActivityElementPicker.ELEMENT_TYPE_EXTRA)) {
                Element e = Session.getInstance().
                        getCurrentSystem().
                        getElement(
                                intent.getStringExtra(ActivityElementPicker.ELEMENT_TYPE_EXTRA),
                                intent.getStringExtra(ActivityElementPicker.ELEMENT_NAME_EXTRA));

                for (String s : e.getCategories()) {
                    li.add(new Category(s, e));
                    Log.d(TAG, "Category " + s + " has been added");
                    for (Field f : e.getFieldsByCategory(s)) {
                        li.add(f);
                        Log.d(TAG, "Field " + f.getName() + " has been added");
                    }
                }
            }
        }

        mAdapter = new AdapterCategoryField(this, li);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}