
package com.sam.team.character.viewmodel2;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Vaize
 */
public class Element {
    private String name, type;
    private TreeMap<String, TreeMap<String, Field>> fields;
    //constructors
    Element() {
        name = type = null;
        fields = new TreeMap<String, TreeMap<String, Field>>();
    }
    Element(String name, String type) {
        this.name = name;
        this.type = type;
        fields = new TreeMap<String, TreeMap<String, Field>>();
    }
    //work with name
    void setName(String name) {
        this.name = name;
    }
    String getName() {
        return name;
    }
    //work with type
    void setType(String type) {
        this.type = type;
    }
    String getType() {
        return type; 
    }
    //work with fields
    void addField(Field field) {
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
    void removeElement(String type, String name) {
        if (fields.containsKey(type)) {
            TreeMap<String, Field> temp = fields.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }
    Set getCategories() {
        return fields.keySet();
    }
    Set getNamesByCategory(String category) {
        if (fields.containsKey(category)) {
            return fields.get(category).keySet();
        } else {
            return null;
        }
    }
    Field getField(String category, String name) {
        return fields.get(category).get(name);
    }
    Collection getFieldsByCategory(String type) {
        if (fields.containsKey(type)) {
            return fields.get(type).values();
        } else {
            return null;
        }
    }    
}
