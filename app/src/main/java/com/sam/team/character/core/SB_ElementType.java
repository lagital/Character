package com.sam.team.character.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.simpleframework.xml.*;

@Root(name = "Element")
public class SB_ElementType<
        S extends SB_System<S, E, C, F>,
        E extends SB_ElementType<S, E, C, F>,
        C extends SB_Category<S, E, C, F>,
        F extends SB_Field<S, E, C, F>> implements Cloneable{
    
    @Attribute(name = "index") private int index = 0;
    @Element(name = "Name")    private String name = "";
    private Map<String, C> categories = new TreeMap<>();
    @ElementList(name="Categories") private List<C> categoriesXML = new ArrayList<>();
    private S system;
    @Attribute(name = "is_Character") private boolean isCharacter = false;
    @Attribute(name = "is_Template")  private boolean isTemplate  = false;
    
    //constructors
    public SB_ElementType(int index, String name, S system) {
        this.index = index;
        this.name = name;
        this.system = system;
    }
    public SB_ElementType() {}
    public SB_ElementType(E source) {
        this.name   = "";
        this.system = (S) source.getSystem();
        this.index  = this.system.getAmountOfElements() + 1;
        this.isTemplate  = false;
        this.isCharacter = true;

        for (C c : source.getCategories()) {

        }
    }

    //work with character flag
    public void setIsCharacter(boolean isCharacter) { this.isCharacter = isCharacter; }
    public boolean isCharacter() { return isCharacter; }

    //work with template flag
    public void setIsTemplate(boolean isTemplate) { this.isTemplate = isTemplate; }
    public boolean isTemplate() { return isTemplate; }

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

    public void addCloneCategory(Class<C> cclass, Class <F> fclass, C source) throws Exception {
        C tmp = cclass.getConstructor().newInstance();
        tmp.setIndex(getAmountOfCategories()+1);
        tmp.setName(source.getName());
        tmp.setElement((E) this);

        for (F f : source.getFields()) {
            tmp.addCloneField(fclass, f);
        }
        categories.put(source.getName(), tmp);
    }

    public void removeCategory(String categoryName) throws CategoryExistException {
        if (!categories.containsKey(categoryName)) throw new CategoryExistException("Category doesn't exist");
        categories.remove(categoryName);
    }

    public C getCategory(String categoryName) throws CategoryExistException {
        if (!categories.containsKey(categoryName)) throw new CategoryExistException("Category doesn't exist");
        return categories.get(categoryName);
    }

    public ArrayList<String> getCategoryNames() {
        ArrayList<String> tmp = new ArrayList<>();
        for (String key : categories.keySet()) {
            tmp.add(key);
        }
        Collections.sort(tmp, SortByIndex(this));
        return tmp;
    }

    public ArrayList<C> getCategories() {
        ArrayList<C> tmp = new ArrayList<>();
        ArrayList<String> tmps = new ArrayList<>();
        tmps.addAll(categories.keySet());
        Collections.sort(tmps, SortByIndex(this));
        for (String key : tmps) {
            try {
                tmp.add(this.getCategory(key));
            } catch (CategoryExistException e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

    public int getAmountOfCategories(){ return categories.size(); }
    
    //work with fields
    public void addField(Class<F> clazz, String categoryName, String fieldName, SB_Field.FieldType type, boolean ... rewrite) throws Exception{
        categories.get(categoryName).addField(clazz, fieldName, type, rewrite);
    } 
    public void removeField(String categoryName, String fieldName) throws FieldExistException {
        try { categories.get(categoryName).removeField(fieldName); }
        catch(Exception e) { throw new FieldExistException("Field doesn't exist"); }
    }
    public F getField(String categoryName, String fieldName) throws FieldExistException {
        try { return categories.get(categoryName).getField(fieldName); }
        catch(Exception e) { throw new FieldExistException("Field doesn't exist"); }
    }
    public ArrayList<String> getFieldsInCategory(String categoryName) {
        return categories.get(categoryName).getFieldNames();
    }
    public int getAmountOfFields() {
        int tmp = 0;
        for(C c : getCategories())
            tmp += getAmountOfFieldsInCategory(c);
        return tmp;
    }
    public int getAmountOfFieldsInCategory(C c) {
        return c.getAmountOfFields();
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
        for(String s : getCategoryNames()){
            categoriesXML.add(categories.get(s));
        };
    }

    public void listToMap() {
        if(categoriesXML.size() == 0) return;
        categories.clear();
        for (C c : categoriesXML) {
            categories.put(c.getName(), c);
            categories.get(c.getName()).listToMap();
        }
    }

    //clone
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
