package com.sam.team.character.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.sam.team.character.core.RPSystem;
import com.sam.team.character.corev2.SB_Field;
import com.sam.team.character.corev2.SB_System;
import com.sam.team.character.design.ApplicationMain;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class ViewModelSystem extends BaseObservable implements ListItem, ViewModelEnvelope<SB_System> {

    private static final String TAG = "ViewModelSystem";

    public static String SYSTEM_FILE_TYPE = "xml";

    private SB_System system;

    public ViewModelSystem (SB_System system) {
        this.system = system;
    }

    @Override
    public SB_System getContent() {
        return system;
    }

    @Override
    public void setContent(SB_System system) {
        this.system = system;
    }

    @Override
    public void save() {}

    @Override
    public void delete() {}

    @Bindable
    public String getName() {
        return system.getName();
    }

    public void setName(String name) {
        system.setName(name);
    }

    @Bindable
    public String getVersion() {
        return system.getVersion();
    }

    public void setVersion(String version) {
        system.setVersion(version);
    }

    @Bindable
    public String getCopyright() {
        return system.getCopyright();
    }

    public void setCopyright(String copyright) {
        system.setCopyright(copyright);
    }

    public File exportXML (String path) {
        return system.exportXML(path + "/" + system.getName() + "." + SYSTEM_FILE_TYPE);
        // TODO: catch exceptions on UI
    }

    public ArrayList<String> getElements () {
        return system.getElements();
    }

    public void addElement (String name) {
        system.addElement(name);
    }

    public void addField (String elementName, String categoryName, String fieldName) {
        system.addField(elementName, categoryName, fieldName);
    }

    public ArrayList<String> getFieldsInCategory (String elementName, String categoryName) {
        return system.getFieldsInCategory(elementName, categoryName);
    }

    @Override
    public int getItemType() {
        return TYPE_SYSTEM;
    }

    public static ArrayList<File> getAvailableSystems (Context context) {
        ArrayList<File> files = new ArrayList<>();
        if (ApplicationMain.isExternalStorageReadable()) {
            for (File f : context.getExternalFilesDir(null).listFiles()) {
                // find all rpg system files
                if (f.getName().endsWith(RPSystem.SYSTEM_FILE_TYPE)) {
                    files.add(f);
                    Log.d(TAG, "File " + f.getName() + " has been added chosen.");
                }
            }
        }
        return files;
    }
}
