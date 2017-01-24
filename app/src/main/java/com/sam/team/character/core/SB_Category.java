package com.sam.team.character.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.simpleframework.xml.*;

@Root(name="Category")
public class SB_Category <
        S extends SB_System,
        E extends SB_ElementType<S, E, C, F>,
        C extends SB_Category<S, E, C, F>,
        F extends SB_Field<S, E, C, F>> {
    
    @Attribute(name = "index") private int index;
    @Element(name = "Name") private String name;
    private Map<String, F> fields;
    @ElementList(name = "Fields") private List<F> fieldsXML;
    private E element;
    
    //constructor to create temporary objects
    public SB_Category() {
        fields = new TreeMap<>();
    }
    //constructor to create permanent objects
    public SB_Category(int index, String name, E element) {
        this.index = index;
        this.name = name;
        this.element = element;
        fields = new TreeMap<>();
    }

    public E getElement() {
        return element;
    }

    public void setElement(E element) {
        this.element = element;
    }
    
    //work with name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    
    //work with index
    public void setIndex(int index) { this.index = index; }
    public int getIndex() { return index; }
        
    //work with fields
    public void addField(Class<F> clazz, String fieldName, SB_Field.FieldType type, boolean ... rewrite) throws Exception {
        F tmp = clazz.getConstructor().newInstance();
        tmp.setIndex(getAmountOfFields()+1);
        tmp.setName(fieldName);
        tmp.setType(type);
        tmp.setCategory((C) this);
        fields.put(fieldName, tmp);

    } 
    public void removeField(String fieldName) throws FieldExistException {
        if(!fields.containsKey(fieldName)) throw new FieldExistException("Field doesn't exist");
        fields.remove(fieldName);
    };
    public F getField(String fieldName) throws FieldExistException {
        if(!fields.containsKey(fieldName)) throw new FieldExistException("Field doesn't exist");
        return fields.get(fieldName);
    }
    public ArrayList<String> getFields() {
        ArrayList<String> tmp = new ArrayList<>();
        for (String key : fields.keySet()) {
            tmp.add(key);
        }
        Collections.sort(tmp, SortByIndex(this));
        return tmp;
    }
    public int getAmountOfFields(){ return fields.size(); }
    
    //custom comparator
    private Comparator<String> SortByIndex(final SB_Category category) {
        return new Comparator<String>(){
            @Override
            public int compare(String s1, String s2)
            {
                int tmp = 0;
                try{
                    tmp = Integer.toString(category.getField(s1).getIndex()).compareTo(
                          Integer.toString(category.getField(s2).getIndex()));
                } catch(Exception e){}
                return tmp;
            }        
        };
    }
    
    //generateXML
    public void prepareListOfFields() {
        fieldsXML = new ArrayList<F>();
        for(String s : getFields()){
            fieldsXML.add(fields.get(s));
        };
    }
    public void listToMap() {
        if(fieldsXML.size() == 0) return;
        fields.clear();
        for(F f: fieldsXML) {
            fields.put(f.getName(), f);
            fields.get(f.getName()).listToMap();
        }
    }
}
