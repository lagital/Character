package com.sam.team.character.viewmodel;

import android.databinding.Bindable;

import com.sam.team.character.core.Element;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/31/2016.
 */

public class SysElement extends Element<SysField, SysRPSystem> implements ListItem {

    private static final String TAG = "SysElement";

    public SysElement(String name, ElementType type, SysRPSystem system) {
        super(name, type, system);
    }

    @Bindable
    public String getName() {
        return super.getName();
    }

    @Bindable
    public ElementType getType() {
        return super.getType();
    }

    public ArrayList<SysField> getFieldsByCategory(String category) {
        return super.getFieldsByCategory(category);
    }

    @Override
    public int getItemType() {
        return TYPE_ELEMENT;
    }

    @Override
    public SysRPSystem getSystem() {
        return super.getSystem();
    }
}
