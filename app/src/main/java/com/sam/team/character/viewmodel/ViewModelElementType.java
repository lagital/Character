package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.util.Log;

import com.sam.team.character.BR;
import com.sam.team.character.corev2.SB_ElementType;

import java.io.Serializable;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class ViewModelElementType extends SB_ElementType<ViewModelSystem, ViewModelElementType, ViewModelCategory, ViewModelField>
        implements
        ListItem,
        ViewModelEnvelope,
        Observable,
        Serializable {

    private static final String TAG = "ViewModelElementType";

    private transient PropertyChangeRegistry callbacks;

    // dummy constructor, normally not used
    public ViewModelElementType(int index, String name, ViewModelSystem system) {
        super(index, name, system);
    }

    public ViewModelElementType() {
        super();
    }

    @Override
    public void save() {
        Log.d(TAG, "save");
        // save changes into System file
        getSystem().save();
    }

    @Override
    public boolean delete() {
        Log.d(TAG, "delete");
        getSystem().removeElement(getName());
        getSystem().notifyChange();
        return true;
    }

    @Bindable
    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
        notifyPropertyChanged(BR.name);
    }

    public void addCategory(String categoryName, boolean... rewrite) {
        try {
            super.addCategory(ViewModelCategory.class, categoryName, rewrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ViewModelField getField(String categoryName, String fieldName) {
        return super.getField(categoryName, fieldName);
    }

    public void addField(String categoryName, String fieldName, boolean... rewrite) throws Exception {
        try {
            super.addField(ViewModelField.class, categoryName, fieldName, rewrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemType() {
        return TYPE_ELEMENT;
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
