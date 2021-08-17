package pl.pjatk.softdrive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Middleware class between custom database manager and DBMS
 * @author Dominik Stec
 * For see class pattern please visit
 * @link https://developer.android.com/training/data-storage/sqlite
 */
public class DbHelper extends SQLiteOpenHelper {

    /**
     * database variables
     */
    public static final int DATABASE_VERSION = 24;
    public static final String DATABASE_NAME = "SensorData.db";

    /**
     * Constructor of middleware helper class
     * @param context this Android application Context object
     */
    public DbHelper(Context context) {
        // If you change the pl.pjatk.softdrive.database schema, you must increment the pl.pjatk.softdrive.database version.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create table
     * @param db SQLite database object
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable.SQL_CREATE_TABLE_SENSOR_DATA);
    }

    /**
     * Renew create table query
     * @param db SQLite database object
     * @param oldVersion Old version of database
     * @param newVersion New version of database
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This pl.pjatk.softdrive.database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(CreateTable.SQL_DELETE_TABLE_SENSOR_DATA);
        onCreate(db);
    }

    /**
     * Renew create table query after version change
     * @param db SQLite database object
     * @param oldVersion Old version of database
     * @param newVersion New version of database
     */
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
