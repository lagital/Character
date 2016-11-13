package com.sam.team.character.design;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.CleanOnTouchListener;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.SysCategory;
import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysField;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/27/2016.
 */

public class FragmentEditElement extends Fragment {

    private static final String TAG = "FragmentEditElement";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mMainFAB;
    /* Categories and fields: */
    private ArrayList<ListItem> items;

    private SysElement element;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_element, null);

        Log.d(TAG, "onCreateView");

        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.edit_element_title);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.categories_list);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mMainFAB = (FloatingActionButton) view.findViewById(R.id.fab_main);

        mMainFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Main FAB click");

                final LinearLayout l = (LinearLayout) View.inflate(getActivity(),
                        R.layout.dialog_new_category, null);
                final EditText name = (EditText) l.findViewById(R.id.name);
                name.setOnTouchListener(new CleanOnTouchListener(getActivity(), name,
                        R.string.new_category_dflt_name));

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

                final AlertDialog dialog = builder.create();
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
                            items.add(new SysCategory(e_name));
                            mAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        } else {
                            Toast.makeText(getActivity(),
                                    getResources().getString(R.string.new_category_empty_name),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        items = new ArrayList<>();
        element = Session.getInstance().getElementFromCache();
        fillList();

        mAdapter = new AdapterCategoryField(getActivity(), items);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void fillList () {
        Log.d(TAG, "fillList");

        items.clear();
        if (element != null) {
            for (String s : element.getCategories()) {
                items.add(new SysCategory(s, element));
                for (SysField f : element.getFieldsByCategory(s)) {
                    items.add(f);
                }
            }
        }
    }
}
