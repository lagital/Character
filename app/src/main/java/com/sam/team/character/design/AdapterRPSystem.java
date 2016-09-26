package com.sam.team.character.design;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.databinding.RpsystemItemBinding;
import com.sam.team.character.viewmodel2.RPSystem;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

public class AdapterRPSystem extends RecyclerView.Adapter<AdapterRPSystem.ViewHolderRPSystemItem> {

    private ArrayList<RPSystem> systems;
    private Context context;

    public AdapterRPSystem (Context context, ArrayList<RPSystem> systems) {
        this.systems = systems;
    }

    @Override
    public ViewHolderRPSystemItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RpsystemItemBinding binding = RpsystemItemBinding.inflate(inflater, parent, false);
        return new ViewHolderRPSystemItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(ViewHolderRPSystemItem holder, int position) {
        holder.binding.setSystem(systems.get(position));
        holder.binding.setClick(new RPSystemClickHandler() {
            @Override
            public void onClick(View view) {
                //TODO: going inside the system
            }
        });
    }

    @Override
    public int getItemCount() {
        return systems.size();
    }

    public class ViewHolderRPSystemItem extends RecyclerView.ViewHolder {

        RpsystemItemBinding binding;

        public ViewHolderRPSystemItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }
}