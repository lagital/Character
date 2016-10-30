package com.sam.team.character.viewmodel;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class Context {

    private static Context instance;

    private RPSystem currentSystem;
    private Element element;

    public static synchronized Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public RPSystem getCurrentSystem() {
        return currentSystem;
    }

    public void setCurrentSystem(RPSystem system) {
        this.currentSystem = system;
    }

    public void cacheElement (Element element) {
        this.element = element;
    }

    public Element getElementFromCache () {
        return element;
    }
}
