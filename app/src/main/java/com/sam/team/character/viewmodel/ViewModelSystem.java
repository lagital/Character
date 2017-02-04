package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.AsyncTask;
import android.util.Log;

import com.sam.team.character.BR;
import com.sam.team.character.core.SB_Field;
import com.sam.team.character.core.SB_System;
import com.sam.team.character.design.ApplicationMain;

import org.simpleframework.xml.Root;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * View-Model layer for communicating with UI
 * Created by pborisenko on 10/31/2016.
 */

@Root(name = "System")
public class ViewModelSystem extends SB_System<ViewModelSystem, ViewModelElementType, ViewModelCategory, ViewModelField>
        implements
        ListItem,
        ViewModelEnvelope,
        Observable,
        Serializable{

    private static final String TAG = "ViewModelSystem";

    public static String SYSTEM_FILE_TYPE = "rpg";

    private transient PropertyChangeRegistry callbacks;
    private String systemFilePath;

    public ViewModelSystem() {
        super();
    }

    public ViewModelSystem (String name, String version, String copyright) {
        super(name, version, copyright);
    }

    @Override
    public void save() {
        Log.d(TAG, "save");
        AsyncTaskFileSave task = new AsyncTaskFileSave(this);
        task.execute();
    }

    @Override
    public boolean delete() {
        Log.d(TAG, "delete");
        Session.getInstance().getSystemStorage().remove(this);
        return new File(getSystemFilePath()).delete();
    }

    @Bindable
    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
        // rename system file
        new File(systemFilePath).renameTo(new File(generateSystemFilePath()));
        this.systemFilePath = generateSystemFilePath();
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getVersion() {
        return super.getVersion();
    }

    public void setVersion(String version) {
        super.setVersion(version);
        notifyPropertyChanged(BR.version);
    }

    @Bindable
    public String getCopyright() {
        return super.getCopyright();
    }

    public void setCopyright(String copyright) {
        super.setCopyright(copyright);
        notifyPropertyChanged(BR.copyright);
    }

    public File exportSystemXML () {
        Log.d(TAG, "exportSystemXML");
        String xml = generateXML();
        File tmp = new File(getSystemFilePath());
        try {
            FileOutputStream fos = new FileOutputStream(tmp);
            fos.write(xml.getBytes());
            fos.close();
            return tmp;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<String> getElementNames() {
        return super.getElementNames();
    }

    public void addElement (String name) {
        try {
            super.addElement(ViewModelElementType.class, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyChange();
    }

    public void addField (String elementName, String categoryName, String fieldName, SB_Field.FieldType type) {
        try {
            super.addField(ViewModelField.class, elementName, categoryName, fieldName, type);
            getField(elementName, categoryName, fieldName).setValue("");
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifyChange();
    }

    public ArrayList<String> getFieldsInCategory (String elementName, String categoryName) {
        return super.getFieldsInCategory(elementName, categoryName);
    }

    @Override
    public int getItemType() {
        return TYPE_SYSTEM;
    }

    /*
     Change getters to avoid direct connection to Core entities
     */
    public ViewModelElementType getElement (String elementName) {
        ViewModelElementType tmp = new ViewModelElementType();
        try { tmp =  super.getElement(elementName); }
        catch(Exception e) {}
        return tmp;
    }

    public String getSystemFilePath() {
        if (systemFilePath == null) {
            systemFilePath = generateSystemFilePath();
        }
        return systemFilePath;
    }

    private String generateSystemFilePath() {
        Log.d(TAG, "generateSystemFilePath");
        Log.d(TAG, super.getName());
        if (ApplicationMain.getAppExternalStoragePath() != null) {
            return ApplicationMain.getAppExternalStoragePath()
                    + "/"
                    + getName()
                    + "." + SYSTEM_FILE_TYPE;
        } else {
            return ApplicationMain.getAppInternalStoragePath()
                    + "/"
                    + getName()
                    + "." + SYSTEM_FILE_TYPE;
        }
    }

    private class AsyncTaskFileSave extends AsyncTask<Void, Void, Void> {
        private static final String TAG = "AsyncTaskFileSave";
        private ViewModelSystem system;

        AsyncTaskFileSave(ViewModelSystem system) {
            this.system = system;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");
            exportSystemXML();
            return null;
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
