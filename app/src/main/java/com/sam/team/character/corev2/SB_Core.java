package com.sam.team.character.corev2;

import java.util.ArrayList;

/**
 *
 * @author vaize
 */
public class SB_Core {
    public static void main(String[] args) {
        SB_System rps = new SB_System("rps");
        
            rps.addElement("Investigator");
            rps.addCategory("Investigator", "Common");
            rps.addField("Investigator", "Common", "Name");
            rps.addField("Investigator", "Common", "Player");
            rps.addField("Investigator", "Common", "Age");
            rps.addCategory("Investigator", "Skills");
            
            
            rps.addElement("Weapon");
            rps.addCategory("Weapon", "Common");
            
            rps.addElement("Skill");
            rps.addCategory("Skill", "Common");
            
            
        ArrayList<String> el_list = new ArrayList<String>();
        el_list = rps.getElements();
        for(String s1 : el_list) {
                SB_ElementType e = rps.getElement(s1);
                System.out.print(e.getIndex() + ". ");
                System.out.print(e.getName());
                System.out.print(" (Categories: " + e.getAmountOfCategories() + ";");
                System.out.println(" Fields: " + e.getAmountOfFields()+ ")");
                
                ArrayList<String> cat_list = new ArrayList<String>();
                cat_list = e.getCategories();
                for(String s2 : cat_list) {
                    SB_Category c = rps.getCategory(s1, s2);
                    System.out.print("\t" + c.getIndex() + ". ");
                    System.out.print(c.getName());
                    System.out.println(" (Fields: " + c.getAmountOfFields() + ")");
                    
                    ArrayList<String> fie_list = new ArrayList<String>();
                    fie_list = c.getFields();
                    for(String s3 : fie_list) {
                        SB_Field f = rps.getField(s1, s2, s3);
                        System.out.print("\t\t" + f.getIndex() + ". ");
                        System.out.println(f.getName());
                    }
                    
                }
        }
        
        rps.exportXML("test.xml");
    }
}
