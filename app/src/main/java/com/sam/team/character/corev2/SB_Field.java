package com.sam.team.character.corev2;

import com.sam.team.character.viewmodel.ViewModelField;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.Arrays;

/**
 *
 * @author vaize
 */
@Root(name="Field")
public class SB_Field<
        S extends SB_System,
        E extends SB_ElementType<S, E, C, F>,
        C extends SB_Category<S, E, C, F>,
        F extends SB_Field<S, E, C, F>> {

    /* symbols for typing formulas and values
     * [] - link to field used in formulas
     * {} - mentioning field's name in text
     */
    public static final String LINK_OPEN_SYMBOL = "[";
    public static final String LINK_CLOSE_SYMBOL = "]";
    public static final String MENTION_OPEN_SYMBOL = "{";
    public static final String MENTION_CLOSE_SYMBOL = "}";

    // delimiter used in typing Links and Mentions
    public static final String DELIMITER = ".";

    // only fields of registered types a to be used for typing in formulas
    public static final FieldType[] LINK_COMPATIBLE_TYPES = {FieldType.CALCULATED, FieldType.NUMERIC};

    @Attribute(name = "index") private int index;
    @Element(name = "Name") private String name;
    @Element(name = "Value") private String value = "";
    @Element(name = "Type") private String type;
    private C category;

    //constructor to create temporary objects
    public SB_Field() {}
    //constructor to create permanent objects
    public SB_Field(int index, String name, FieldType type, C category) {
        this.index = index;
        this.name = name;
        this.category = category;
        this.type = type.name();
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


    public boolean isLinkCompatible() {
        return Arrays.asList(ViewModelField.LINK_COMPATIBLE_TYPES).contains(this.getType());
    }

    public enum FieldType {
        SHORT_TEXT,
        LONG_TEXT,
        NUMERIC,
        CALCULATED,
        UNDEFINED
    }
    
}
