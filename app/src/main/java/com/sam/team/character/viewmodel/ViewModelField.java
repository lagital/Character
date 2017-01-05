package com.sam.team.character.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.sam.team.character.R;
import com.sam.team.character.corev2.SB_Field;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class ViewModelField extends BaseObservable implements ListItem, ViewModelEnvelope<SB_Field> {

    private static final String TAG = "ViewModelField";

    private SB_Field field;

    public ViewModelField (SB_Field field) {
        this.field = field;
    }

    @Override
    public SB_Field getContent() {
        return field;
    }

    @Override
    public void setContent(SB_Field field) {
        this.field = field;
    }

    @Override
    public void save() {}

    @Override
    public void delete() {}

    @Bindable
    public String getName() {
        return field.getName();
    }

    @Bindable
    public String getValue() {
        return field.getValue();
    }

    @Override
    public int getItemType() {
        return TYPE_FIELD;
    }

    public String getFieldTypeName (Context c) {
        return formatTypeToName(c, field.getType());
    }

    public static String formatTypeToName (Context c, SB_Field.FieldType t) {
        switch (t) {
            case SHORT_TEXT: {return c.getResources().getString(R.string.field_type_short_text);}
            case LONG_TEXT:  {return c.getResources().getString(R.string.field_type_long_text);}
            case NUMERIC:    {return c.getResources().getString(R.string.field_type_numeric);}
            case CALCULATED: {return c.getResources().getString(R.string.field_type_calculated);}
            default:         {return c.getResources().getString(R.string.field_type_short_text);}
        }
    }

    public static SB_Field.FieldType formatIntToType (int i) {
        switch (i) {
            case 0: {return SB_Field.FieldType.SHORT_TEXT;}
            case 1: {return SB_Field.FieldType.LONG_TEXT;}
            case 2: {return SB_Field.FieldType.NUMERIC;}
            case 3: {return SB_Field.FieldType.CALCULATED;}
            default: {return SB_Field.FieldType.SHORT_TEXT;}
        }
    }
}
