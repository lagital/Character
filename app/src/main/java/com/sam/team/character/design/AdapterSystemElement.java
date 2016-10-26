package com.sam.team.character.design;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sam.team.character.R;
import com.sam.team.character.databinding.ItemElementBinding;
import com.sam.team.character.databinding.ItemRpsystemBinding;
import com.sam.team.character.viewmodel.Element;
import com.sam.team.character.viewmodel.ListItem;
import com.sam.team.character.viewmodel.RPSystem;
import com.sam.team.character.viewmodel.Session;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/26/2016.
 */

class AdapterSystemElement extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "AdapterSystemElement";

    private ArrayList<ListItem> items;
    private Context context;

    AdapterSystemElement(Context context, ArrayList<ListItem> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == ListItem.TYPE_SYSTEM) {
            ItemRpsystemBinding binding = ItemRpsystemBinding.inflate(inflater, parent, false);
            return new AdapterSystemElement.ViewHolderSystemItem(binding.getRoot());
        } else if (viewType == ListItem.TYPE_ELEMENT) {
            ItemElementBinding binding = ItemElementBinding.inflate(inflater, parent, false);
            return new AdapterSystemElement.ViewHolderElementItem(binding.getRoot());
        }

        ItemRpsystemBinding binding = ItemRpsystemBinding.inflate(inflater, parent, false);
        return new ViewHolderSystemItem(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == ListItem.TYPE_SYSTEM) {
            Log.d(TAG, "Bind system");
            AdapterSystemElement.ViewHolderSystemItem h = (AdapterSystemElement.ViewHolderSystemItem) holder;
            h.binding.setSystem((RPSystem) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Click on system " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem((RPSystem) items.get(position));
                }
            });
            h.binding.setShareclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: sharing the system
                    Log.d(TAG, "Sharing system " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem((RPSystem) items.get(position));
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit system " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem((RPSystem) items.get(position));

                    final LinearLayout l = (LinearLayout) View.inflate(context,
                            R.layout.dialog_new_element, null);
                    final EditText name = (EditText) l.findViewById(R.id.name);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(l);
                    builder.setTitle(R.string.new_element_dflt_name);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing, see dialog.getButton
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            EditText name = (EditText) l.findViewById(R.id.name);
                            String e_name = name.getText().toString();
                            if (!e_name.isEmpty() && !e_name.equalsIgnoreCase(
                                    context.getResources().getString(R.string.new_element_dflt_name))) {
                                // TODO: work with type
                                Session.getInstance().getCurrentSystem().addElement(
                                        new Element(e_name, "Test", Session.getInstance().getCurrentSystem()));
                                dialog.cancel();
                            } else {
                                Toast.makeText(context,
                                        context.getResources().getString(R.string.new_element_empty_name),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        } else if (type == ListItem.TYPE_ELEMENT) {
            Log.d(TAG, "Bind element");
            AdapterSystemElement.ViewHolderElementItem h = (AdapterSystemElement.ViewHolderElementItem) holder;
            h.binding.setElement((Element) items.get(position));
            h.binding.setCardclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: going inside the element
                    Log.d(TAG, "Click on element " + Integer.toString(position));
                }
            });
            h.binding.setEditclick(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "Edit element " + Integer.toString(position));
                    Session.getInstance().setCurrentSystem(((Element) items.get(position)).getRpSystem());
                    Intent intent = new Intent(context, ActivityEditElement.class);
                    intent.putExtra(ActivityEditElement.ELEMENT_NAME_EXTRA, ((Element) items.get(position)).getName());
                    intent.putExtra(ActivityEditElement.ELEMENT_TYPE_EXTRA, ((Element) items.get(position)).getType());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class ViewHolderSystemItem extends RecyclerView.ViewHolder {

        ItemRpsystemBinding binding;

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
}