package pl.pjatk.softdrive.database;

import android.provider.BaseColumns;

/**
 * Contains required SQL queries
 * @author Dominik Stec
 * For see class pattern please visit
 * @link https://developer.android.com/training/data-storage/sqlite
 */
public class CreateTable {

    /**
     * Contains name of SQL tables
     */
    public static class TableSensorData implements BaseColumns {
        public static final String TABLE_NAME = "sensor_data";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_SCAN_2D = "scan_2d";
    }

    /**
     * Contains create table SQL structure
     */
    public static final String SQL_CREATE_TABLE_SENSOR_DATA =
            "CREATE TABLE " + TableSensorData.TABLE_NAME + " (" +
                    TableSensorData._ID + " INTEGER PRIMARY KEY," +
                    TableSensorData.COLUMN_NAME_DISTANCE + " INTEGER," +
                    TableSensorData.COLUMN_NAME_SCAN_2D + " TEXT)";

    /**
     * Contains delete table SQL query
     */
    public static final String SQL_DELETE_TABLE_SENSOR_DATA =
            "DROP TABLE IF EXISTS " + TableSensorData.TABLE_NAME;

}
