package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.ItemCategoryBinding;
import com.sam.team.character.databinding.ItemFieldBinding;
import com.sam.team.character.viewmodel2.Category;
import com.sam.team.character.viewmodel2.Field;
import com.sam.team.character.viewmodel2.ListItem;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCategoryField extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterCategories";

    private ArrayList<ListItem> items;
    private Context context;

    public AdapterCategoryField(Context context, ArrayList<ListItem> items) {
        this.items = items;
        this.context = context;

        Log.d(TAG, "Items total:" + Integer.toString(items.size()));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == Category.TYPE_HEADER) {
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
        if (type == Category.TYPE_HEADER) {
            Log.d(TAG, "Bind category");
            ViewHolderCategoryItem h = (ViewHolderCategoryItem) holder;
            h.binding.setCategory((Category) items.get(position));
            h.binding.setPlusclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: add field
                    Log.d(TAG, "Click on category " + Integer.toString(position));
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit category " + Integer.toString(position));
                    // TODO: changing category
                }
            });
        } else {
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
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolderCategoryItem extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        ViewHolderCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    class ViewHolderFieldItem extends RecyclerView.ViewHolder {

        ItemFieldBinding binding;

        ViewHolderFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }
}