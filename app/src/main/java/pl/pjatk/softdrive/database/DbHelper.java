package pl.pjatk.softdrive.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 50;
    public static final String DATABASE_NAME = "SensorData.db";

    public DbHelper(Context context) {
        // If you change the pl.pjatk.softdrive.database schema, you must increment the pl.pjatk.softdrive.database version.
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable.SQL_CREATE_TABLE_SENSOR_DATA);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This pl.pjatk.softdrive.database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(CreateTable.SQL_DELETE_TABLE_SENSOR_DATA);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
