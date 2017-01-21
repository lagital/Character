package com.sam.team.character.design;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.databinding.ItemCharacterBinding;
import com.sam.team.character.databinding.ItemSimpleSystemBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelSystem;

import java.util.ArrayList;

/**
 * Adapter representing Systems and their child Elements.
 * Only Characters (Elements with corresponding type) are shown.
 * Created by pborisenko on 01/15/2017.
 */

class AdapterSystemCharacter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterSystemCharacter";

    private ArrayList<ListItem> items;
    private FragmentLoadSystems fragment;

    AdapterSystemCharacter(FragmentLoadSystems fragment) {
        this.fragment = fragment;
        this.items = new ArrayList<>();
        renewItems();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_SYSTEM) {
            ItemSimpleSystemBinding binding = ItemSimpleSystemBinding.inflate(inflater, parent, false);
            return new AdapterSystemCharacter.ViewHolderSimpleSystemItem(binding.getRoot());
        } else if (viewType == ListItem.TYPE_ELEMENT) {
            ItemCharacterBinding binding = ItemCharacterBinding.inflate(inflater, parent, false);
            return new ViewHolderCharacterItem(binding.getRoot());
        }

        ItemSimpleSystemBinding binding = ItemSimpleSystemBinding.inflate(inflater, parent, false);
        return new ViewHolderSimpleSystemItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_SYSTEM) {
            AdapterSystemCharacter.ViewHolderSimpleSystemItem h = (AdapterSystemCharacter.ViewHolderSimpleSystemItem) holder;
            h.binding.setSystem((ViewModelSystem) items.get(position));
            h.binding.setAddclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "New character for system " + Integer.toString(position));
                    ArrayList<TextParameter> tpl = new ArrayList<>();
                    tpl.add(new TextParameter(fragment.getResources().getString(R.string.new_character_dflt_name), null, true, null));
                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                            fragment.getActivity(),
                            R.layout.dialog_settings_container,
                            R.layout.dialog_settings_parameter,
                            R.string.load_systems_add_character,
                            tpl) {
                        @Override
                        void applySettings() {
                            ((ViewModelSystem) items.get(position)).addElement(getResults().get(0));
                            ((ViewModelSystem) items.get(position)).getElement(getResults().get(0)).setIsCharacter(true);
                            ((ViewModelSystem) items.get(position)).getElement(getResults().get(0)).setIsTemplate(false);
                            renewItems();
                            notifyDataSetChanged();
                        }
                    };
                    builder.getDialog().show();
                }
            });
        } else if (type == ListItem.TYPE_ELEMENT) {
            Log.d(TAG, "Bind character");
            ViewHolderCharacterItem h = (ViewHolderCharacterItem) holder;
            h.binding.setElement((ViewModelElementType) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Go to character " + Integer.toString(position));
                    // cache System and Element variables before going into settings
                    Session.getInstance().cacheSystem(((ViewModelElementType) items.get(position)).getSystem());
                    Session.getInstance().cacheElement(((ViewModelElementType) items.get(position)));
                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                            .FragmentType.CHARACTER);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderSimpleSystemItem extends RecyclerView.ViewHolder {

        ItemSimpleSystemBinding binding;

        ViewHolderSimpleSystemItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderCharacterItem extends RecyclerView.ViewHolder {

        ItemCharacterBinding binding;

        ViewHolderCharacterItem(View v) {
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
        for (ViewModelSystem s : Session.getInstance().getSystemStorage()) {
            items.add(s);
            for (String se : s.getElements()) {
                ViewModelElementType tmp = s.getElement(se);
                if (tmp.isCharacter() && !tmp.isTemplate()) {
                    items.add(tmp);
                }
            }
        }
    }
}