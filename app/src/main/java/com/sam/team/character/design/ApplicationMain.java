package com.sam.team.character.design;

import android.app.Application;
import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.samsara.team.samsaralib.core.SamsaraCore;

/**
 * Created by pborisenko on 7/3/2016.
 */
public class ApplicationMain extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            SamsaraCore.setLocale(this, null, R.string.locale);
        }
    }
}
