package pl.pjatk.softdrive.database;

import android.provider.BaseColumns;

public class CreateTable {

    public static class TableSensorData implements BaseColumns {
        public static final String TABLE_NAME = "sensor_data";
        public static final String COLUMN_NAME_DISTANCE = "distance";
        public static final String COLUMN_NAME_SCAN_2D = "scan_2d";
    }

    public static final String SQL_CREATE_TABLE_SENSOR_DATA =
            "CREATE TABLE " + TableSensorData.TABLE_NAME + " (" +
                    TableSensorData._ID + " INTEGER PRIMARY KEY," +
                    TableSensorData.COLUMN_NAME_DISTANCE + " INTEGER," +
                    TableSensorData.COLUMN_NAME_SCAN_2D + " TEXT)";

    public static final String SQL_DELETE_TABLE_SENSOR_DATA =
            "DROP TABLE IF EXISTS " + TableSensorData.TABLE_NAME;

}
