package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.databinding.ItemCategoryBinding;
import com.sam.team.character.databinding.ItemFieldBinding;
import com.sam.team.character.viewmodel.Category;
import com.sam.team.character.viewmodel.Field;
import com.sam.team.character.viewmodel.ListItem;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCategoryField extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterCategoryField";

    private ArrayList<ListItem> items;
    private Context context;

    public AdapterCategoryField(Context context, ArrayList<ListItem> items) {
        this.items = items;
        this.context = context;

        Log.d(TAG, "Items total:" + Integer.toString(items.size()));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_CATEGORY) {
            ItemCategoryBinding binding = ItemCategoryBinding.inflate(inflater, parent, false);
            return new ViewHolderCategoryItem(binding.getRoot());
        } else {
            ItemFieldBinding binding = ItemFieldBinding.inflate(inflater, parent, false);
            return new ViewHolderFieldItem(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_CATEGORY) {
            Log.d(TAG, "Bind category");
            ViewHolderCategoryItem h = (ViewHolderCategoryItem) holder;
            h.binding.setCategory((Category) items.get(position));
            h.binding.setPlusclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Add field " + Integer.toString(position));
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit category " + Integer.toString(position));
                    // TODO: changing category
                }
            });
        } else if (type == ListItem.TYPE_FIELD)  {
            Log.d(TAG, "Bind field");
            ViewHolderFieldItem h = (ViewHolderFieldItem) holder;
            h.binding.setField((Field) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click on field " + Integer.toString(position));
                    // TODO: changing field
                }
            });

            switch (((Field) items.get(position)).getType()) {
                case SHORT_TEXT: {
                    h.binding.icon0.setImageResource(R.drawable.ic_title_black_24dp);
                }
                case LONG_TEXT: {
                    h.binding.icon0.setImageResource(R.drawable.ic_text_fields_black_24dp);
                }
                case NUMERIC: {
                    for (int i = 0; i < ((Field) items.get(position)).getValues().size(); i++) {
                        int resource;
                        if (((Field) items.get(position)).getRule(i) != null) {
                            resource = R.drawable.ic_functions_black_24dp;
                        } else {
                            resource = R.drawable.ic_looks_one_black_24dp;
                        }

                        switch (i) {
                            case 0: {
                                h.binding.icon0.setImageResource(resource);
                            }
                            case 1: {
                                h.binding.icon1.setImageResource(resource);
                                h.binding.icon1.setVisibility(View.VISIBLE);
                            }
                            case 2: {
                                h.binding.icon2.setImageResource(resource);
                                h.binding.icon2.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderCategoryItem extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        ViewHolderCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderFieldItem extends RecyclerView.ViewHolder {

        ItemFieldBinding binding;

        ViewHolderFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }
}