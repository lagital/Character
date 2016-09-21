package com.sam.team.character.viewmodel;

import java.util.ArrayList;

/**
 * Created by Vaize on 18.09.2016.
 */
public class RPSh {
    private String name, system, type;
    private ArrayList<Field> fields;
    private ArrayList<String> fieldsCategories;

    RPSh() {
        name = system = type = null;
        fields = new ArrayList();
        fieldsCategories = new ArrayList();
    }
    RPSh(String name, String system, String type) {
        this.name = name;
        this.system = system;
        this.type = type;
        fields = new ArrayList();
        fieldsCategories = new ArrayList();
    }

    void setSystem(String system){ this.system = system; }
    String getSytem() { return system; }
    void setName(String name) { this.name = name; }
    String getName() { return name; }
    void setType(String type) { this.type = type; }
    String getType() { return type; }
    void addField(Field field) {
        fields.add(field);
        if (field.getCategory().length() > 0) {
            if (!fieldsCategories.contains(field.getCategory())) fieldsCategories.add(field.getCategory());
        }
    }
    void removeField(int id) { fields.remove(id); }
    void removeField(Field field) { fields.remove(field); }
    Field getField(int id) { return fields.get(id); }
    ArrayList getFields() { return fields; }
    int countFields() { return fields.size(); }
    int countFieldsCategories() { return fieldsCategories.size(); }
    String getFieldsCategories(int id) { return fieldsCategories.get(id); }
    void copyFields(ArrayList<Field> fields) {
        for(Field f : fields) {
            this.fields.add(new Field(f.getCategory(), f.getName()));
            if (f.getCategory().length() > 0) {
                if (!fieldsCategories.contains(f.getCategory())) fieldsCategories.add(f.getCategory());
            }
        }
    }
}
