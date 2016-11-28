package com.sam.team.character.viewmodel;

import android.content.Context;
import android.databinding.Bindable;
import android.util.Log;

import com.sam.team.character.core.RPSystem;
import com.sam.team.character.design.ApplicationMain;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class SysRPSystem extends RPSystem<SysElement> implements ListItem {
    private static final String TAG = "SysRPSystem";

    //constructors
    public SysRPSystem(String name, String version, String copyright) {
        super(name, version, copyright);
    }

    @Bindable
    public String getName() {
        return super.getName();
    }

    @Bindable
    public String getVersion() {
        return super.getVersion();
    }

    @Bindable
    public String getCopyright() {
        return super.getCopyright();
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
