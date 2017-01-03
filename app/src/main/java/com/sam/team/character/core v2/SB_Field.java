
package sbcore;

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
    
    //work with field's name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    
    //work with index
    public void setIndex(int index) { this.index = index; }
    public int getIndex() { return index; }
    
    //work with field's calculation rule
    public void setRule(String calcRule) { this.calcRule = calcRule; }
    public String getRule() { return calcRule; }
    public void removeRule() { calcRule = null; } 
    
}
