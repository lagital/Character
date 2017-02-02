package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.net.Uri;
import android.util.Log;

import com.sam.team.character.BR;
import com.sam.team.character.core.SB_ElementType;
import com.sam.team.character.core.SB_Field;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import static com.sam.team.character.viewmodel.DrawerItem.DrawerItemType.PAGE;

/**
 * View-Model layer for communicating with UI
 * Created by pborisenko on 10/31/2016.
 */

@Root(name = "Element")
public class ViewModelElementType extends SB_ElementType<ViewModelSystem, ViewModelElementType, ViewModelCategory, ViewModelField>
        implements
        ListItem,
        DrawerItem,
        ViewModelEnvelope,
        Observable,
        Serializable {

    private static final String TAG = "ViewModelElementType";

    private transient PropertyChangeRegistry callbacks;

    @ElementList(name="DiceBags")
    private ArrayList<DiceBag> diceBags = new ArrayList<>();

    @Element(name="PhotoUri", required = false) private String photoUri = "";

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
        try { getSystem().removeElement(getName()); }
        catch(Exception e) {}
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
        ViewModelField tmp = new ViewModelField();
        try { tmp = super.getField(categoryName, fieldName); }
        catch(Exception e) {}
        return tmp;
    }

    public void addField(String categoryName, String fieldName, SB_Field.FieldType type, boolean... rewrite) throws Exception {
        try {
            super.addField(ViewModelField.class, categoryName, fieldName, type, rewrite);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<DiceBag> getDiceBags() {
        return diceBags;
    }

    public void addDiceBag(DiceBag bag) {
        diceBags.add(bag);
    }

    public void removeDiceBag(DiceBag bag) {
        diceBags.remove(bag);
    }

    public Uri getPhotoUri() {
        return Uri.parse(photoUri);
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri.toString();
    }

    @Override
    public int getItemType() {
        return TYPE_ELEMENT;
    }

    @Override
    public DrawerItemType getDrawerItemType() {
        return PAGE;
    }

    @Override
    public String getDrawerItemTitle() {
        return getName();
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
