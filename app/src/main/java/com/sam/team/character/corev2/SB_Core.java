package com.sam.team.character.corev2;

import java.util.ArrayList;

import static com.sam.team.character.corev2.SB_Field.FieldType.NUMERIC;
import static com.sam.team.character.corev2.SB_Field.FieldType.SHORT_TEXT;

/**
 *
 * @author vaize
 */
public class SB_Core {
    public static void main(String[] args) {
        SB_System rps = new SB_System("rps");
        try {
            rps.addElement(SB_ElementType.class, "Investigator");
            rps.addCategory(SB_Category.class, "Investigator", "Common");
            rps.addField(SB_Field.class, "Investigator", "Common", "Name", SHORT_TEXT);
            rps.addField(SB_Field.class, "Investigator", "Common", "Player", SHORT_TEXT);
            rps.addField(SB_Field.class, "Investigator", "Common", "Age", NUMERIC);
            rps.addCategory(SB_Category.class, "Investigator", "Skills");


            rps.addElement(SB_ElementType.class, "Weapon");
            rps.addCategory(SB_Category.class, "Weapon", "Common");

            rps.addElement(SB_ElementType.class, "Skill");
            rps.addCategory(SB_Category.class, "Skill", "Common");
        } catch (Exception e) {
            e.printStackTrace();
        }
            
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
        
        rps.generateXML();
    }
}