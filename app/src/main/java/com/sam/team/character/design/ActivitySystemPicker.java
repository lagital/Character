package com.sam.team.character.design;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.sam.team.character.R;
import com.sam.team.character.data.DBHelper;

public class ActivitySystemPicker extends AppCompatActivity {
    private static final String TAG = "ActivitySystemPicker";

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_picker);

        AsyncTaskInitDB task = new AsyncTaskInitDB(this);
        task.execute();
    }

    private class AsyncTaskInitDB extends AsyncTask<Void,Void,Void> {
        private static final String TAG = "AsyncTaskInitDB";

        private Context mContext;

        AsyncTaskInitDB (Context context) {
            mContext = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");

            mProgressDialog = ProgressDialog.show(
                    mContext,
                    getResources().getString(R.string.db_validation_dialog_title),
                    getResources().getString(R.string.db_validation_dialog),
                    true);

            DBHelper mDbHelper = new DBHelper(mContext);
            ApplicationMain.setWriteDb(mDbHelper.getWritableDatabase());
            ApplicationMain.setReadDb(mDbHelper.getReadableDatabase());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.cancel();
        }
    }
}
