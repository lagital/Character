package com.sam.team.character.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.sam.team.character.BuildConfig;

/**
 * Created by pborisenko on 5/13/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    // If you change the database schema, you must increment the database version.
    public static final int     DATABASE_VERSION = 1;
    public static final String  DATABASE_NAME = "Character.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE  = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String BLOB_TYPE = " BLOB";
    private static final String SEP = ",";

    private static final String SQL_CREATE_SYSTEM_TABLE =
            "CREATE TABLE " + SystemColumns.TABLE_NAME + " (" +
                    SystemColumns._ID                   + " INTEGER PRIMARY KEY," +
                    SystemColumns.NAME                  + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CategoryColumns.TABLE_NAME + " (" +
                    CategoryColumns._ID                 + " INTEGER PRIMARY KEY," +
                    CategoryColumns.NAME                + TEXT_TYPE + SEP +
                    CategoryColumns.DESCRIPTION         + TEXT_TYPE + SEP +
                    CategoryColumns.SYSTEM__OID         + INT_TYPE  +
                    " )";

    private static final String SQL_CREATE_ATTRIBUTE_TABLE =
            "CREATE TABLE " + AttributeColumns.TABLE_NAME + " (" +
                    AttributeColumns._ID                + " INTEGER PRIMARY KEY," +
                    AttributeColumns.NAME               + TEXT_TYPE + SEP +
                    AttributeColumns.SEQV_NUMBER        + INT_TYPE  + SEP +
                    AttributeColumns.TYPE               + INT_TYPE  + SEP +
                    AttributeColumns.TEXT               + TEXT_TYPE + SEP +
                    AttributeColumns.NUMERIC            + REAL_TYPE + SEP +
                    AttributeColumns.CALCULATION_RULES  + TEXT_TYPE + SEP +
                    AttributeColumns.ATTRIBUTE__OID     + INT_TYPE  + SEP +
                    AttributeColumns.CATEGORY__OID      + INT_TYPE  + SEP +
                    AttributeColumns.IS_PREDEFINED      + INT_TYPE  + SEP +
                    AttributeColumns.IS_TEMPLATE        + INT_TYPE  +
                    " )";

    private static final String SQL_CREATE_ATTR_VALUE_TABLE =
            "CREATE TABLE " + AttrValueColumns.TABLE_NAME + " (" +
                    AttrValueColumns._ID                + " INTEGER PRIMARY KEY," +
                    AttrValueColumns.TEXT               + TEXT_TYPE + SEP +
                    AttrValueColumns.NUMERIC            + REAL_TYPE + SEP +
                    AttrValueColumns.TYPE               + INT_TYPE  + SEP +
                    AttrValueColumns.ATTRIBUTE__OID     + INT_TYPE  +
                    " )";

    private static final String SQL_CREATE_CHARACTER_TABLE =
            "CREATE TABLE " + CharacterColumns.TABLE_NAME + " (" +
                    CharacterColumns._ID                + " INTEGER PRIMARY KEY," +
                    CharacterColumns.NAME               + TEXT_TYPE + SEP +
                    CharacterColumns.SYSTEM__OID        + INT_TYPE  + SEP +
                    CharacterColumns.AVATAR             + BLOB_TYPE +
                    " )";

    private static final String SQL_DROP_SYSTEM_TABLE =
            "DROP TABLE IF EXISTS " + SystemColumns.TABLE_NAME;

    private static final String SQL_DROP_CATEGORY_TABLE =
            "DROP TABLE IF EXISTS " + CategoryColumns.TABLE_NAME;

    private static final String SQL_DROP_ATTRIBUTE_TABLE =
            "DROP TABLE IF EXISTS " + AttributeColumns.TABLE_NAME;

    private static final String SQL_DROP_ATTR_VALUE_TABLE =
            "DROP TABLE IF EXISTS " + AttrValueColumns.TABLE_NAME;

    private static final String SQL_DROP_CHARACTER_TABLE =
            "DROP TABLE IF EXISTS " + CharacterColumns.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_SYSTEM_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_ATTRIBUTE_TABLE);
        db.execSQL(SQL_CREATE_ATTR_VALUE_TABLE);
        db.execSQL(SQL_CREATE_CHARACTER_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        Log.d(TAG, "onUpgrade");
        db.execSQL(SQL_DROP_SYSTEM_TABLE);
        db.execSQL(SQL_DROP_CATEGORY_TABLE);
        db.execSQL(SQL_DROP_ATTRIBUTE_TABLE);
        db.execSQL(SQL_DROP_ATTR_VALUE_TABLE);
        db.execSQL(SQL_DROP_CHARACTER_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onDowngrade");
        onUpgrade(db, oldVersion, newVersion);
    }

    private static abstract class SystemColumns implements BaseColumns {
        public static final String TABLE_NAME   = "system";
        public static final String NAME         = "name";
    }

    private static abstract class CategoryColumns implements BaseColumns {
        public static final String TABLE_NAME   = "category";
        public static final String NAME         = "name";
        public static final String DESCRIPTION  = "description";
        public static final String SYSTEM__OID  = "system__oid";
    }

    private static abstract class AttributeColumns implements BaseColumns {
        public static final String TABLE_NAME           = "attribute";
        public static final String NAME                 = "name";
        public static final String CATEGORY__OID        = "category__oid";
        public static final String ATTRIBUTE__OID       = "attribute__oid";
        public static final String TYPE                 = "type";
        public static final String SEQV_NUMBER          = "seqv_number";
        public static final String CALCULATION_RULES    = "calculation_rules";
        public static final String TEXT                 = "text";
        public static final String NUMERIC              = "numeric";
        public static final String IS_PREDEFINED        = "is_predefined";
        public static final String IS_TEMPLATE          = "is_template";
    }

    private static abstract class AttrValueColumns implements BaseColumns {
        public static final String TABLE_NAME       = "attr_value";
        public static final String TEXT             = "text";
        public static final String NUMERIC          = "numeric";
        public static final String ATTRIBUTE__OID   = "attribute__oid";
        public static final String TYPE             = "type";
    }

    private static abstract class CharacterColumns implements BaseColumns {
        public static final String TABLE_NAME       = "character";
        public static final String NAME             = "name";
        public static final String SYSTEM__OID      = "system__oid";
        public static final String AVATAR           = "avatar";
    }

    /*-----------API-----------*/

    /*
    public void insertMissedCall (SQLiteDatabase db, String number, String dateTime, CallType type) {
        Log.d(TAG, "insertMissedCall: " + number);
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CallColumns.COL_CALL_NUMBER, number);
        values.put(CallColumns.COL_CALL_TIME, dateTime);
        values.put(CallColumns.COL_CALL_STATUS, callTypeToInt(type));
        values.put(CallColumns.COL_IS_ACTIVE, 1);
        // Insert the new row, returning the primary key value of the new row
        try {
            db.insert(CallColumns.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clearMissedCalls (SQLiteDatabase db) {
        Log.d(TAG, "clearMissedCalls");
        // Define 'where' part of query.
        String condition = "1 = 1";
        // Issue SQL statement.
        ContentValues cv = new ContentValues();
        cv.put(CallColumns.COL_IS_ACTIVE, "0");
        try {
            db.update(CallColumns.TABLE_NAME, cv, condition, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Call> retrieveMissedCalls (SQLiteDatabase db) {
        Log.d(TAG, "retrieveMissedCalls");
        ArrayList<Call> calls = new ArrayList<Call>();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " +
                    CallColumns.TABLE_NAME + " WHERE " + CallColumns.COL_IS_ACTIVE
                    + " = 1 " +"ORDER BY _ID DESC", null);

            if (cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    calls.add(new Call(
                            cursor.getString(cursor.getColumnIndex(CallColumns.COL_CALL_NUMBER)),
                            cursor.getString(cursor.getColumnIndex(CallColumns.COL_CALL_TIME)),
                            intToCallType(cursor.getInt(cursor.getColumnIndex(CallColumns.COL_CALL_STATUS)))
                    ));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calls;
    }

    public enum CallType {
        ANSWERED,
        MISSED,
        BLOCKED
    }
    */

    /*-----------API-----------*/
    /*-------Debug API --------*/

    /*
    public void debugInsertMissedCall (SQLiteDatabase db, String number, String dateTime, CallType type) {
        Log.d(TAG, "debugInsertMissedCall: " + number);
        try {
            if (BuildConfig.DEBUG) {
                // Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(CallColumns.COL_CALL_NUMBER, number);
                values.put(CallColumns.COL_CALL_TIME, dateTime);
                values.put(CallColumns.COL_CALL_STATUS, callTypeToInt(type));
                values.put(CallColumns.COL_IS_ACTIVE, 1);

                // Insert the new row, returning the primary key value of the new row
                db.insert(CallColumns.TABLE_NAME, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    /*-------Debug API --------*/
}
