package com.sam.team.character.viewmodel;

import android.content.Context;
import android.databinding.Bindable;

import com.sam.team.character.R;
import com.sam.team.character.core.Field;

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

    public String getFieldTypeName (Context c) {
        return formatTypeToName(c, getType());
    }

    public static String formatTypeToName (Context c, FieldType t) {
        switch (t) {
            case SHORT_TEXT: {return c.getResources().getString(R.string.field_type_short_text);}
            case LONG_TEXT:  {return c.getResources().getString(R.string.field_type_long_text);}
            case NUMERIC:    {return c.getResources().getString(R.string.field_type_numeric);}
            case CALCULATED: {return c.getResources().getString(R.string.field_type_calculated);}
            default:         {return c.getResources().getString(R.string.field_type_short_text);}
        }
    }
}
