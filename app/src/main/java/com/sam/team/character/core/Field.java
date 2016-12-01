
package com.sam.team.character.core;

import android.databinding.BaseObservable;

import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 *
 * @author Vaize
 */

@Root
public class Field<E extends Element> extends BaseObservable {

    private static final String TAG = "Field";

    private String name;
    private String category;
    private String typeStr;
    private String value;
    private String calcRule;
    private Boolean visible;
    private Boolean editable;

    private E element;
    private FieldType type;

    public Field(String category, String name, FieldType type, E element) {
        this.category = category;
        this.name = name;
        this.type = type;
        // for xml
        this.typeStr = type.name();
        this.element = element;
        value = calcRule = null;
    }

    public Field(String category, String name, String type, E element) {
        this.category = category;
        this.name = name;
        // from xml
        this.type = FieldType.valueOf(type);
        this.typeStr = type;
        this.element = element;
        value = calcRule = null;
    }

    @org.simpleframework.xml.Element
    public String getCategory() { return category; }

    @org.simpleframework.xml.Element
    public void setName(String name) { this.name = name; }

    @org.simpleframework.xml.Element
    public String getName() { return name; }

    @org.simpleframework.xml.Element
    public void setCategory(String category) { this.category = category; }

    @org.simpleframework.xml.Element
    public void setValue(String value) {
        if (type != FieldType.CALCULATED) {
            this.value = value;
        }
    }

    @org.simpleframework.xml.Element
    public String getValue() { return value; }

    public void removeValue(int id) { value = null; }

    @org.simpleframework.xml.Element(required = false)
    public void setRule(String rule) { calcRule = rule; }

    @org.simpleframework.xml.Element(required = false)
    public String getRule() { return calcRule; }

    public void removeRule() { calcRule = null; }

    public FieldType getType() {
        return type;
    }

    @org.simpleframework.xml.Element
    public String getTypeStr() {
        return type.name();
    }

    public E getElement() {
        return element;
    }

    public Boolean getEditable() {
        return editable;
    }

    public void setEditable(Boolean editable) {
        this.editable = editable;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public enum FieldType {
        SHORT_TEXT,
        LONG_TEXT,
        NUMERIC,
        CALCULATED
    }

    public static FieldType getTypeFromInt (int i) {
        switch (i) {
            case 1: return FieldType.SHORT_TEXT;
            case 2: return FieldType.LONG_TEXT;
            case 3: return FieldType.NUMERIC;
            case 4: return FieldType.CALCULATED;
            default: return FieldType.SHORT_TEXT;
        }
    }
}