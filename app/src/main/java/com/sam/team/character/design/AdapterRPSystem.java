package com.sam.team.character.design;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.ItemRpsystemBinding;
import com.sam.team.character.viewmodel2.RPSystem;
import com.sam.team.character.viewmodel2.Session;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterRPSystem extends RecyclerView.Adapter<AdapterRPSystem.ViewHolderRPSystemItem> {

    private static final String TAG = "AdapterRPSystem";

    private ArrayList<RPSystem> systems;
    private Context context;

    AdapterRPSystem (Context context, ArrayList<RPSystem> systems) {
        this.systems = systems;
        this.context = context;
    }

    @Override
    public ViewHolderRPSystemItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemRpsystemBinding binding = ItemRpsystemBinding.inflate(inflater, parent, false);
        return new ViewHolderRPSystemItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderRPSystemItem holder, final int position) {
        holder.binding.setSystem(systems.get(position));
        holder.binding.setCardclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: going inside the system
                Log.d(TAG, "Click on system " + Integer.toString(position));
                Session.getInstance().setCurrentSystem(systems.get(position));
            }
        });
        holder.binding.setShareclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: sharing the system
                Log.d(TAG, "Sharing system " + Integer.toString(position));
                Session.getInstance().setCurrentSystem(systems.get(position));
            }
        });
        holder.binding.setEditclick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Edit system " + Integer.toString(position));
                Session.getInstance().setCurrentSystem(systems.get(position));
                Intent intent = new Intent(context, ActivityElementPicker.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return systems.size();
    }

    class ViewHolderRPSystemItem extends RecyclerView.ViewHolder {

        ItemRpsystemBinding binding;

        ViewHolderRPSystemItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}