package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.sam.team.character.BR;
import com.sam.team.character.corev2.SB_ElementType;
import com.sam.team.character.corev2.SB_System;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class ViewModelElementType extends SB_ElementType implements
        ListItem,
        ViewModelEnvelope,
        Observable {

    private static final String TAG = "ViewModelElementType";

    private transient PropertyChangeRegistry callbacks;

    // dummy constructor, normally not used
    public ViewModelElementType(int index, String name, SB_System system) {
        super(index, name, system);
    }

    @Override
    public void save() {
        // save changes into System file
        getSystem().save();
    }

    @Override
    public boolean delete() {
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

    /*
     Change getters to avoid direct connection to Core entities
     */
    public ViewModelSystem getSystem() {
        return (ViewModelSystem) super.getSystem();
    }

    public ViewModelCategory getCategory(String name) {
        return (ViewModelCategory) super.getCategory(name);
    }

    public ViewModelField getField(String categoryName, String fieldName) {
        return (ViewModelField) super.getField(categoryName, fieldName);
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
