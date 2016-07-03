package com.sam.team.character.design;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.samsara.team.samsaralib.core.SamsaraCore;

/**
 * Created by pborisenko on 7/3/2016.
 */
public class ApplicationMain extends Application{

    public static SQLiteDatabase WRITE_DB;
    public static SQLiteDatabase READ_DB;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            SamsaraCore.setLocale(this, null, R.string.locale);
        }
    }

    public static void setWriteDb (SQLiteDatabase db) {
        WRITE_DB = db;
    }

    public static void setReadDb (SQLiteDatabase db) {
        READ_DB = db;
    }
}
