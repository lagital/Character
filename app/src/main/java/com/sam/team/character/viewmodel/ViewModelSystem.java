package com.sam.team.character.viewmodel;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.os.AsyncTask;

import com.sam.team.character.BR;
import com.sam.team.character.corev2.SB_System;
import com.sam.team.character.design.ApplicationMain;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class ViewModelSystem extends SB_System implements
        ListItem,
        ViewModelEnvelope,
        Observable {

    private static final String TAG = "ViewModelSystem";

    public static String SYSTEM_FILE_TYPE = "rpg";

    private transient PropertyChangeRegistry callbacks;
    private String systemFilePath;

    public ViewModelSystem (String name, String version, String copyright) {
        super(name, version, copyright);
        this.systemFilePath = generateSystemFilePath();
    }

    @Override
    public void save() {
        AsyncTaskFileSave task = new AsyncTaskFileSave(this);
        task.execute();
    }

    @Override
    public boolean delete() {
        Session.getInstance().getSystemStorage().remove(this);
        return new File(systemFilePath).delete();
    }

    @Bindable
    public String getName() {
        return super.getName();
    }

    public void setName(String name) {
        super.setName(name);
        // rename system file
        Boolean result = new File(systemFilePath).renameTo(new File(generateSystemFilePath()));
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

    public File exportXML (String path) {
        return super.exportXML(path + "/" + getName() + "." + SYSTEM_FILE_TYPE);
    }

    public ArrayList<String> getElements () {
        return super.getElements();
    }

    public void addElement (String name) {
        super.addElement(name);
        notifyChange();
    }

    public void addField (String elementName, String categoryName, String fieldName) {
        super.addField(elementName, categoryName, fieldName);
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
        return (ViewModelElementType) super.getElement(elementName);
    }

    public String getSystemFilePath() {
        return systemFilePath;
    }

    private String generateSystemFilePath() {
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

        private ViewModelSystem system;

        AsyncTaskFileSave(ViewModelSystem system) {
            this.system = system;
        }

        @Override
        protected Void doInBackground(Void... params) {
            system.exportXML(system.systemFilePath);
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
