package com.sam.team.character.corev2;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 *
 * @author vaize
 */
@Root(name="Field") public class SB_Field<
        S extends SB_System,
        E extends SB_ElementType<S, E, C, F>,
        C extends SB_Category<S, E, C, F>,
        F extends SB_Field<S, E, C, F>> {

    @Attribute(name = "index") private int index;
    @Element(name = "Name") private String name;
    @Element(name = "Value") private String value = "";
    @Element(name = "Type") private String type;
    @Element(name = "CalculationRule", required = false) private String calcRule;
    private C category;

    //constructor to create temporary objects
    public SB_Field() {}
    //constructor to create permanent objects
    public SB_Field(int index, String name, String calcRule, C category) {
        this.index = index;
        this.name = name;
        this.calcRule = calcRule;
        this.category = category;
    }

    public C getCategory() {
        return category;
    }

    public void setCategory(C category) {
        this.category = category;
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
