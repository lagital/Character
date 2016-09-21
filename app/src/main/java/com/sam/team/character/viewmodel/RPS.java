package com.sam.team.character.viewmodel;

import android.databinding.Bindable;

import java.util.ArrayList;

/**
 * Created by Vaize on 18.09.2016.
 */
public class RPS {
    private String name;
    private ArrayList<RPSh> sheets;
    private ArrayList<String> sheetTypes;

    public RPS(){
        name = null;
        sheets = new ArrayList();
        sheetTypes = new ArrayList();
    }

    @Bindable
    public void setName(String name) { this.name = name; }

    @Bindable
    public String getName() { return name; }

    @Bindable
    public void addSheet(RPSh sheet) {
        sheets.add(sheet);
        if (sheet.getType().length() > 0) {
            if (!sheetTypes.contains(sheet.getType())) sheetTypes.add(sheet.getType());
        }
    }

    void removeSheet(int id) { sheets.remove(id); }

    @Bindable
    public void removeSheet(RPSh sheet) { sheets.remove(sheet); }

    RPSh getSheet(int id) { return sheets.get(id); }

    @Bindable
    public ArrayList getSheets() { return sheets; }

    int countSheets() { return sheets.size();}

    int countSheetTypes() { return sheetTypes.size(); }

    String getSheetType(int id) { return sheetTypes.get(id); }

    void copySheet(RPSh sheet) {
        this.addSheet(new RPSh(null, name, sheet.getType()));
        this.getSheet(sheets.size()-1).copyFields(sheet.getFields());
    }
}
