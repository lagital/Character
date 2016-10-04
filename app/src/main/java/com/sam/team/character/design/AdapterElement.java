package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.ElementItemBinding;
import com.sam.team.character.viewmodel2.Element;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterElement extends RecyclerView.Adapter<AdapterElement.ViewHolderElementItem> {

    private static final String TAG = "AdapterElement";

    private ArrayList<Element> elements;
    private Context context;

    AdapterElement (Context context, ArrayList<Element> systems) {
        this.elements = systems;
        this.context = context;
    }

    @Override
    public ViewHolderElementItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ElementItemBinding binding = ElementItemBinding.inflate(inflater, parent, false);
        return new ViewHolderElementItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderElementItem holder, final int position) {
        holder.binding.setElement(elements.get(position));
        holder.binding.setCardclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: going inside the element
                Log.d(TAG, "Click on element " + Integer.toString(position));
            }
        });
        holder.binding.setEditclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit element " + Integer.toString(position));
                // TODO: changing element
            }
        });
    }

    @Override
    public int getItemCount() {
        return elements.size();
    }

    class ViewHolderElementItem extends RecyclerView.ViewHolder {

        ElementItemBinding binding;

        ViewHolderElementItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}