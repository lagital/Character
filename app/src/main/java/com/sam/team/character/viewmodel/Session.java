package com.sam.team.character.viewmodel;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class Session {

    private static final String TAG = "Session";

    private static Session instance;

    private ArrayList<ViewModelSystem> systemStorage;
    private ViewModelSystem currentSystem;
    private ViewModelElementType elementType;
    private ViewModelCategory category;

    public static synchronized Session getInstance() {
        if (instance == null) {
            Log.d(TAG, "initiate new singleton object");
            instance = new Session();
        }
        return instance;
    }

    public void setSystemStorage (ArrayList<ViewModelSystem> systemStorage) {
        this.systemStorage = systemStorage;
    }

    public ArrayList<ViewModelSystem> getSystemStorage() {
        return systemStorage;
    }

    public void cacheSystem(ViewModelSystem system) {
        Log.d(TAG, "cacheSystem");
        this.currentSystem = system;
    }

    public ViewModelSystem getSystemFromCache() {
        Log.d(TAG, "getSystemFromCache");
        return currentSystem;
    }

    public void cacheElement(ViewModelElementType elementType) {
        Log.d(TAG, "cacheElementType");
        this.elementType = elementType;
    }

    public ViewModelElementType getElementFromCache () {
        Log.d(TAG, "getElementTypeFromCache");
        return elementType;
    }

    public void cacheCategory (ViewModelCategory category) {
        Log.d(TAG, "cacheCategory");
        this.category = category;
    }

    public ViewModelCategory getCategoryFromCache () {
        Log.d(TAG, "getCategoryFromCache");
        return category;
    }
}