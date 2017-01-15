package com.sam.team.character.viewmodel;

import android.content.Context;
import android.util.Log;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.design.ApplicationMain;

import java.io.File;
import java.util.ArrayList;

import static com.sam.team.character.corev2.SB_Field.FieldType.CALCULATED;
import static com.sam.team.character.corev2.SB_Field.FieldType.LONG_TEXT;
import static com.sam.team.character.corev2.SB_Field.FieldType.NUMERIC;
import static com.sam.team.character.corev2.SB_Field.FieldType.SHORT_TEXT;

/**
 * Created by pborisenko on 10/8/2016.
 */

public class Session {

    private static final String TAG = "Session";

    private static Session instance;

    private ArrayList<ViewModelSystem> systemStorage;
    private boolean loadDone;
    private ViewModelSystem currentSystem;
    private ViewModelElementType elementType;
    private ViewModelCategory category;
    private ViewModelField field;

    public static synchronized Session getInstance() {
        if (instance == null) {
            Log.d(TAG, "initiate new singleton object");
            instance = new Session();
            instance.setSystemStorage(new ArrayList<ViewModelSystem>());
            instance.loadDone = false;
        }
        return instance;
    }

    public void setSystemStorage (ArrayList<ViewModelSystem> systemStorage) {
        this.systemStorage = systemStorage;
    }

    public ArrayList<ViewModelSystem> getSystemStorage() {
        return systemStorage;
    }

    public boolean isLoadDone() {
        return loadDone;
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

    public void cacheField (ViewModelField field) {
        Log.d(TAG, "cacheField");
        this.field = field;
    }

    public ViewModelField getFieldFromCache () {
        Log.d(TAG, "getFieldFromCache");
        return field;
    }

    public void collectAvailableSystems (Context context) {
        if (!loadDone) {
            if (ApplicationMain.isExternalStorageReadable()) {
                for (File f : context.getExternalFilesDir(null).listFiles()) {
                    // find all rpg system files
                    if (f.getName().endsWith(ViewModelSystem.SYSTEM_FILE_TYPE)) {
                        Log.d(TAG, "File " + f.getName() + " has been chosen.");
                        //TODO: creating System objects from files, adding into systemStorage
                    }
                }
            }
            loadDone = true;

            /* DEBUG */
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "fill debug values");

                ViewModelSystem rps = new ViewModelSystem("Game", "1.0", "Bla-bla");
                rps.addElement("Character Sheet");
                rps.getElement("Character Sheet").addCategory("Main");
                rps.getElement("Character Sheet").addCategory("Additional");
                rps.addField("Character Sheet", "Main", "Name", SHORT_TEXT);
                rps.getField("Character Sheet", "Main", "Name").setValue("Test");
                rps.addField("Character Sheet", "Main", "Age", NUMERIC);
                rps.getField("Character Sheet", "Main", "Age").setValue("1");
                rps.addField("Character Sheet", "Additional", "Knowledge", LONG_TEXT);
                rps.getField("Character Sheet", "Additional", "Knowledge").setValue("Test");
                rps.addField("Character Sheet", "Additional", "Power", CALCULATED);
                rps.getField("Character Sheet", "Additional", "Power").setValue("[Character Sheet.Main.Age]");
                systemStorage.add(rps);
            }
            /* DEBUG */
        }
    }
}