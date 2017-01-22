package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.util.Log;

import com.sam.team.character.BR;
import com.sam.team.character.core.SB_Category;

import org.simpleframework.xml.Root;

import java.io.Serializable;

/**
 * Created by pborisenko on 10/4/2016.
 */

@Root(name="Category")
public class ViewModelCategory extends SB_Category <ViewModelSystem, ViewModelElementType, ViewModelCategory, ViewModelField>
        implements
        ListItem,
        ViewModelEnvelope,
        Observable,
        Serializable {

    private static final String TAG = "ViewModelCategory";

    private transient PropertyChangeRegistry callbacks;

    // dummy constructor, normally not used
    public ViewModelCategory() {
        super();
    }

    @Override
    public void save() {
        Log.d(TAG, "save");
        // create temporary Envelope to save changes into System file
        getElement().getSystem().save();
    }

    @Override
    public boolean delete() {
        Log.d(TAG, "delete");
        try {
            getElement().removeCategory(getName());
            return true;
        } catch(Exception e) { return false; }
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
        return super.getElement();
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
