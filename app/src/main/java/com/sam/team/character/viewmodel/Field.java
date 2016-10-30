
package com.sam.team.character.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.sam.team.character.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Vaize
 */

public class Field extends BaseObservable implements ListItem {

    private static final String TAG = "Field";

    private String category, name;
    private ArrayList<String> values, calcRules;

    private FieldType type;

    public Field(String category, String name, FieldType type) {
        this.category = category;
        this.name = name;
        this.type = type;
        values = new ArrayList();
        calcRules = new ArrayList();
    }
    
    public String getCategory() { return category; }

    public void setName(String name) { this.name = name; }

    @Bindable
    public String getName() { return name; }

    public void setCategory(String category) { this.category = category; }

    public int addValue(String value) {
        values.add(value);
        calcRules.add(null);
        return (values.size() - 1); //id
    }

    public void setValue(int id, String value) { values.set(id, value); }

    public String getValue(int id) {
        if (values.isEmpty()) return "NaN";
        else if (values.size() < id - 1) return "NaN";
        else return values.get(id);
    }

    @Bindable
    public String getValue() {
        if (values.isEmpty()) return "NaN";
        else return values.get(0); 
    }

    public ArrayList getValues() { return values; }

    public int getNumberOfValues() { return values.size(); }

    public void removeValue(int id) { values.remove(id); }

    public void removeValue(String value) { values.remove(value); }

    public void setRule(int id, String rule) { calcRules.set(id, rule); }

    public String getRule(int id) { return calcRules.get(id); }

    public String getRule() { return calcRules.get(0); }

    public ArrayList getRules() { return calcRules; }

    public void removeRule(int id) { calcRules.remove(id); }

    public void removeRule(String rule) { calcRules.remove(rule); }

    public int describeContents() {
        return 0;
    }

    @Override
    public int getItemType() {
        return TYPE_FIELD;
    }

    public FieldType getType() {
        return type;
    }

    public String getTypeStr() {
        return type.name();
    }



    public enum FieldType {
        SHORT_TEXT,
        LONG_TEXT,
        NUMERIC
    }
}