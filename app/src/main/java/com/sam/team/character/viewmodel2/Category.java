package com.sam.team.character.viewmodel2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.TreeMap;

/**
 * Created by pborisenko on 10/4/2016.
 */

public class Category extends BaseObservable {

    private String name;
    private TreeMap<String, TreeMap<String, Field>> fields;
    private Element element;

    //constructors
    public Category() {
        name = null;
        fields = new TreeMap<String, TreeMap<String, Field>>();
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
        if (field.getCategory() == null || field.getName() == null) return;
        if (!fields.containsKey(field.getCategory())) {
            TreeMap<String, Field> temp = new TreeMap<String, Field>();
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
        } else {
            TreeMap<String, Field> temp = fields.get(field.getCategory());
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
        }
    }
}
