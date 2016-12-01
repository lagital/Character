
package com.sam.team.character.core;

import android.databinding.BaseObservable;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.Arrays;
import java.util.ArrayList;
/**
 *
 * @author Vaize
 */
@Root
public class RPSystem<E extends Element> extends BaseObservable {

    private String name;
    private String version;
    private String copyright;
    @ElementList
    private ArrayList<E> elements;

    public static String SYSTEM_FILE_TYPE = "xml";

    // character constructor
    public RPSystem(String name) {
        this.name = name;
        version = null;
        copyright = null;
        elements = new ArrayList<>();
    }

    // system constructor
    public RPSystem(String name, String version, String copyright) {
        this.name = name;
        this.version = version;
        this.copyright = copyright;
        elements = new ArrayList<>();
    }

    //work with name
    @org.simpleframework.xml.Element
    public void setName(String name) {
        this.name = name;
    }

    @org.simpleframework.xml.Element
    public String getName() {
        return name;
    }

    //work with version
    @org.simpleframework.xml.Element(required = false)
    public void setVersion(String version) {
        this.version = version;
    }

    @org.simpleframework.xml.Element(required = false)
    public String getVersion() {
        return version;
    }

    //work with copyright
    @org.simpleframework.xml.Element(required = false)
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @org.simpleframework.xml.Element(required = false)
    public String getCopyright() {
        return copyright;
    }

    //work with elements
    public void addElement(E element) {
        if (element.getType() == null || element.getName() == null) return;
        elements.add(element);
    }

    public void removeElement(Element.ElementType type, String name) {
        for (E e : elements) {
            if (e.getType() == type && e.getName().equals(name)){
                elements.remove(e);
            }
        }
    }

    public ArrayList<E> getElementsByName (Element.ElementType type, String name) {
        ArrayList<E> el = new ArrayList<>();
        for (E e : elements) {
            if (e.getType() == type && e.getName().equals(name)){
                el.add(e);
            }
        }
        return el;
    }

    public ArrayList<E> getElementsByType(Element.ElementType t) {
        ArrayList<E> el = new ArrayList<>();
        for (E e : elements) {
            if (e.getType() == t){
                el.add(e);
            }
        }
        return el;
    }

    public ArrayList<E> getElements () {
        ArrayList<E> el = new ArrayList<>();
        for (E e: elements) {
            el.add(e);
        }
        return el;
    }
    
    //calculation
    public String parseExp(String exp){
        if (!validateExp(exp)) return "not valid";
	while(exp.contains("(")){    
            System.out.println(exp);
            exp = exp.substring(0, exp.lastIndexOf("(")) +
                  calculateExp(exp.substring(exp.lastIndexOf("(")+1, exp.indexOf(")"))) +
                  exp.substring(exp.indexOf(")")+1, exp.length());
            //need add processing * and /
            exp = exp.replaceAll("\\+\\-|\\-\\+", "-");
            exp = exp.replaceAll("--", "-");
        }
        System.out.println(exp);
        exp = calculateExp(exp);
        return exp;
    }

    private String calculateExp(String exp){
        ArrayList<String> operands = new ArrayList();
        operands.addAll(Arrays.asList(exp.split("[\\+\\-\\*\\/)]+")));
        ArrayList<String> operators = new ArrayList();
        operators.addAll(Arrays.asList(exp.split("[\\w@:().]+")));
        
        //delete space (need improve regex)
        operators.remove(0);
        //calculcation (need improve)
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
    
    public boolean validateExp(String exp){
        return true;
    }

    public static File generateXML (RPSystem system, String root) {
        File xml = new File(root + "/" + system.getName() + "." + SYSTEM_FILE_TYPE);
        Serializer serializer = new Persister();
        try {
            serializer.write(system, xml);
            return xml;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RPSystem parseXML (String filePath) {
        File xml = new File(filePath);
        Serializer serializer = new Persister();
        try {
            return serializer.read(RPSystem.class, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
