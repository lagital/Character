package com.sam.team.character.design;

import android.app.Application;
import android.util.Log;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.samsara.team.samsaralib.core.SamsaraCore;

/**
 * Created by pborisenko on 7/3/2016.
 */
public class ApplicationMain extends Application{

    private static final String TAG = "ApplicationMain";

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "set debug locale");
            SamsaraCore.setLocale(this, null, R.string.locale);
        }
    }
}
