package com.sam.team.character.viewmodel;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class Session {

    private static Session instance;

    private RPSystem currentSystem;

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public RPSystem getCurrentSystem() {
        return currentSystem;
    }

    public void setCurrentSystem(RPSystem system) {
        this.currentSystem = system;
    }
}
