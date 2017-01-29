package com.sam.team.character.viewmodel;

/**
 * Interface for Drawer items
 * Created by lagital on 22.01.17.
 */

public interface DrawerItem {
    String getDrawerItemTitle();
    DrawerItemType getDrawerItemType();

    enum DrawerItemType {
        PAGE,
        DICEBAG,
        ADD_DICES,
        TITLE
    }
}