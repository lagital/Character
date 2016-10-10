package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.FieldItemBinding;
import com.sam.team.character.viewmodel2.Field;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterField extends RecyclerView.Adapter<AdapterField.ViewHolderFieldItem> {

    private static final String TAG = "AdapterField";

    private ArrayList<Field> fields;
    private Context context;

    public AdapterField(Context context, ArrayList<Field> fields) {
        this.fields = fields;
        this.context = context;
    }

    @Override
    public ViewHolderFieldItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        FieldItemBinding binding = FieldItemBinding.inflate(inflater, parent, false);
        return new ViewHolderFieldItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderFieldItem holder, final int position) {
        holder.binding.setField(fields.get(position));
        // TODO: variable setting
        holder.binding.setVariable(1, 1);
        holder.binding.setCardclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit field " + Integer.toString(position));
                // TODO: changing category
            }
        });
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    class ViewHolderFieldItem extends RecyclerView.ViewHolder {

        FieldItemBinding binding;

        ViewHolderFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}