package com.sam.team.character.design;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.team.character.R;
import com.sam.team.character.viewmodel.DiceBag;
import com.sam.team.character.viewmodel.DrawerItem;
import com.sam.team.character.viewmodel.Session;
import com.sam.team.character.viewmodel.ViewModelElementType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.sam.team.character.viewmodel.DrawerItem.DrawerItemType.ADD_DICES;
import static com.sam.team.character.viewmodel.DrawerItem.DrawerItemType.TITLE;

/**
 * Adapter for character drawer menu.
 * Created by lagital on 22.01.17.
 */

public class AdapterCharacterDrawer extends ArrayAdapter<DrawerItem>
{
    private static final String TAG = "AdapterCharacterDrawer";

    private Context context;
    private DrawerItem data[] = null;
    private LayoutInflater inflater;

    public AdapterCharacterDrawer(Context context, int layoutResourceId, DrawerItem[] data)
    {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        inflater = ((AppCompatActivity) context).getLayoutInflater();
    }

    @NotNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        switch(getItemViewType(position)) {
            case 0: {
                ViewHolderTitle holder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.drawer_item_title, null);
                    holder = new ViewHolderTitle();
                    holder.titleTextView = (TextView) convertView.findViewById(R.id.drawer_item_title_text);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolderTitle) convertView.getTag();
                }
                holder.titleTextView.setText(data[position].getDrawerItemTitle());
                convertView.setEnabled(false); // titles are not clickable
                break;
            }
            case 1: {
                ViewHolderPage holder;
                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.drawer_item_page, null);
                    holder = new ViewHolderPage();
                    holder.titleTextView = (TextView) convertView.findViewById(R.id.drawer_item_page_text);
                    holder.iconImageView = (ImageView) convertView.findViewById(R.id.drawer_item_page_icon);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolderPage) convertView.getTag();
                }
                holder.titleTextView.setText(data[position].getDrawerItemTitle());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: get into another element
                    }
                });
                break;
            }
            case 2: {
                ViewHolderDiceBag holder;

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.drawer_item_dicebag, null);
                    holder = new ViewHolderDiceBag();
                    holder.titleTextView = (TextView) convertView.findViewById(R.id.drawer_item_dicebag_text);
                    holder.iconImageView = (ImageView) convertView.findViewById(R.id.drawer_item_dicebag_icon);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolderDiceBag) convertView.getTag();
                }
                holder.titleTextView.setText(data[position].getDrawerItemTitle());
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Snackbar snackBar = Snackbar.make(
                                ((Activity) context).getWindow().getDecorView(),
                                ((DiceBag) data[position]).shakeAsString(),
                                Snackbar.LENGTH_INDEFINITE);
                        snackBar.setAction(
                                context.getResources().getString(R.string.dialog_dismiss_roll),
                                new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                snackBar.dismiss();
                            }
                        });
                        snackBar.show();
                    }
                });
                break;
            }
            case 3: {
                ViewHolderAddDices holder;

                if (convertView == null) {
                    convertView = inflater.inflate(R.layout.drawer_item_add_dices, null);
                    holder = new ViewHolderAddDices();
                    holder.iconImageView = (ImageView) convertView.findViewById(R.id.drawer_item_add_dices);
                    convertView.setTag(holder);
                }
                else {
                    holder = (ViewHolderAddDices) convertView.getTag();
                }
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        final View dv = inflater.inflate(R.layout.dialog_dice_bag, null);
                        builder.setView(dv);
                        final EditTextEndCursor number = (EditTextEndCursor) dv.findViewById(R.id.dice_number);
                        final EditTextEndCursor power = (EditTextEndCursor) dv.findViewById(R.id.dice_power);
                        number.setOnTouchListener(new CleanOnTouchListener(number, null));
                        power.setOnTouchListener(new CleanOnTouchListener(power, null));
                        builder.setTitle(R.string.dialog_add_dicebag);
                        builder.setPositiveButton(R.string.dialog_btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int inumber = 0;
                                int ipower = 0;
                                if(!number.getText().toString().equals("")){
                                    inumber = Integer.parseInt(number.getText().toString());
                                }
                                if(!power.getText().toString().equals("")){
                                    ipower = Integer.parseInt(power.getText().toString());
                                }
                                if (inumber != 0 && ipower != 0) {
                                    Session.getInstance().getElementFromCache()
                                            .addDiceBag(new DiceBag(inumber, ipower));
                                    renewItems();
                                }
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });
                break;
            }
        }
        return convertView;
    }

    void renewItems() {
        Log.d(TAG, "renewItems");
        ArrayList<DrawerItem> tmp = new ArrayList<>();
        // add title
        tmp.add(new DrawerItem() {
            @Override
            public String getDrawerItemTitle() {
                return context.getResources().getString(R.string.drawer_group_pages);
            }

            @Override
            public DrawerItemType getDrawerItemType() {
                return TITLE;
            }
        });
        // add additional pages
        for (ViewModelElementType e : Session.getInstance().getSystemFromCache().getElements()) {
            if (!e.isCharacter() && !e.isTemplate()) {
                tmp.add(e);
            }
        }
        // add title
        tmp.add(new DrawerItem() {
            @Override
            public String getDrawerItemTitle() {
                return context.getResources().getString(R.string.drawer_group_dicebags);
            }

            @Override
            public DrawerItemType getDrawerItemType() {
                return TITLE;
            }
        });
        // add dice bags
        for (DiceBag db : Session.getInstance().getElementFromCache().getDiceBags()) {
            tmp.add(db);
        }
        // add 'Add dices' action
        tmp.add(new DrawerItem() {
            @Override
            public String getDrawerItemTitle() {return null;}

            @Override
            public DrawerItemType getDrawerItemType() {return ADD_DICES;}
        });

        data = tmp.toArray(new DrawerItem[tmp.size()]);
        Log.d(TAG, Integer.toString(data.length) + " " + Integer.toString(tmp.size()));
        notifyDataSetChanged();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        switch (data[position].getDrawerItemType()) {
            case TITLE:     return 0;
            case PAGE:      return 1;
            case DICEBAG:   return 2;
            case ADD_DICES: return 3;
            default:        return 0;
        }
    }

    @Override
    public int getCount() {
        return data.length;
    }

    private static class ViewHolderPage {
        private TextView titleTextView;
        private ImageView iconImageView;
    }

    private static class ViewHolderDiceBag {
        private TextView titleTextView;
        private ImageView iconImageView;
    }

    private static class ViewHolderTitle {
        private TextView titleTextView;
    }

    private static class ViewHolderAddDices {
        private ImageView iconImageView;
    }
}