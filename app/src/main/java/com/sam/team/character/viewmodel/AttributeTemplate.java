package com.sam.team.character.viewmodel;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/18/2016.
 */
public class AttributeTemplate {

    private String title;
    private ArrayList<String> calculationRules;
    private AttrType attributeType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getCalculationRules() {
        return calculationRules;
    }

    public void setCalculationRules(ArrayList<String> calculationRules) {
        this.calculationRules = calculationRules;
    }

    public void addCalculationRule(String cr) {
        this.calculationRules.add(cr);
    }

    public AttrType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttrType attributeType) {
        this.attributeType = attributeType;
    }

    public enum AttrType {
        INTEGER,
        DOUBLE,
        STRING,
        ICON
    }
}
