package com.sam.team.character.viewmodel;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/18/2016.
 */
public class CategoryTemplate {

    private String title;
    private ArrayList<AttributeTemplate> attributeTemplateList;

    public ArrayList<AttributeTemplate> getAttributeTemplateList() {
        return attributeTemplateList;
    }

    public void setAttributeTemplateList(ArrayList<AttributeTemplate> attributeTemplateList) {
        this.attributeTemplateList = attributeTemplateList;
    }

    public void addAttributeTemplateListItem (AttributeTemplate at) {
        this.attributeTemplateList.add(at);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
