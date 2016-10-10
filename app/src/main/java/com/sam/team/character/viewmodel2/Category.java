package com.sam.team.character.viewmodel2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pborisenko on 10/4/2016.
 */

public class Category extends BaseObservable implements ParentObject {

    private String name;
    private ArrayList<Field> fields;
    private Element element;

    //constructors
    public Category(String name, Element element) {
        this.name = name;
        this.element = element;
        this.fields = new ArrayList<>(element.getFieldsByCategory(name).values());
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
        Boolean b = false;
        if (field.getCategory() == null || field.getName() == null) return;
        for (Field f: fields) {
            if (!b) {
                b = f.getCategory().equals(field.getCategory());
            }
        }
        if (b) {
            fields.add(field);
            element.addField(field);
        }
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    @Override
    public List<Object> getChildObjectList() {
        return new ArrayList<Object>(fields);
    }

    @Override
    public void setChildObjectList(List<Object> list) {
        this.fields = new ArrayList<>(list.size());
        for (Object object : list) {
            fields.add((Field) object);
        }
    }
}
