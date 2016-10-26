package com.sam.team.character.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

/**
 * Created by pborisenko on 10/4/2016.
 */

public class Category extends BaseObservable implements ListItem{

    private String name;
    private Element element;

    //constructors
    public Category(String name, Element element) {
        this.name = name;
        this.element = element;
    }

    // for dummy categories
    public Category(String name) {
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
        element.addField(field);
    }

    @Override
    public int getItemType() {
        return TYPE_CATEGORY;
    }
}
