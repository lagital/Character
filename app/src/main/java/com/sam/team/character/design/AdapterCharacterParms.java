package com.sam.team.character.design;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.ItemCharacterCategoryBinding;
import com.sam.team.character.databinding.ItemCharacterFieldBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelCategory;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelField;

import java.util.ArrayList;

/**
 * Adapter to fill Character's parameters.
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCharacterParms extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterCategoryField";

    private ArrayList<ListItem> items;
    private FragmentCharacter fragment;

    AdapterCharacterParms(FragmentCharacter fragment) {
        this.fragment = fragment;
        this.items = new ArrayList<>();
        renewItems();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_CATEGORY) {
            ItemCharacterCategoryBinding binding = ItemCharacterCategoryBinding.inflate(inflater, parent, false);
            return new ViewHolderCharacterCategoryItem(binding.getRoot());
        } else {
            ItemCharacterFieldBinding binding = ItemCharacterFieldBinding.inflate(inflater, parent, false);
            return new ViewHolderCharacterFieldItem(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_CATEGORY) {
            ViewHolderCharacterCategoryItem h = (ViewHolderCharacterCategoryItem) holder;
            h.binding.setCategory((ViewModelCategory) items.get(position));
        } else if (type == ListItem.TYPE_FIELD) {
            ViewHolderCharacterFieldItem h = (ViewHolderCharacterFieldItem) holder;
            h.binding.setField((ViewModelField) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click on field " + Integer.toString(position));
                    // cache field for further changing in fragment
                    Session.getInstance().cacheCategory(((ViewModelField) items.get(position)).getCategory());
                    Session.getInstance().cacheField((ViewModelField) items.get(position));

                    // TODO: interaction with fields
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderCharacterCategoryItem extends RecyclerView.ViewHolder {

        ItemCharacterCategoryBinding binding;

        ViewHolderCharacterCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderCharacterFieldItem extends RecyclerView.ViewHolder {

        ItemCharacterFieldBinding binding;

        ViewHolderCharacterFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    void renewItems() {
        Log.d(TAG, "renewItems");
        items.clear();
        ViewModelElementType tmpE = Session.getInstance().getElementFromCache();
        for (ViewModelCategory c : tmpE.getCategories()) {
            items.add(c);
            for (ViewModelField f : c.getFields()) {
                items.add(f);
            }
        }
        notifyDataSetChanged();
    }
}