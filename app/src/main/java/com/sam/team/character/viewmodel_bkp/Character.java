package com.sam.team.character.viewmodel_bkp;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by pborisenko on 9/18/2016.
 */
public class Character {

    private String name;
    private Bitmap avatar;
    private ArrayList<Attribute> attributeList;

    public Character (String name, Bitmap avatar, ArrayList<Attribute> attrList) {
        this.name = name;
        this.avatar = avatar;
        this.attributeList = attrList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }
}
