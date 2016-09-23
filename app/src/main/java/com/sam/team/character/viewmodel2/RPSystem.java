
package com.sam.team.character.viewmodel2;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Vaize
 */
public class RPSystem {
    private String name, version;
    private TreeMap<String, TreeMap<String, Element>> elements;
    //constructors
    RPSystem() {
        name = version = null;
        elements = new TreeMap<String, TreeMap<String, Element>>();
    }
    RPSystem(String name) {
        this.name = name;
        version = null;
        elements = new TreeMap<String, TreeMap<String, Element>>();
    }
    RPSystem(String name, String version) {
        this.name = name;
        this.version = version;
        elements = new TreeMap<String, TreeMap<String, Element>>();
    }
    //work with name
    void setName(String name) { 
        this.name = name;
    }
    String getName() { 
        return name;
    }
    //work with version
    void setVersion(String version) {
        this.version = version;
    }
    String getVersion() { 
        return version;
    }
    //work with elements
    void addElement(Element element) {
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
    void removeElement(String type, String name) {
        if (elements.containsKey(type)) {
            TreeMap<String, Element> temp = elements.get(type);
            if (temp.containsKey(name)) temp.remove(name);
        }
    }
    Set getTypes() {
        return elements.keySet();
    }
    Set getNamesByType(String type) {
        if (elements.containsKey(type)) {
            return elements.get(type).keySet();
        } else {
            return null;
        }
    }
    Element getElement(String type, String name) {
        return elements.get(type).get(name);
    }
    Collection getElementsByType(String type) {
        if (elements.containsKey(type)) {
            return elements.get(type).values();
        } else {
            return null;
        }
    }
}