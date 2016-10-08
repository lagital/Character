package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.adapters.CheckedTextViewBindingAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.expandablerecyclerview.Adapter.ExpandableRecyclerAdapter;
import com.sam.team.character.databinding.CategoryItemBinding;
import com.sam.team.character.databinding.ElementItemBinding;
import com.sam.team.character.databinding.FieldItemBinding;
import com.sam.team.character.viewmodel2.Category;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.ViewHolderCategoryItem> {

    private static final String TAG = "AdapterCategories";

    private ArrayList<Category> categories;
    private Context context;

    AdapterCategories (Context context, ArrayList<Category> categories) {
        this.categories = categories;
        this.context = context;
    }

    @Override
    public ViewHolderCategoryItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CategoryItemBinding binding = CategoryItemBinding.inflate(inflater, parent, false);
        return new ViewHolderCategoryItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderCategoryItem holder, final int position) {
        holder.binding.setCategory(categories.get(position));
        holder.binding.setPlusclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: add field
                Log.d(TAG, "Click on category " + Integer.toString(position));
            }
        });
        holder.binding.setEditclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit category " + Integer.toString(position));
                // TODO: changing category
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class ViewHolderFieldItem extends RecyclerView.ViewHolder {

        FieldItemBinding binding;

        ViewHolderFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    class ViewHolderCategoryItem extends RecyclerView.ViewHolder {

        CategoryItemBinding binding;

        ViewHolderCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}