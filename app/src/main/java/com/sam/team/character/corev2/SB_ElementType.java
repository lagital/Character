package com.sam.team.character.corev2;

import java.util.ArrayList;
import java.util.Collections;
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
public class SB_ElementType<
        S extends SB_System,
        E extends SB_ElementType<S, E, C, F>,
        C extends SB_Category<S, E, C, F>,
        F extends SB_Field<S, E, C, F>> {
    
    @Attribute(name = "index") private int index;
    @Element(name = "Name") private String name;
    private Map<String, C> categories;
    @ElementList(name="Categories") private List<C> categoriesXML;
    private S system;
    
    //full constructor
    public SB_ElementType(int index, String name, S system) {
        this.index = index;
        this.name = name;
        this.system = system;
        categories = new TreeMap<>();
    }

    //dummy constructor
    public SB_ElementType() {
        categories = new TreeMap<>();
    }
    
    //work with name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    
    //work with index
    public int getIndex(){ return index; }

    //work with system
    public S getSystem(){ return system; }
    public void setSystem(S system){ this.system = system; }

    //work with categories
    public void addCategory(Class<C> clazz, String categoryName, boolean ... rewrite) throws Exception {
        C tmp = clazz.getConstructor().newInstance();
        tmp.setIndex(getAmountOfCategories()+1);
        tmp.setName(categoryName);
        tmp.setElement((E) this);
        categories.put(categoryName, tmp);
    } 
    public void removeCategory(String categoryName) {
        categories.remove(categoryName);
    };
    public C getCategory(String categoryName) {
        return categories.get(categoryName);
    }
    public ArrayList<String> getCategories() {
        ArrayList<String> tmp = new ArrayList<>();
        for (String key : categories.keySet()) {
            tmp.add(key);
        }
        Collections.sort(tmp, SortByIndex(this));
        return tmp;
    }
    public int getAmountOfCategories(){ return categories.size(); }
    
    //work with fields
    public void addField(Class<F> clazz, String categoryName, String fieldName, boolean ... rewrite) throws Exception{
        categories.get(categoryName).addField(clazz, fieldName, rewrite);
    } 
    public void removeField(String categoryName, String fieldName) {
        categories.get(categoryName).removeField(fieldName);
    }
    public F getField(String categoryName, String fieldName) {
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
    private Comparator<String> SortByIndex(final SB_ElementType elementType) {
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
        categoriesXML = new ArrayList<C>();
        for(String s : getCategories()){
            categoriesXML.add(categories.get(s));
        };
    }
}
