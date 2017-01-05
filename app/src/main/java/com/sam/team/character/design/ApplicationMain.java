package com.sam.team.character.design;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.sam.team.character.BuildConfig;
import com.sam.team.character.R;
import com.samsara.team.samsaralib.core.SamsaraCore;

/**
 * Created by pborisenko on 7/3/2016.
 */
public class ApplicationMain extends Application{

    private static final String TAG = "ApplicationMain";

    private static String appExternalStoragePath;
    private static String appInternalStoragePath;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "set debug locale");
            SamsaraCore.setLocale(this, null, R.string.locale);
        }

        appInternalStoragePath = this.getFilesDir().getAbsolutePath();

        if (isExternalStorageReadable() && isExternalStorageWritable()) {
            appExternalStoragePath = this.getExternalFilesDir(null).getAbsolutePath();
        }
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static String getAppExternalStoragePath () {
        return appExternalStoragePath;
    }

    public static String getAppInternalStoragePath () {
        return appInternalStoragePath;
    }
}
