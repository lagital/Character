package com.sam.team.character.viewmodel;

import android.databinding.Bindable;

import com.sam.team.character.core.RPSystem;

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
}
