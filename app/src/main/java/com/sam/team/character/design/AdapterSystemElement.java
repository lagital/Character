package com.sam.team.character.design;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sam.team.character.R;
import com.sam.team.character.databinding.ItemElementBinding;
import com.sam.team.character.databinding.ItemSystemBinding;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelSystem;

import java.io.File;
import java.util.ArrayList;

import static com.sam.team.character.viewmodel.ViewModelSystem.SYSTEM_FILE_TYPE;

/**
 * Adapter representing Systems and their child Elements.
 * Only template Elements are shown.
 * Created by pborisenko on 9/26/2016.
 */

class AdapterSystemElement extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterSystemElement";

    private ArrayList<ListItem> items;
    private FragmentSystemPicker fragment;

    AdapterSystemElement(FragmentSystemPicker fragment) {
        this.fragment = fragment;
        this.items = new ArrayList<>();
        renewItems();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_SYSTEM) {
            ItemSystemBinding binding = ItemSystemBinding.inflate(inflater, parent, false);
            return new AdapterSystemElement.ViewHolderSystemItem(binding.getRoot());
        } else if (viewType == ListItem.TYPE_ELEMENT) {
            ItemElementBinding binding = ItemElementBinding.inflate(inflater, parent, false);
            return new AdapterSystemElement.ViewHolderElementItem(binding.getRoot());
        }

        ItemSystemBinding binding = ItemSystemBinding.inflate(inflater, parent, false);
        return new ViewHolderSystemItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_SYSTEM) {
            AdapterSystemElement.ViewHolderSystemItem h = (AdapterSystemElement.ViewHolderSystemItem) holder;
            h.binding.setSystem((ViewModelSystem) items.get(position));
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit system " + Integer.toString(position));
                    Session.getInstance().cacheSystem((ViewModelSystem) items.get(position));
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
                                    tpl.add(new TextParameter("", ((ViewModelSystem) items.get(position)).getName(), true, null));
                                    tpl.add(new TextParameter("", ((ViewModelSystem) items.get(position)).getVersion(), false, null));
                                    tpl.add(new TextParameter("", ((ViewModelSystem) items.get(position)).getCopyright(), false, null));
                                    new TextParmsDialogBuilder(
                                            fragment.getActivity(),
                                            R.layout.dialog_settings_container,
                                            R.layout.dialog_settings_parameter,
                                            R.string.edit_system_dialog_title,
                                            tpl) {
                                        @Override
                                        void applySettings() {
                                            ((ViewModelSystem) items.get(position)).setName(getResults().get(0));
                                            ((ViewModelSystem) items.get(position)).setVersion(getResults().get(1));
                                            ((ViewModelSystem) items.get(position)).setCopyright(getResults().get(2));
                                            renewItems();
                                            notifyDataSetChanged();
                                        }
                                    };
                                    break;
                                }
                                case R.id.system_item_edit_menu_share: {
                                    File tmp = ((ViewModelSystem) items.get(position)).exportSystemXML();
                                    Uri fileUri = Uri.fromFile(tmp);
                                    if (fileUri != null) {

                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.setType("application/" + SYSTEM_FILE_TYPE);
                                        // temp permission for receiving app to read the file
                                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        //shareIntent.setDataAndType(fileUri,
                                         //       fragment.getActivity().getContentResolver().getType(fileUri));
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                                        fragment.startActivity(Intent.createChooser(shareIntent,
                                                fragment.getResources().getString(R.string.system_item_share_title)));
                                    }
                                    break;
                                }
                                case R.id.system_item_edit_menu_delete: {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                                    builder.setTitle(fragment.getResources().getString(R.string.dialog_are_you_sure));
                                    final AlertDialog alertDialog = builder.create();
                                    builder.setPositiveButton(R.string.dialog_btn_yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((ViewModelSystem) items.get(position)).delete();
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
            h.binding.setAddclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "New sheet for system " + Integer.toString(position));
                    ArrayList<TextParameter> tpl = new ArrayList<>();
                    final String[] isCharacterList = {fragment.getResources().getString(R.string.new_sheet_character_type),
                            fragment.getResources().getString(R.string.new_sheet_other_type)};
                    tpl.add(new TextParameter(fragment.getResources().getString(R.string.new_sheet_dflt_name), null, true, null));
                    tpl.add(new TextParameter(fragment.getResources().getString(R.string.new_sheet_dflt_type), null, true, isCharacterList));
                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                            fragment.getActivity(),
                            R.layout.dialog_settings_container,
                            R.layout.dialog_settings_parameter,
                            R.string.new_sheet_dialog_title,
                            tpl) {
                        @Override
                        void applySettings() {
                            ((ViewModelSystem) items.get(position)).addElement(getResults().get(0));
                            ((ViewModelSystem) items.get(position)).getElement(getResults().get(0)).setIsTemplate(true);
                            // user chooses - character sheet or not
                            if (getResults().get(1).equals(isCharacterList[0])) {
                                ((ViewModelSystem) items.get(position)).getElement(getResults().get(0)).setIsCharacter(true);
                            } else {
                                ((ViewModelSystem) items.get(position)).getElement(getResults().get(0)).setIsCharacter(false);
                            }
                            renewItems();
                            notifyDataSetChanged();
                        }
                    };
                    builder.getDialog().show();
                }
            });
        } else if (type == ListItem.TYPE_ELEMENT) {
            Log.d(TAG, "Bind sheet");
            AdapterSystemElement.ViewHolderElementItem h = (AdapterSystemElement.ViewHolderElementItem) holder;
            h.binding.setElement((ViewModelElementType) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit element " + Integer.toString(position));
                    // cache System and Element variables before going into settings
                    Session.getInstance().cacheSystem(((ViewModelElementType) items.get(position)).getSystem());
                    Session.getInstance().cacheElement(((ViewModelElementType) items.get(position)));
                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                            .FragmentType.EDIT_ELEMENT);
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu pm = new PopupMenu(fragment.getActivity(), view);
                    pm.getMenu().add(1, R.id.sheet_item_edit_menu_rename,  1, R.string.sheet_item_edit_menu_rename);
                    pm.getMenu().add(1, R.id.sheet_item_edit_menu_edit,    2, R.string.sheet_item_edit_menu_edit);
                    pm.getMenu().add(1, R.id.sheet_item_edit_menu_delete,  3, R.string.sheet_item_edit_menu_delete);
                    pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.sheet_item_edit_menu_rename: {
                                    ArrayList<TextParameter> tpl = new ArrayList<>();
                                    tpl.add(new TextParameter(fragment.getResources().getString(R.string.new_sheet_dflt_name), ((ViewModelElementType) items.get(position)).getName(), true, null));
                                    TextParmsDialogBuilder builder = new TextParmsDialogBuilder(
                                            fragment.getActivity(),
                                            R.layout.dialog_settings_container,
                                            R.layout.dialog_settings_parameter,
                                            R.string.edit_sheet_dialog_title,
                                            tpl) {
                                        @Override
                                        void applySettings() {
                                            ((ViewModelElementType) items.get(position)).setName(getResults().get(0));
                                            renewItems();
                                            notifyDataSetChanged();
                                        }
                                    };
                                    builder.getDialog().show();
                                    break;
                                }
                                case R.id.sheet_item_edit_menu_edit: {
                                    // cache System and Element variables before going into settings
                                    Session.getInstance().cacheSystem(((ViewModelElementType) items.get(position)).getSystem());
                                    Session.getInstance().cacheElement(((ViewModelElementType) items.get(position)));
                                    ((ActivityContainer) fragment.getActivity()).replaceFragment(ActivityContainer
                                            .FragmentType.EDIT_ELEMENT);
                                    break;
                                }
                                case R.id.sheet_item_edit_menu_delete: {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getActivity());
                                    builder.setTitle(fragment.getResources().getString(R.string.dialog_are_you_sure));
                                    final AlertDialog alertDialog = builder.create();
                                    builder.setPositiveButton(R.string.dialog_btn_yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((ViewModelElementType) items.get(position)).delete();
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
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderSystemItem extends RecyclerView.ViewHolder {

        ItemSystemBinding binding;

        ViewHolderSystemItem(View v) {
            super(v);
            binding = DataBindingUtil.bind(v);
        }
    }

    private class ViewHolderElementItem extends RecyclerView.ViewHolder {

        ItemElementBinding binding;

        ViewHolderElementItem(View v) {
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
                if (tmp.isTemplate()) {
                    items.add(s.getElement(se));
                }
            }
        }
        notifyDataSetChanged();
    }
}