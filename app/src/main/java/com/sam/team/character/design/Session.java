package com.sam.team.character.design;

import android.util.Log;
import com.sam.team.character.viewmodel.ViewModelCategory;
import com.sam.team.character.viewmodel.ViewModelElementType;
import com.sam.team.character.viewmodel.ViewModelSystem;

/**
 * Created by pborisenko on 10/8/2016.
 */

class Session {

    private static final String TAG = "Session";

    private static Session instance;

    private ViewModelSystem currentSystem;
    private ViewModelElementType elementType;
    private ViewModelCategory category;

    static synchronized Session getInstance() {
        if (instance == null) {
            Log.d(TAG, "initiate new singleton object");
            instance = new Session();
        }
        return instance;
    }

    ViewModelSystem getSystemFromCache() {
        Log.d(TAG, "getSystemFromCache");
        return currentSystem;
    }

    void cacheSystem(ViewModelSystem system) {
        Log.d(TAG, "cacheSystem");
        this.currentSystem = system;
    }

    void cacheElement(ViewModelElementType elementType) {
        Log.d(TAG, "cacheElementType");
        this.elementType = elementType;
    }

    ViewModelElementType getElementFromCache () {
        Log.d(TAG, "getElementTypeFromCache");
        return elementType;
    }

    void cacheCategory (ViewModelCategory category) {
        Log.d(TAG, "cacheCategory");
        this.category = category;
    }

    ViewModelCategory getCategoryFromCache () {
        Log.d(TAG, "getCategoryFromCache");
        return category;
    }
}
