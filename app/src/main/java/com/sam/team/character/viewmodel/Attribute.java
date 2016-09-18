package com.sam.team.character.viewmodel;

import android.graphics.Bitmap;

import com.sam.team.character.viewmodel.AttributeTemplate.AttrType;
import java.util.ArrayList;

/**
 * Created by pborisenko on 9/18/2016.
 */
public class Attribute {

    private String title;
    private ArrayList<String> calculationRules;
    private AttrType attributeType;
    private ArrayList<Double> doubleValues;
    private ArrayList<String> strValues;
    private ArrayList<Bitmap> iconValues;

    /* values - double */
    public Attribute(AttributeTemplate attrTemplate, Double v1, Double v2, Double v3) {
        this.attributeType = attrTemplate.getAttributeType();
        if (this.attributeType != AttrType.DOUBLE) {
            UnsupportedOperationException e = new UnsupportedOperationException();
            e.printStackTrace();
            return;
        }
        this.doubleValues = new ArrayList<>();
        this.doubleValues.add(v1);
        this.doubleValues.add(v2);
        this.doubleValues.add(v3);
        this.title = attrTemplate.getTitle();
        this.calculationRules = attrTemplate.getCalculationRules();
    }

    /* values - string */
    public Attribute(AttributeTemplate attrTemplate, String v1, String v2, String v3) {
        this.attributeType = attrTemplate.getAttributeType();
        if (this.attributeType != AttrType.STRING) {
            UnsupportedOperationException e = new UnsupportedOperationException();
            e.printStackTrace();
            return;
        }
        this.strValues = new ArrayList<>();
        this.strValues.add(v1);
        this.strValues.add(v2);
        this.strValues.add(v3);
    }

    /* values - bitmap */
    public Attribute(AttributeTemplate attrTemplate, Bitmap v1, Bitmap v2, Bitmap v3) {
        this.attributeType = attrTemplate.getAttributeType();
        if (this.attributeType != AttrType.ICON) {
            UnsupportedOperationException e = new UnsupportedOperationException();
            e.printStackTrace();
            return;
        }
        this.iconValues = new ArrayList<>();
        this.iconValues.add(v1);
        this.iconValues.add(v2);
        this.iconValues.add(v3);
    }

    public void recalculateValues() {
        if (this.attributeType == AttrType.DOUBLE) {
            for (String cr : calculationRules) {
                if (cr != null) {
                    // TODO: rule parsing
                }
            }
        }
    }

    public void setDoubleValue(int pos, Double v) {
        if (this.calculationRules.get(pos) == null) {
            this.doubleValues.set(pos, v);
            this.recalculateValues();
        } else {
            UnsupportedOperationException e = new UnsupportedOperationException();
            e.printStackTrace();
        }
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getCalculationRules() {
        return calculationRules;
    }

    public AttrType getAttributeType() {
        return attributeType;
    }
}