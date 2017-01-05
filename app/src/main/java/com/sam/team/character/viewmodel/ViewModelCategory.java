package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;

import com.sam.team.character.BR;
import com.sam.team.character.corev2.SB_Category;

/**
 * Created by pborisenko on 10/4/2016.
 */

public class ViewModelCategory extends SB_Category implements
        ListItem,
        ViewModelEnvelope,
        Observable {

    private transient PropertyChangeRegistry callbacks;

    // dummy constructor, normally not used
    public ViewModelCategory() {
        super();
    }

    @Override
    public void save() {
        // create temporary Envelope to save changes into System file
        ((ViewModelSystem) getElement().getSystem()).save();
    }

    @Override
    public boolean delete() {
        getElement().removeCategory(getName());
        getElement().notifyChange();
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

    @Override
    public int getItemType() {
        return TYPE_CATEGORY;
    }

    /*
     Change getters to avoid direct connection to Core entities
     */
    public ViewModelElementType getElement() {
        return (ViewModelElementType) super.getElement();
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
