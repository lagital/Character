package com.sam.team.character.design;

import android.util.Log;

import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysRPSystem;

/**
 * Created by pborisenko on 10/8/2016.
 */

class Session {

    private static final String TAG = "Session";

    private static Session instance;

    private SysRPSystem currentSystem;
    private SysElement element;

    static synchronized Session getInstance() {
        if (instance == null) {
            Log.d(TAG, "initiate new singleton object");
            instance = new Session();
        }
        Log.d(TAG, "use existing singleton object");
        return instance;
    }

    SysRPSystem getCurrentSystem() {
        Log.d(TAG, "getCurrentSystem");
        return currentSystem;
    }

    void setCurrentSystem(SysRPSystem system) {
        Log.d(TAG, "setCurrentSystem");
        this.currentSystem = system;
    }

    void cacheElement (SysElement element) {
        Log.d(TAG, "cacheElement");
        this.element = element;
    }

    SysElement getElementFromCache () {
        Log.d(TAG, "getElementFromCache");
        return element;
    }
}
