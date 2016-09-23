
package com.sam.team.character.viewmodel2;

import java.util.ArrayList;

/**
 *
 * @author Vaize
 */
public class Field {

    private String category, name;
    private ArrayList<String> values, calcRules;

    Field() {
        category = name = null;
        values = new ArrayList();
        calcRules = new ArrayList();
    }
    Field(String category, String name) {
        this.category = category;
        this.name = name;
        values = new ArrayList();
        calcRules = new ArrayList();
    }
    
    public String getCategory() { return category; }
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    public void setCategory(String category) { this.category = category; }
    public void addValue(String value) { values.add(value); }
    public void setValue(int id, String value) { values.set(id, value); }
    public String getValue(int id) { 
        if (values.isEmpty()) return "NaN";
        else if (values.size() < id - 1) return "NaN";
        else return values.get(id);
    }
    public String getValue() { 
        if (values.isEmpty()) return "NaN";
        else return values.get(0); 
    }
    public ArrayList getValues() { return values; }
    public void removeValue(int id) { values.remove(id); }
    public void removeValue(String value) { values.remove(value); }
    public void addRule(String rule) { calcRules.add(rule); }
    public void setRule(int id, String rule) { calcRules.set(id, rule); }
    public String getRule(int id) { return calcRules.get(id); }
    public String getRule() { return calcRules.get(0); }
    public ArrayList getRules() { return calcRules; }
    public void removeRule(int id) { calcRules.remove(id); }
    public void removeRule(String rule) { calcRules.remove(rule); }
}