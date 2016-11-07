package com.sam.team.character.design;

import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysRPSystem;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class Session {

    private static Session instance;

    private SysRPSystem currentSystem;
    private SysElement element;

    static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    SysRPSystem getCurrentSystem() {
        return currentSystem;
    }

    void setCurrentSystem(SysRPSystem system) {
        this.currentSystem = system;
    }

    void cacheElement (SysElement element) {
        this.element = element;
    }

    SysElement getElementFromCache () {
        return element;
    }
}
