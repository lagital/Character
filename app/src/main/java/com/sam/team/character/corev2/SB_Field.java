package com.sam.team.character.corev2;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author vaize
 */
@Root(name="Field") public class SB_Field {

    @Attribute(name = "index") private int index;
    @Element(name = "Name") private String name;
    @Element(name = "Value") private String value;
    @Element(name = "Type") private String type;
    @Element(name = "CalculationRule", required = false) private String calcRule;
    private SB_Category category;

    //constructor to create temporary objects
    public SB_Field() {}
    //constructor to create permanent objects
    public SB_Field(int index, String name, String calcRule, SB_Category category) {
        this.index = index;
        this.name = name;
        this.calcRule = calcRule;
        this.category = category;
    }

    public SB_Category getCategory() {
        return category;
    }
    
    //work with field's name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    //work with field's value
    public void setValue(String value) { this.value = value; }
    public String getValue() { return value; }

    //work with index
    public void setIndex(int index) { this.index = index; }
    public int getIndex() { return index; }

    //work with type
    public void setType(FieldType type) { this.type = type.name(); }
    public FieldType getType() { return FieldType.valueOf(type); }
    
    //work with field's calculation rule
    public void setRule(String calcRule) { this.calcRule = calcRule; }
    public String getRule() { return calcRule; }
    public void removeRule() { calcRule = null; }

    public enum FieldType {
        SHORT_TEXT,
        LONG_TEXT,
        NUMERIC,
        CALCULATED
    }
    
}
