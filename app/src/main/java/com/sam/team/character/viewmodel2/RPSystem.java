
package com.sam.team.character.viewmodel2;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Vaize
 */
public class RPSystem extends BaseObservable {

    private static final String TAG = "RPSystem";

    private String name, version;
    private TreeMap<String, TreeMap<String, Element>> elements;

    //constructors
    public RPSystem() {
        name = version = null;
        elements = new TreeMap<String, TreeMap<String, Element>>();
    }

    public RPSystem(String name) {
        this.name = name;
        version = null;
        elements = new TreeMap<String, TreeMap<String, Element>>();
    }

    public RPSystem(String name, String version) {
        this.name = name;
        this.version = version;
        elements = new TreeMap<String, TreeMap<String, Element>>();
    }

    //work with name
    public void setName(String name) {
        this.name = name;
    }
    @Bindable
    public String getName() {
        return name;
    }

    //work with version
    public void setVersion(String version) {
        this.version = version;
    }
    @Bindable
    public String getVersion() {
        return version;
    }

    //work with elements
    public void addElement(Element element) {
        if (element.getType() == null || element.getName() == null) return;
        if (!elements.containsKey(element.getType())) {
            TreeMap<String, Element> temp = new TreeMap<String, Element>();
            temp.put(element.getName(), element);
            elements.put(element.getType(), temp);
        } else {
            TreeMap<String, Element> temp = elements.get(element.getType());
            temp.put(element.getName(), element);
            elements.put(element.getType(), temp);
        }
    }

    public void removeElement(String type, String name) {
        if (elements.containsKey(type)) {
            TreeMap<String, Element> temp = elements.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }

    public Element getElement(String type, String name) {
        return elements.get(type).get(name);
    }

    public Collection getElementsByType(String type) {
        if (elements.containsKey(type)) {
            return elements.get(type).values();
        } else {
            return null;
        }
    }

    public ArrayList<Element> getElements () {
        ArrayList<Element> el = new ArrayList<>();
        for (TreeMap<String, Element> tm : elements.values()) {
            for (String s : tm.keySet()) {
                el.add(tm.get(s));
            }
        }
            return el;
    }

    //work with types
    public Set getTypes() {
        return elements.keySet();
    }

    public Set getNamesByType(String type) {
        if (elements.containsKey(type)) {
            return elements.get(type).keySet();
        } else {
            return null;
        }
    }
}