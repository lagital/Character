
package com.sam.team.character.core;

import android.databinding.BaseObservable;

import org.simpleframework.xml.Element;
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
public class RPSystem<S extends Sheet> extends BaseObservable {

    public static String SYSTEM_FILE_TYPE = "xml";

    private String name;
    private String version;
    private String copyright;
    @ElementList
    private ArrayList<S> sheets;

    // character constructor
    public RPSystem(String name) {
        this.name = name;
        version = null;
        copyright = null;
        sheets = new ArrayList<>();
    }

    // system constructor
    public RPSystem(String name, String version, String copyright) {
        this.name = name;
        this.version = version;
        this.copyright = copyright;
        sheets = new ArrayList<>();
    }

    //work with name
    @Element
    public void setName(String name) {
        this.name = name;
    }

    @Element
    public String getName() {
        return name;
    }

    //work with version
    @Element(required = false)
    public void setVersion(String version) {
        this.version = version;
    }

    @Element(required = false)
    public String getVersion() {
        return version;
    }

    //work with copyright
    @Element(required = false)
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    @Element(required = false)
    public String getCopyright() {
        return copyright;
    }

    //work with sheets
    public void addSheet(S sheet) {
        if (sheet.getType() == null || sheet.getName() == null) return;
        sheets.add(sheet);
    }

    public void removeSheet(Sheet.SheetType type, String name) {
        for (S s : sheets) {
            if (s.getType() == type && s.getName().equals(name)){
                sheets.remove(s);
            }
        }
    }

    public ArrayList<S> getSheetsByName (Sheet.SheetType type, String name) {
        ArrayList<S> el = new ArrayList<>();
        for (S s : sheets) {
            if (s.getType() == type && s.getName().equals(name)){
                el.add(s);
            }
        }
        return el;
    }

    public ArrayList<S> getSheetsByType(Sheet.SheetType t) {
        ArrayList<S> sl = new ArrayList<>();
        for (S s : sheets) {
            if (s.getType() == t){
                sl.add(s);
            }
        }
        return sl;
    }

    public ArrayList<S> getSheets () {
        ArrayList<S> sl = new ArrayList<>();
        for (S s: sheets) {
            sl.add(s);
        }
        return sl;
    }
    
    //calculation
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
        //delete space (need improve regex)
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
