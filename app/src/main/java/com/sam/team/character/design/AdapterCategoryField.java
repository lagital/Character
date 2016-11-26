package com.sam.team.character.design;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.databinding.ItemSyscategoryBinding;
import com.sam.team.character.databinding.ItemSysfieldBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.SysCategory;
import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysField;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCategoryField extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterCategoryField";

    private ArrayList<ListItem> items;
    private FragmentEditElement fragment;

    AdapterCategoryField(FragmentEditElement fragment, ArrayList<ListItem> items) {
        this.items = items;
        this.fragment = fragment;
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
                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                            .FragmentType.EDIT_FIELD);
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit category " + Integer.toString(position));
                    Session.getInstance().cacheCategory(((SysCategory) items.get(position)).getName());
                    PopupMenu pm = new PopupMenu(fragment.getActivity(), view);
                    pm.getMenu().add(1, R.id.category_item_edit_menu_edit,   1, R.string.category_item_edit_menu_edit);
                    pm.getMenu().add(1, R.id.category_item_edit_menu_delete, 2, R.string.category_item_edit_menu_delete);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.category_item_edit_menu_edit: {
                                    ArrayList<TextParameter> tpl = new ArrayList<>();
                                    tpl.add(new TextParameter("", Session.getInstance().getCategoryFromCache(), true));
                                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                                            fragment.getActivity(),
                                            R.layout.dialog_settings_container,
                                            R.layout.dialog_settings_parameter,
                                            R.string.edit_category_dialog_title,
                                            tpl) {
                                        @Override
                                        void applySettings() {
                                            for (SysField f : Session.getInstance().getElementFromCache()
                                                    .getFieldsByCategory(Session.getInstance().getCategoryFromCache())) {
                                                f.setCategory(getResults().get(0));
                                            }
                                            fragment.fillList();
                                        }
                                    };
                                    builder.getDialog().show();
                                    break;
                                }

                                case R.id.category_item_edit_menu_delete: {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                                    builder.setTitle(fragment.getResources().getString(R.string.dialog_are_you_sure));
                                    final AlertDialog alertDialog = builder.create();
                                    builder.setPositiveButton(R.string.dialog_btn_yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            for (SysField f : Session.getInstance().getElementFromCache()
                                                    .getFieldsByCategory(Session.getInstance().getCategoryFromCache())) {
                                            Session.getInstance().getElementFromCache().removeField(f.getTypeStr(), f.getName());
                                            }
                                            fragment.fillList();
                                            alertDialog.dismiss();
                                        }
                                    });
                                    builder.setNegativeButton(R.string.dialog_btn_no, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                    pm.show();
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