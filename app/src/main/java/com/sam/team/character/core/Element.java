package com.sam.team.character.core;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Vaize
 */
public class Element<F extends Field, S extends RPSystem> extends BaseObservable {

    private static final String TAG = "Element";

    private String name, type;
    private S system;
    private TreeMap<String, TreeMap<String, F>> fields;

    //constructors
    public Element(S system) {
        name = type = null;
        fields = new TreeMap<>();
    }

    public Element(String name, String type, S system) {
        this.name = name;
        this.type = type;
        this.system = system;
        fields = new TreeMap<>();
    }

    //work with name
    public void setName(String name) {
        this.name = name;
    }

    @Bindable
    public String getName() {
        return name;
    }

    //work with type
    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getType() {
        return type; 
    }

    //work with fields
    public void addField(F field) {
        if (field.getCategory() == null || field.getName() == null) {
            Log.d(TAG, "Incorrect field format");
            return;
        }
        if (!fields.containsKey(field.getCategory())) {
            TreeMap<String, F> temp = new TreeMap<String, F>();
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
            Log.d(TAG, "Field with new category");
        } else {
            TreeMap<String, F> temp = fields.get(field.getCategory());
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
            Log.d(TAG, "Field with old category");
        }
    }

    public void removeField(String type, String name) {
        if (fields.containsKey(type)) {
            TreeMap<String, F> temp = fields.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }

    public F getField(String category, String name) {
        return fields.get(category).get(name);
    }

    public ArrayList<F> getFieldsByCategory(String category) {
        ArrayList<F> l = new ArrayList<>();
        if (fields.containsKey(category)) {
            for (Map.Entry<String, F> entry : fields.get(category).entrySet()) {
                l.add(entry.getValue());
            }
            return l;
        } else {
            return null;
        }
    }

    //work with cetegories
    public Set<String> getCategories() {
        return fields.keySet();
    }

    public Set getNamesByCategory(String category) {
        if (fields.containsKey(category)) {
            return fields.get(category).keySet();
        } else {
            return null;
        }
    }

    public S getSystem() {
        return system;
    }
}