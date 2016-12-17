
package com.sam.team.character.core;

import android.databinding.BaseObservable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author Vaize
 */

@Root
public class Field<S extends Sheet> extends BaseObservable {

    private static final String TAG = "Field";

    private String name;
    private String category;
    private String value;
    private String calcRule;
    private Boolean visible;
    private Boolean editable;

    private S sheet;
    private FieldType type;

    public Field(String category, String name, FieldType type, S sheet) {
        this.category = category;
        this.name = name;
        this.type = type;
        this.sheet = sheet;
        value = calcRule = null;
    }

    public Field(String category, String name, String type, S sheet) {
        this.category = category;
        this.name = name;
        // from xml
        this.type = FieldType.valueOf(type);
        this.sheet = sheet;
        value = calcRule = null;
    }

    @Element
    public String getCategory() { return category; }

    @Element
    public void setName(String name) { this.name = name; }

    @Element
    public String getName() { return name; }

    @Element
    public void setCategory(String category) { this.category = category; }

    @Element
    public void setValue(String value) {
        if (type != FieldType.CALCULATED) {
            this.value = value;
        }
    }

    @Element
    public String getValue() { return value; }

    public void removeValue(int id) { value = null; }

    @Element(required = false)
    public void setRule(String rule) { calcRule = rule; }

    @Element(required = false)
    public String getRule() { return calcRule; }

    public void removeRule() { calcRule = null; }

    public FieldType getType() {
        return type;
    }

    @Element
    public String getTypeStr() {
        return type.name();
    }

    public S getSheet() {
        return sheet;
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