package com.sam.team.character.viewmodel;

import android.databinding.Bindable;

import com.sam.team.character.core.Field;
import com.sam.team.character.design.ListItem;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class SysField extends Field implements ListItem {

    private static final String TAG = "SysField";

    public SysField(String category, String name, FieldType type, SysElement element) {
        super(category, name, type, element);
    }

    @Bindable
    public String getName() {
        return super.getName();
    }

    @Bindable
    public String getValue() {
        return super.getValue();
    }

    @Override
    public int getItemType() {
        return TYPE_FIELD;
    }
}
