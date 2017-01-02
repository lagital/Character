
package sbcore;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.simpleframework.xml.*;

/**
 *
 * @author vaize
 */
@Root(name = "Element")
public class SB_ElementType {
    
    @Attribute(name = "index") private int index;
    @Element(name = "Name") private String name;
    private Map<String, SB_Category> categories;
    @ElementList(name="Categories") private List<SB_Category> categoriesXML;
    private SB_System system;
    
    //full constructor
    public SB_ElementType(int index, String name, SB_System system) {
        this.index = index;
        this.name = name;
        this.system = system;
        categories = new TreeMap<>();
    }
    
    //work with name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    
    //work with index
    public int getIndex(){ return index; }
       
    //work with categories
    public void addCategory(String categoryName, boolean ... rewrite) {
        categories.put(categoryName, new SB_Category(getAmountOfCategories()+1, categoryName, this));
    } 
    public void removeCategory(String categoryName) {
        categories.remove(categoryName);
    };
    public SB_Category getCategory(String categoryName) { 
        return categories.get(categoryName);
    }
    public ArrayList<String> getCategories() {
        ArrayList<String> tmp = new ArrayList<>();
        for (String key : categories.keySet()) {
            tmp.add(key);
        }
        tmp.sort(SortByIndex(this));
        return tmp;
    }
    public int getAmountOfCategories(){ return categories.size(); }
    
    //work with fields
    public void addField(String categoryName, String fieldName, boolean ... rewrite) {
        categories.get(categoryName).addField(fieldName, rewrite);
    } 
    public void removeField(String categoryName, String fieldName) {
        categories.get(categoryName).removeField(fieldName);
    }
    public SB_Field getField(String categoryName, String fieldName) {
        return categories.get(categoryName).getField(fieldName);
    }
    public ArrayList<String> getFieldsInCategory(String categoryName) {
        return categories.get(categoryName).getFields();
    }
    public int getAmountOfFields() {
        int tmp = 0;
        for(String s : getCategories())
            tmp += getAmountOfFieldsInCategory(s);
        return tmp;
    }
    public int getAmountOfFieldsInCategory(String categoryName) {
        return categories.get(categoryName).getAmountOfFields();
    }
    
    //custom Comparator
    private Comparator<String> SortByIndex(SB_ElementType elementType) {   
        Comparator comp = new Comparator<String>(){
            @Override
            public int compare(String s1, String s2)
            {
                int tmp = 0;
                try{
                    tmp = Integer.toString(elementType.getCategory(s1).getIndex()).compareTo(
                          Integer.toString(elementType.getCategory(s2).getIndex()));
                } catch(Exception e){}
                return tmp;
            }        
        };
        return comp;
    }
    
    //generateXML
    public void prepareListOfCategories() {
        categoriesXML = new ArrayList<SB_Category>();
        for(String s : getCategories()){
            categoriesXML.add(categories.get(s));
        };
    }
}
