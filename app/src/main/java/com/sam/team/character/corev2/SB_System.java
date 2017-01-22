package com.sam.team.character.corev2;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.simpleframework.xml.*;
import org.simpleframework.xml.core.Persister;

@Root(name = "System")
public class SB_System <
        S extends SB_System<S, E, C, F>,
        E extends SB_ElementType<S, E, C, F>,
        C extends SB_Category<S, E, C, F>,
        F extends SB_Field<S, E, C, F>> {

    @Element(name = "Name") private String name;
    private String version, copyright;
    private Map<String, E> elements;
    @ElementList(name = "Elements") private List<E> elementsXML;

    //constructors
    public SB_System(String name) {
        this.name = name;
        version = copyright = null;
        elements = new TreeMap<>();
    }
    public SB_System(String name, String version, String copyright) {
        this.name = name;
        this.version = version;
        this.copyright = copyright;
        elements = new TreeMap<>();
    }

    //work with name
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

    //work with version
    public void setVersion(String version) { this.version = version; }
    public String getVersion() { return version; }

    //work with copyright
    public void setCopyright(String copyright) { this.copyright = copyright; } 
    public String getCopyright() { return copyright; }
    
    //calculation (Builder needs only validation)
    public String parseExp(String exp){
    	if (!validateExp(exp)) return "not valid";
        String tmp;
	    while(exp.contains("(")){
            tmp = exp.substring(exp.lastIndexOf("("),exp.length()); 
            exp = exp.substring(0, exp.lastIndexOf("(")) +
                  calculateExp(tmp.substring(tmp.lastIndexOf("(")+1, tmp.indexOf(")"))) +
                  tmp.substring(tmp.indexOf(")")+1, tmp.length());
            exp = exp.replaceAll("\\+\\-|\\-\\+", "-");
            exp = exp.replaceAll("--", "+");
            System.out.println(exp);
        }
        exp = calculateExp(exp);
        return exp;
    }
    private String calculateExp(String exp){
        ArrayList<String> operands = new ArrayList();
        operands.addAll(Arrays.asList(exp.split("[\\+\\-\\*\\/)]+")));
        ArrayList<String> operators = new ArrayList();
        operators.addAll(Arrays.asList(exp.split("[\\w@:().]+")));
        //delete space (needs improvement regex)
        operators.remove(0);
        int tmp = 0;
        while(operators.size() > 0){
            if (operators.contains("*")){
                if (operators.contains("/")){
                    if (operators.indexOf("*") < operators.indexOf("/")){
                        tmp = operators.indexOf("*");
                        operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) *
                                                         Double.parseDouble(operands.get(tmp + 1))
                        ));
                    } else {
                    	tmp = operators.indexOf("/");
                        operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) /
                                                         Double.parseDouble(operands.get(tmp + 1))
                        ));
                    }
                } else {
                	tmp = operators.indexOf("*");
                        operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) *
                                                         Double.parseDouble(operands.get(tmp + 1))
                    ));
                }
            } else {
            	if (operators.contains("/")){
            		tmp = operators.indexOf("/");
                        operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) /
                                                         Double.parseDouble(operands.get(tmp + 1))
                        ));
            	} else {
            		if (operators.contains("+")){
                		if (operators.contains("-")){
                			if (operators.indexOf("+") < operators.indexOf("-")){
                				tmp = operators.indexOf("+");
                                                operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) +
                                                                                 Double.parseDouble(operands.get(tmp + 1))
                                                ));
                			} else {
                				tmp = operators.indexOf("-");
                                                operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) -
                                                                                 Double.parseDouble(operands.get(tmp + 1))
                    			));
                			}
                		} else {
                			tmp = operators.indexOf("+");
                                        operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) +
                                                                         Double.parseDouble(operands.get(tmp + 1))
                    		));
                		}
            		} else {
            			if (operators.contains("-")){
                                    tmp = operators.indexOf("-");
                                    operands.set(tmp, String.valueOf(Double.parseDouble(operands.get(tmp)) -
                                                                     Double.parseDouble(operands.get(tmp + 1))
                                    ));
            			}
            		}
            	}
            }
            operators.remove(tmp);
            operands.remove(tmp + 1);        
        }
        return operands.get(0);
    }
    private boolean validateExp(String exp){
        return true;
    }
    
    //work with elements
    public void addElement(Class<E> clazz, String elementName, boolean ... rewrite) throws Exception {// throws ElementExistException {
        //if (rewrite.length == 0 && elements.containsKey(elementName))
        //    throw new ElementExistException("Element already exists");
        E tmp = clazz.getConstructor().newInstance();
        tmp.setName(elementName);
        tmp.setSystem((S) this);
        elements.put(elementName, tmp);
    }
    public void removeElement(String elementName) {// throws ElementExistException {
        //if (!elements.containsKey(elementName)) 
        //    throw new ElementExistException("Element does not exist");
        elements.remove(elementName);
    }
    public E getElement(String elementName) {//throws ElementExistException {
        //if (!elements.containsKey(elementName)) 
        //    throw new ElementExistException("Element does not exist");
        return elements.get(elementName);
    }
    public ArrayList<String> getElements() {
        ArrayList<String> tmp = new ArrayList<>();
        for (String key : elements.keySet()) {
            tmp.add(key);
        }
        Collections.sort(tmp, SortByIndex(this));
        return tmp;
    }
    public int getAmountOfElements() { return elements.size(); }
    
    //work with categories
    public void addCategory(Class<C> clazz, String elementName, String categoryName, boolean ... rewrite) throws Exception {
        elements.get(elementName).addCategory(clazz, categoryName, rewrite);
    }
    public void removeCategory(String elementName, String categoryName) {
        elements.get(elementName).removeCategory(categoryName);
    }
    public C getCategory(String elementName, String categoryName) {
        return elements.get(elementName).getCategory(categoryName);
    }
    public ArrayList<String> getCategories(String elementName) {
        return elements.get(elementName).getCategories();
    }
    public int getAmountOfCategories() {
        int tmp = 0;
        for(String s : getElements())
            tmp += getAmountOfCategoriesInElement(s);
        return tmp;
    }
    public int getAmountOfCategoriesInElement(String elementName) {
        return elements.get(elementName).getAmountOfCategories();
    }
    
    //work with fields
    public void addField(Class<F> clazz, String elementName, String categoryName, String fieldName, SB_Field.FieldType type, boolean ... rewrite) throws Exception {
        elements.get(elementName).getCategory(categoryName).addField(clazz, fieldName, type, rewrite);
    }
    public void removeField(String elementName, String categoryName, String fieldName) {
        elements.get(elementName).getCategory(categoryName).removeField(fieldName);
    }
    public F getField(String elementName, String categoryName, String fieldName) {
        return elements.get(elementName).getCategory(categoryName).getField(fieldName);
    }
    public ArrayList<String> getFieldsInCategory(String elementName, String categoryName) {
        return elements.get(elementName).getFieldsInCategory(categoryName);
    }
    public int getAmountOfFields() {
        int tmp = 0;
        for(String s : getElements())
            tmp += getAmountOfFieldsInElement(s);
        return tmp;
    }
    public int getAmountOfFieldsInElement(String elementName) {
        return elements.get(elementName).getAmountOfFields();
    }
    
    //genetate XML
    private void prepateLists() {
        //prepare list of elements
        elementsXML = new ArrayList<E>();
        for(String e : getElements()){
            elementsXML.add(elements.get(e));
            //prepare lists of categories
            elements.get(e).prepareListOfCategories();
            for(String c : elements.get(e).getCategories()) {
                elements.get(e).getCategory(c).prepareListOfFields();
            }
        };
    }
    public String generateXML() {
        //prepare lists
        prepateLists();
        //generate xml
        Serializer serializer = new Persister();
        StringWriter sw = new StringWriter();
        try {
            serializer.write(this, sw);
            return sw.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static SB_System readXML(String path) throws Exception {
        Serializer serializer = new Persister();
        File source = new File(path);
        return serializer.read(SB_System.class, source);
    }
    public void listToMap() {
        if(elementsXML.size() == 0) return;
        elements.clear();
        for(E e: elementsXML) {
            elements.put(e.getName(), e);
            elements.get(e.getName()).listToMap();
        }
    }

    //custom comparator
    private Comparator<String> SortByIndex(final SB_System system) {
        Comparator comp = new Comparator<String>(){
            @Override
            public int compare(String s1, String s2)
            {
                int tmp = 0;
                try{
                    tmp = Integer.toString(system.getElement(s1).getIndex()).compareTo(
                          Integer.toString(system.getElement(s2).getIndex()));
                } catch(Exception e){}
                return tmp;
            }        
        };
        return comp;
    }
}