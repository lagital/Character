package com.sam.team.character.design;

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
import com.sam.team.character.databinding.ItemCategoryBinding;
import com.sam.team.character.databinding.ItemFieldBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelCategory;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelField;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterCategoryField extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterCategoryField";

    private ArrayList<ListItem> items;
    private FragmentEditElement fragment;

    AdapterCategoryField(FragmentEditElement fragment) {
        this.fragment = fragment;
        this.items = new ArrayList<>();
        renewItems();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_CATEGORY) {
            ItemCategoryBinding binding = ItemCategoryBinding.inflate(inflater, parent, false);
            return new ViewHolderCategoryItem(binding.getRoot());
        } else {
            ItemFieldBinding binding = ItemFieldBinding.inflate(inflater, parent, false);
            return new ViewHolderFieldItem(binding.getRoot());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_CATEGORY) {
            ViewHolderCategoryItem h = (ViewHolderCategoryItem) holder;
            h.binding.setCategory((ViewModelCategory) items.get(position));
            h.binding.setPlusclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Add field " + Integer.toString(position));
                    // cache Category variable before going into settings
                    Session.getInstance().cacheCategory(((ViewModelCategory) items.get(position)));
                    // clear cached field
                    Session.getInstance().cacheField(null);

                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                            .FragmentType.EDIT_FIELD);
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit category " + Integer.toString(position));
                    PopupMenu pm = new PopupMenu(fragment.getActivity(), view);
                    pm.getMenu().add(1, R.id.category_item_edit_menu_edit,   1, R.string.category_item_edit_menu_edit);
                    pm.getMenu().add(1, R.id.category_item_edit_menu_delete, 2, R.string.category_item_edit_menu_delete);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.category_item_edit_menu_edit: {
                                    ArrayList<TextParameter> tpl = new ArrayList<>();
                                    tpl.add(new TextParameter("", ((ViewModelCategory) items.get(position)).getName(), true));
                                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                                            fragment.getActivity(),
                                            R.layout.dialog_settings_container,
                                            R.layout.dialog_settings_parameter,
                                            R.string.edit_category_dialog_title,
                                            tpl) {
                                        @Override
                                        void applySettings() {
                                            ((ViewModelCategory) items.get(position)).setName(getResults().get(0));
                                            renewItems();
                                            notifyDataSetChanged();
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
                                            ((ViewModelCategory) items.get(position)).delete();
                                            renewItems();
                                            notifyDataSetChanged();
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
            ViewHolderFieldItem h = (ViewHolderFieldItem) holder;
            h.binding.setField((ViewModelField) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Click on field " + Integer.toString(position));
                    // cache field for further changing in fragment
                    Session.getInstance().cacheCategory(((ViewModelField) items.get(position)).getCategory());
                    Session.getInstance().cacheField((ViewModelField) items.get(position));

                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                            .FragmentType.EDIT_FIELD);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderCategoryItem extends RecyclerView.ViewHolder {

        ItemCategoryBinding binding;

        ViewHolderCategoryItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderFieldItem extends RecyclerView.ViewHolder {

        ItemFieldBinding binding;

        ViewHolderFieldItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }

    public void renewItems() {
        Log.d(TAG, "renewItems");
        items.clear();
        ViewModelElementType tmpE = Session.getInstance().getElementFromCache();
        for (String sc : tmpE.getCategories()) {
            ViewModelCategory tmpC = tmpE.getCategory(sc);
            items.add(tmpC);
            for (String sf : tmpC.getFields()) {
                items.add(tmpC.getField(sf));
            }
        };
    }
}