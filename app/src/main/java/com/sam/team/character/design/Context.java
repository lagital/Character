package com.sam.team.character.design;

import com.sam.team.character.viewmodel.SysElement;
import com.sam.team.character.viewmodel.SysRPSystem;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class Context {

    private static Context instance;

    private SysRPSystem currentSystem;
    private SysElement element;

    public static synchronized Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public SysRPSystem getCurrentSystem() {
        return currentSystem;
    }

    public void setCurrentSystem(SysRPSystem system) {
        this.currentSystem = system;
    }

    public void cacheElement (SysElement element) {
        this.element = element;
    }

    public SysElement getElementFromCache () {
        return element;
    }
}
