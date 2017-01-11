package com.sam.team.character.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.util.Log;
import android.view.View;

import com.sam.team.character.R;
import com.sam.team.character.corev2.SB_Field;

import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by pborisenko on 10/31/2016.
 */

@Root(name="Field")
public class ViewModelField extends SB_Field <ViewModelSystem, ViewModelElementType, ViewModelCategory, ViewModelField>
        implements
        ListItem,
        ViewModelEnvelope,
        Observable,
        Serializable {

    private static final String TAG = "ViewModelField";

    private transient PropertyChangeRegistry callbacks;

    @Override
    public void save() {
        Log.d(TAG, "save");
        // create temporary Envelope to save changes into System file
        getCategory().getElement().getSystem().save();
    }

    @Override
    public boolean delete() {
        Log.d(TAG, "delete");
        getCategory().removeField(getName());
        getCategory().notifyChange();
        return true;
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

    @Override
    public void setValue(String value) {
        super.setValue(value);
        save();
    }

    /*
         Change getters to avoid direct connection to Core entities
         */
    public ViewModelCategory getCategory() {
        return super.getCategory();
    }

    public String getFieldTypeName (Context c) {
        return formatTypeToName(c, super.getType());
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

    public static int formatTypeToInt (SB_Field.FieldType t) {
        switch (t) {
            case SHORT_TEXT: {return 0;}
            case LONG_TEXT:  {return 1;}
            case NUMERIC:    {return 2;}
            case CALCULATED: {return 3;}
            default:         {return 0;}
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

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        if (callbacks == null) {
            callbacks = new PropertyChangeRegistry();
        }
        callbacks.add(onPropertyChangedCallback);
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback onPropertyChangedCallback) {
        if (callbacks != null) {
            callbacks.remove(onPropertyChangedCallback);
        }
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public synchronized void notifyChange() {
        if (callbacks != null) {
            callbacks.notifyCallbacks(this, 0, null);
            save();
        }
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with {@link Bindable} to generate a field in
     * <code>BR</code> to be used as <code>fieldId</code>.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        if (callbacks != null) {
            callbacks.notifyCallbacks(this, fieldId, null);
            save();
        }
    }
}
