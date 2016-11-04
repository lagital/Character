package com.sam.team.character.design;

/**
 * Created by pborisenko on 10/16/2016.
 */

public interface ListItem {

    int TYPE_SYSTEM = 0;
    int TYPE_ELEMENT = 1;
    int TYPE_CATEGORY = 2;
    int TYPE_FIELD = 3;

    int getItemType();
}
