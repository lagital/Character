
package com.sam.team.character.core;

import android.databinding.BaseObservable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Vaize
 */
public class RPSystem<E extends Element> extends BaseObservable {

    private String name, version, copyright;
    private TreeMap<String, TreeMap<String, E>> elements;

    // character constructor
    public RPSystem(String name) {
        this.name = name;
        version = null;
        copyright = null;
        elements = new TreeMap<>();
    }

    // system constructor
    public RPSystem(String name, String version, String copyright) {
        this.name = name;
        this.version = version;
        this.copyright = copyright;
        elements = new TreeMap<>();
    }

    //work with name
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    //work with version
    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    //work with copyright
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getCopyright() {
        return copyright;
    }

    //work with elements
    public void addElement(E element) {
        if (element.getType() == null || element.getName() == null) return;
        if (!elements.containsKey(element.getType())) {
            TreeMap<String, E> temp = new TreeMap<String, E>();
            temp.put(element.getName(), element);
            elements.put(element.getType(), temp);
        } else {
            TreeMap<String, E> temp = elements.get(element.getType());
            temp.put(element.getName(), element);
            elements.put(element.getType(), temp);
        }
    }

    public void removeElement(String type, String name) {
        if (elements.containsKey(type)) {
            TreeMap<String, E> temp = elements.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }

    public E getElement(String type, String name) {
        return elements.get(type).get(name);
    }

    public Collection getElementsByType(String type) {
        if (elements.containsKey(type)) {
            return elements.get(type).values();
        } else {
            return null;
        }
    }

    public ArrayList<E> getElements () {
        ArrayList<E> el = new ArrayList<>();
        for (TreeMap<String, E> tm : elements.values()) {
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