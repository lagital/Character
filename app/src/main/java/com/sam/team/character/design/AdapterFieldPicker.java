package com.sam.team.character.design;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.SimpleItemCategoryBinding;
import com.sam.team.character.databinding.SimpleItemElementBinding;
import com.sam.team.character.databinding.SimpleItemFieldBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelCategory;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelField;

import java.util.ArrayList;

/**
 * Created by pborisenko on 1/14/2017.
 */

class AdapterFieldPicker extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterPicker";

    private ArrayList<ListItem> items;
    private Fragment fragment;

    AdapterFieldPicker(Fragment fragment, ViewModelElementType element) {
        this.fragment = fragment;
        this.items = new ArrayList<>();
        renewItems(element);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {
            case ListItem.TYPE_ELEMENT: {
                SimpleItemElementBinding binding = SimpleItemElementBinding.inflate(inflater, parent, false);
                return new AdapterFieldPicker.ViewHolderSimpleElementItem(binding.getRoot());
            }
            case ListItem.TYPE_CATEGORY: {
                SimpleItemCategoryBinding binding = SimpleItemCategoryBinding.inflate(inflater, parent, false);
                return new AdapterFieldPicker.ViewHolderSimpleCategoryItem(binding.getRoot());
            }
            case ListItem.TYPE_FIELD: {
                SimpleItemFieldBinding binding = SimpleItemFieldBinding.inflate(inflater, parent, false);
                return new AdapterFieldPicker.ViewHolderSimpleFieldItem(binding.getRoot());
            }
            default: {
                SimpleItemElementBinding binding = SimpleItemElementBinding.inflate(inflater, parent, false);
                return new AdapterFieldPicker.ViewHolderSimpleElementItem(binding.getRoot());
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        switch (type) {
            case ListItem.TYPE_ELEMENT: {
                AdapterFieldPicker.ViewHolderSimpleElementItem h = (AdapterFieldPicker.ViewHolderSimpleElementItem) holder;
                h.binding.setElement((ViewModelElementType) items.get(position));
                h.binding.setCardclick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ActivityFieldPicker) fragment.getActivity()).nextStep((ViewModelElementType) items.get(position));
                    }
                });
                break;
            }
            case ListItem.TYPE_CATEGORY: {
                AdapterFieldPicker.ViewHolderSimpleCategoryItem h = (AdapterFieldPicker.ViewHolderSimpleCategoryItem) holder;
                h.binding.setCategory((ViewModelCategory) items.get(position));
                break;
            }
            case ListItem.TYPE_FIELD: {
                AdapterFieldPicker.ViewHolderSimpleFieldItem h = (AdapterFieldPicker.ViewHolderSimpleFieldItem) holder;
                h.binding.setField((ViewModelField) items.get(position));
                h.binding.setCardclick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ActivityFieldPicker) fragment.getActivity()).returnLinkOrMention((ViewModelField) items.get(position));
                    }
                });
                break;
            }
        }
    }

    private void renewItems(ViewModelElementType element) {
        Log.d(TAG, "renewItems");
        items.clear();
        if (element == null) {
            for (String e : Session.getInstance().getSystemFromCache().getElements()) {
                items.add(Session.getInstance().getSystemFromCache().getElement(e));
            }
        } else {
            // element is not null, need to show catefories in the element
            for (String sc : element.getCategories()) {
                ViewModelCategory tmpC = element.getCategory(sc);
                items.add(tmpC);
                for (String sf : tmpC.getFields()) {
                    items.add(tmpC.getField(sf));
                }
            };
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderSimpleElementItem extends RecyclerView.ViewHolder {

        SimpleItemElementBinding binding;

        ViewHolderSimpleElementItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderSimpleCategoryItem extends RecyclerView.ViewHolder {

        SimpleItemCategoryBinding binding;

        ViewHolderSimpleCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderSimpleFieldItem extends RecyclerView.ViewHolder {

        SimpleItemFieldBinding binding;

        ViewHolderSimpleFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}
