package pl.pjatk.softdrive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Customization of database middleware DbHelper
 * @see DbHelper
 */
public class DbManager extends DbHelper{

    /**
     * Clear database after MAX_ROW_COUNT commits limit
     */
    public static final long MAX_ROW_COUNT = 200;

    private ExecutorService ex;

    private DbHelper dbHelper;

    private int distance;
    private String scan2d;

    /**
     *  Constructor of customized middleware helper class
     *  @param context this Android application Context object
     */
    public DbManager(Context context) {
        super(context);

        dbHelper  = this;

        distance = 0;
        scan2d = "null";

        ex = Executors.newCachedThreadPool();
    }

    /**
     * Get actual distance from buffer to commit
     * @return actual set distance
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Set actual distance into buffer
     * @param distance Distance to set
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * TODO
     * @return
     */
    public String getScan2d() {
        return scan2d;
    }

    /**
     * TODO
     * @param scan2d
     */
    public void setScan2d(String scan2d) {
        this.scan2d = scan2d;
    }

    public boolean dbCommit() {

            // Gets the data repository in write mode
            SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();

            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE, distance);
            //values.put(CreateTable.TableSensorData.COLUMN_NAME_SCAN_2D, scan2d);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = dbWrite.insert(CreateTable.TableSensorData.TABLE_NAME, null, values);
            Log.v("database", "insert new row");

            // clear database for more efficiency
            if (getRowCount(dbWrite) > MAX_ROW_COUNT) {
                clearDb(dbWrite);
                ContentValues valuesDel = new ContentValues();
                valuesDel.put(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE, -1);
                dbWrite.insert(CreateTable.TableSensorData.TABLE_NAME, null, valuesDel);
                Log.v("database", "clear database rows");
            }

            dbWrite.close();

        return true;
    }

    private void clearDb(SQLiteDatabase dbWritable) {
        dbWritable.execSQL("delete from " + CreateTable.TableSensorData.TABLE_NAME);
    }

    public long getRowCount(SQLiteDatabase dbWritable) {
        long count = DatabaseUtils.queryNumEntries(dbWritable, CreateTable.TableSensorData.TABLE_NAME);
        return count;
    }

    public int getDbDistance() throws InterruptedException {

            SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

            String[] projection = {
                    CreateTable.TableSensorData.COLUMN_NAME_DISTANCE
            };

            Cursor cursor = dbRead.query(
                    CreateTable.TableSensorData.TABLE_NAME,
                    projection,             // The array of columns to return (pass null to get all)
                    null,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,           // don't group the rows
                    null,            // don't filter by row groups
                    null
            );

            int distance = -2;

            try {
                // retry cursor read data
                int count = 0;
                while(! cursor.moveToLast()) {
                    Thread.sleep(50);
                    count++;
                    if(count>20){
                        // if to much retries
                        cursor.close();
                        dbRead.close();
                        return -4;
                    }
                }
                // read distance from db
                distance = cursor.getInt(cursor.getColumnIndex(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE));
                cursor.close();
            } catch (CursorIndexOutOfBoundsException e) {
                distance = -3;
            }

            dbRead.close();

            return distance;
        }
}
