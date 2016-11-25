package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.databinding.ItemSyscategoryBinding;
import com.sam.team.character.databinding.ItemSysfieldBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.SysCategory;
import com.sam.team.character.viewmodel.SysField;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCategoryField extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterCategoryField";

    private ArrayList<ListItem> items;
    private Context context;

    AdapterCategoryField(Context context, ArrayList<ListItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_CATEGORY) {
            ItemSyscategoryBinding binding = ItemSyscategoryBinding.inflate(inflater, parent, false);
            return new ViewHolderSysCategoryItem(binding.getRoot());
        } else {
            ItemSysfieldBinding binding = ItemSysfieldBinding.inflate(inflater, parent, false);
            return new ViewHolderSysFieldItem(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_CATEGORY) {
            ViewHolderSysCategoryItem h = (ViewHolderSysCategoryItem) holder;
            h.binding.setCategory((SysCategory) items.get(position));
            h.binding.setPlusclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Add field " + Integer.toString(position));
                    Session.getInstance().cacheCategory(((SysCategory) items.get(position)).getName());
                    ((ActivityContainer) context).replaceFragment(ActivityContainer
                            .FragmentType.EDIT_FIELD);
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit category " + Integer.toString(position));
                    // TODO: changing category
                }
            });
        } else if (type == ListItem.TYPE_FIELD) {
            ViewHolderSysFieldItem h = (ViewHolderSysFieldItem) holder;
            h.binding.setField((SysField) items.get(position));
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

    private class ViewHolderSysCategoryItem extends RecyclerView.ViewHolder {

        ItemSyscategoryBinding binding;

        ViewHolderSysCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderSysFieldItem extends RecyclerView.ViewHolder {

        ItemSysfieldBinding binding;

        ViewHolderSysFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }
}