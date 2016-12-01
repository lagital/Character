package com.sam.team.character.core;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Vaize
 */

@Root
public class Element<F extends Field, S extends RPSystem> extends BaseObservable {

    private String name;
    private ElementType type;
    @ElementList
    private ArrayList<F> fields;
    private Set<String> categories = new HashSet<>();

    private S system;

    //constructors
    public Element(String name, ElementType type, S system) {
        this.name = name;
        this.type = type;
        this.system = system;
        fields = new ArrayList<>();
    }

    //work with name
    @org.simpleframework.xml.Element
    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    @org.simpleframework.xml.Element
    public String getName() {
        return name;
    }

    //work with type
    @org.simpleframework.xml.Element
    public void setType(ElementType type) {
        this.type = type;
    }

    @Bindable
    @org.simpleframework.xml.Element
    public ElementType getType() {
        return type; 
    }

    //work with fields
    public void addField(F field) {
        if (field.getCategory() == null || field.getName() == null) {
            return;
        } else {
            // register new category
            if (!categories.contains(field.getCategory())) {
                categories.add(field.getCategory());
            }
            fields.add(field);
        }
    }

    public void removeField(Field.FieldType type, String name) {
        for (F f : fields) {
            if (f.getType() == type && f.getName().equals(name)){
                fields.remove(f);
            }
        }
    }

    public void removeField(String type, String name) {
        for (F f : fields) {
            if (f.getType() == Field.FieldType.valueOf(type) && f.getName().equals(name)){
                fields.remove(f);
            }
        }
    }

    public ArrayList<F> getFieldsByCategory(String category) {
        ArrayList<F> fl = new ArrayList<>();
        if (categories.contains(category)) {
            for (F f : fields) {
                if (f.getCategory().equals(category)) {
                    fl.add(f);
                }
            }
        }
        return fl;
    }

    //work with cetegories
    public Set<String> getCategories() {
        return categories;
    }

    public S getSystem() {
        return system;
    }

    public enum ElementType {
        CHARACTER_SHEET,
        SKILLS,
        ITEMS,
        OTHER
    }
}