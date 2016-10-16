package com.sam.team.character.viewmodel2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.FileHandler;

/**
 *
 * @author Vaize
 */
public class Element extends BaseObservable {

    private static final String TAG = "Element";

    private String name, type;
    private TreeMap<String, TreeMap<String, Field>> fields;

    //constructors
    public Element() {
        name = type = null;
        fields = new TreeMap<String, TreeMap<String, Field>>();
    }

    public Element(String name, String type) {
        this.name = name;
        this.type = type;
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

    //work with type
    public void setType(String type) {
        this.type = type;
    }

    @Bindable
    public String getType() {
        return type; 
    }

    //work with fields
    public void addField(Field field) {
        if (field.getCategory() == null || field.getName() == null) {
            Log.d(TAG, "Incorrect field format");
            return;
        }
        if (!fields.containsKey(field.getCategory())) {
            TreeMap<String, Field> temp = new TreeMap<String, Field>();
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
            Log.d(TAG, "Field with new category");
        } else {
            TreeMap<String, Field> temp = fields.get(field.getCategory());
            temp.put(field.getName(), field);
            fields.put(field.getCategory(), temp);
            Log.d(TAG, "Field with old category");
        }
    }

    public void removeField(String type, String name) {
        if (fields.containsKey(type)) {
            TreeMap<String, Field> temp = fields.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }

    public Field getField(String category, String name) {
        return fields.get(category).get(name);
    }

    public ArrayList<Field> getFieldsByCategory(String category) {
        ArrayList<Field> l = new ArrayList<>();
        if (fields.containsKey(category)) {
            for (Map.Entry<String, Field> entry : fields.get(category).entrySet()) {
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
}