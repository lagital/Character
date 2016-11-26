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
import com.sam.team.character.databinding.ItemSyselementBinding;
import com.sam.team.character.databinding.ItemSysrpsystemBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysRPSystem;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterSystemElement extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterSystemElement";

    private ArrayList<ListItem> items;
    private FragmentSystemPicker fragment;

    AdapterSystemElement(FragmentSystemPicker fragment, ArrayList<ListItem> items) {
        this.items = items;
        this.fragment = fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_SYSTEM) {
            ItemSysrpsystemBinding binding = ItemSysrpsystemBinding.inflate(inflater, parent, false);
            return new AdapterSystemElement.ViewHolderSysRPSystemItem(binding.getRoot());
        } else if (viewType == ListItem.TYPE_ELEMENT) {
            ItemSyselementBinding binding = ItemSyselementBinding.inflate(inflater, parent, false);
            return new AdapterSystemElement.ViewHolderSysElementItem(binding.getRoot());
        }

        ItemSysrpsystemBinding binding = ItemSysrpsystemBinding.inflate(inflater, parent, false);
        return new ViewHolderSysRPSystemItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_SYSTEM) {
            AdapterSystemElement.ViewHolderSysRPSystemItem h = (AdapterSystemElement.ViewHolderSysRPSystemItem) holder;
            h.binding.setSystem((SysRPSystem) items.get(position));
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit system " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem((SysRPSystem) items.get(position));
                    PopupMenu pm = new PopupMenu(fragment.getActivity(), view);
                    pm.getMenu().add(1, R.id.system_item_edit_menu_edit,   1, R.string.system_item_edit_menu_edit);
                    pm.getMenu().add(1, R.id.system_item_edit_menu_share,  2, R.string.system_item_edit_menu_share);
                    pm.getMenu().add(1, R.id.system_item_edit_menu_delete, 3, R.string.system_item_edit_menu_delete);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(final MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.system_item_edit_menu_edit: {
                                    // show dialog to pass fill new system parameters.
                                    ArrayList<TextParameter> tpl = new ArrayList<>();
                                    tpl.add(new TextParameter("", Session.getInstance().getCurrentSystem().getName(), true));
                                    tpl.add(new TextParameter("", Session.getInstance().getCurrentSystem().getVersion(), false));
                                    tpl.add(new TextParameter("", Session.getInstance().getCurrentSystem().getCopyright(), false));
                                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                                            fragment.getActivity(),
                                            R.layout.dialog_settings_container,
                                            R.layout.dialog_settings_parameter,
                                            R.string.edit_system_dialog_title,
                                            tpl) {
                                        @Override
                                        void applySettings() {
                                            Session.getInstance().getCurrentSystem().setName(getResults().get(0));
                                            Session.getInstance().getCurrentSystem().setVersion(getResults().get(1));
                                            Session.getInstance().getCurrentSystem().setCopyright(getResults().get(2));
                                            fragment.fillList();
                                        }
                                    };
                                    break;
                                }
                                case R.id.system_item_edit_menu_share: {
                                    // TODO: share system
                                    break;
                                }
                                case R.id.system_item_edit_menu_delete: {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                                    builder.setTitle(fragment.getResources().getString(R.string.dialog_are_you_sure));
                                    final AlertDialog alertDialog = builder.create();
                                    builder.setPositiveButton(R.string.dialog_btn_yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // TODO: deleting system
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
            h.binding.setAddclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "New element for system " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem((SysRPSystem) items.get(position));
                    ArrayList<TextParameter> tpl = new ArrayList<>();
                    tpl.add(new TextParameter(fragment.getResources().getString(R.string.new_element_dflt_name), null, true));
                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                            fragment.getActivity(),
                            R.layout.dialog_settings_container,
                            R.layout.dialog_settings_parameter,
                            R.string.new_element_dialog_title,
                            tpl) {
                        @Override
                        void applySettings() {
                            // TODO: work with element types
                            Session.getInstance().getCurrentSystem().addElement(
                                    new SysElement(getResults().get(0), "CHARACTER",
                                            Session.getInstance().getCurrentSystem()));
                            fragment.fillList();
                        }
                    };
                    builder.getDialog().show();
                }
            });
        } else if (type == ListItem.TYPE_ELEMENT) {
            Log.d(TAG, "Bind element");
            Session.getInstance().cacheElement((SysElement) items.get(position));
            AdapterSystemElement.ViewHolderSysElementItem h = (AdapterSystemElement.ViewHolderSysElementItem) holder;
            h.binding.setElement(Session.getInstance().getElementFromCache());
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit element " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem(Session.getInstance().getElementFromCache().getSystem());
                    Session.getInstance().cacheElement(Session.getInstance().getElementFromCache());
                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                            .FragmentType.EDIT_ELEMENT);
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Session.getInstance().setCurrentSystem(Session.getInstance().getElementFromCache().getSystem());
                    Session.getInstance().cacheElement(Session.getInstance().getElementFromCache());
                    PopupMenu pm = new PopupMenu(fragment.getActivity(), view);
                    pm.getMenu().add(1, R.id.element_item_edit_menu_rename,  1, R.string.element_item_edit_menu_rename);
                    pm.getMenu().add(1, R.id.element_item_edit_menu_edit,    2, R.string.element_item_edit_menu_edit);
                    pm.getMenu().add(1, R.id.element_item_edit_menu_delete,  3, R.string.element_item_edit_menu_delete);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.element_item_edit_menu_rename: {
                                    ArrayList<TextParameter> tpl = new ArrayList<>();
                                    tpl.add(new TextParameter(fragment.getResources().getString(R.string.new_element_dflt_name), Session.getInstance().getElementFromCache().getName(), true));
                                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                                            fragment.getActivity(),
                                            R.layout.dialog_settings_container,
                                            R.layout.dialog_settings_parameter,
                                            R.string.edit_element_dialog_title,
                                            tpl) {
                                        @Override
                                        void applySettings() {
                                            Session.getInstance().getElementFromCache().setName(getResults().get(0));
                                            fragment.fillList();
                                        }
                                    };
                                    builder.getDialog().show();
                                    break;
                                }
                                case R.id.element_item_edit_menu_edit: {
                                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                                            .FragmentType.EDIT_ELEMENT);
                                    break;
                                }
                                case R.id.element_item_edit_menu_delete: {
                                    Session.getInstance().getCurrentSystem().removeElement(
                                            Session.getInstance().getElementFromCache().getType(),
                                            Session.getInstance().getElementFromCache().getName());
                                    fragment.fillList();
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                    pm.show();

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderSysRPSystemItem extends RecyclerView.ViewHolder {

        ItemSysrpsystemBinding binding;

        ViewHolderSysRPSystemItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderSysElementItem extends RecyclerView.ViewHolder {

        ItemSyselementBinding binding;

        ViewHolderSysElementItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getItemType();
    }
}