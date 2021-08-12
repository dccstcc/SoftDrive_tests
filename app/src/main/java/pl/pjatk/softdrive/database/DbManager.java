package pl.pjatk.softdrive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DbManager extends DbHelper{

    public static final long MAX_ROW_COUNT = 200;

    ExecutorService ex;

    public DbManager(Context context) {
        //this.dbHelper = new DbHelper(context);
        super(context);

        dbHelper  = this;

        distance = 0;
        scan2d = "null";

        ex = Executors.newCachedThreadPool();
    }

    DbHelper dbHelper;

    int distance;
    String scan2d;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getScan2d() {
        return scan2d;
    }

    public void setScan2d(String scan2d) {
        this.scan2d = scan2d;
    }

    public boolean dbCommit() throws ExecutionException, InterruptedException {
        System.out.println("rowval");

//        Callable<Long> ctask = () -> {

            // Gets the data repository in write mode
            SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();


            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE, distance);
            //values.put(CreateTable.TableSensorData.COLUMN_NAME_SCAN_2D, scan2d);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = dbWrite.insert(CreateTable.TableSensorData.TABLE_NAME, null, values);

            // clear database for more efficiency
            if (getRowCount(dbWrite) > MAX_ROW_COUNT) {
                clearDb(dbWrite);
            ContentValues valuesDel = new ContentValues();
            values.put(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE, 2000);
            dbWrite.insert(CreateTable.TableSensorData.TABLE_NAME, null, valuesDel);
//            setDistance(1);
//            ContentValues v = new ContentValues();
//            v.put(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE, distance);
//            dbWrite.insert(CreateTable.TableSensorData.TABLE_NAME, null, v);
            }

            dbWrite.close();
//            return newRowId;

//        };

//        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
//
//        ScheduledFuture<Long> sf = ses.schedule(ctask, 50, TimeUnit.MILLISECONDS);


//        boolean ret = false;
//        if (sf.isDone()) {
//            if(sf.get() > 0) {
//                ret = true;
//            }
//        }
//        return ret;

//        int count = 0;
//        while(! sf.isDone()) {
//            Thread.sleep(50);
//            ++count;
//            if(count>20) return false;
//        }

        return true;
    }



    private void clearDb(SQLiteDatabase dbWritable) {
        dbWritable.execSQL("delete from " + CreateTable.TableSensorData.TABLE_NAME);
    }

    public long getRowCount(SQLiteDatabase dbWritable) {
        long count = DatabaseUtils.queryNumEntries(dbWritable, CreateTable.TableSensorData.TABLE_NAME);
        return count;
    }


    public int getDbDistance() throws ExecutionException, InterruptedException {

//        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

//        Callable<Integer> ctask = () -> {

            SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

            String[] projection = {
                    CreateTable.TableSensorData.COLUMN_NAME_DISTANCE
            };

            String selection = CreateTable.TableSensorData.COLUMN_NAME_DISTANCE + " = ?";
            String[] selectionArgs = {"120"};

            //String sortOrder = CreateTable.TableSensorData._ID + " DESC";
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
                int count = 0;
                while(! cursor.moveToLast()) {
                    Thread.sleep(50);
                    count++;
                    if(count>20) return -4;
                }
                distance = cursor.getInt(cursor.getColumnIndex(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE));
                cursor.close();
            } catch (CursorIndexOutOfBoundsException e) {
                distance = -3;
            }

            dbRead.close();

            return distance;

        };

        //ScheduledFuture<Integer> sf = ses.scheduleAtFixedRate(ctask, 0, 250, TimeUnit.MILLISECONDS);

//        int count = 0;
//        while(! sf.isDone()) {
//            Thread.sleep(100);
//            ++count;
//            if(count>20) return -4;
//        }

//        return sf.get();
//    }
}
