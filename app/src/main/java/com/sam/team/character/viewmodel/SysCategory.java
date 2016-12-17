package com.sam.team.character.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.sam.team.character.core.Sheet;
import com.sam.team.character.core.Field;

/**
 * Created by pborisenko on 10/4/2016.
 */

public class SysCategory extends BaseObservable implements ListItem {

    private String name;
    private Sheet sheet;

    //constructors
    public SysCategory(String name, Sheet sheet) {
        this.name = name;
        this.sheet = sheet;
    }

    // for dummy categories
    public SysCategory(String name) {
        this.name = name;
    }

    //work with name
    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    //work with fields
    public void addField(Field field) {
        field.setCategory(this.name);
        sheet.addField(field);
    }

    @Override
    public int getItemType() {
        return TYPE_CATEGORY;
    }
}
