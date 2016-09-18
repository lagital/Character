package com.sam.team.character.viewmodel;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/18/2016.
 */
public class System {

    private String title;
    private Integer version;
    private ArrayList<Character> characterList;
    private ArrayList<CategoryTemplate> categoryTemplateList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public ArrayList<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(ArrayList<Character> characterList) {
        this.characterList = characterList;
    }

    public ArrayList<CategoryTemplate> getCategoryTemplateList() {
        return categoryTemplateList;
    }

    public void setCategoryTemplateList(ArrayList<CategoryTemplate> categoryTemplateList) {
        this.categoryTemplateList = categoryTemplateList;
    }

    public void addCategoryTemplateItem (CategoryTemplate ct) {
        this.categoryTemplateList.add(ct);
    }

    public void addCharacter (Character c) {
        this.characterList.add(c);
    }
}
