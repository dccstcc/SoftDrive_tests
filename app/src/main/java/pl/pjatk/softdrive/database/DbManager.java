package pl.pjatk.softdrive.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class DbManager {

    public static final long MAX_ROW_COUNT = 200;

    public DbManager(Context context) {
        this.dbHelper = new DbHelper(context);

        distance = 0;
        scan2d = "null";

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

    //----------------------------------------------------------------------------------------------
    public long dbCommit() {
        // Gets the data repository in write mode
        SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();

        // clear database for more efficiency
        if(getRowCount(dbWrite) > MAX_ROW_COUNT)
            clearDb(dbWrite);

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(CreateTable.TableSensorData.COLUMN_NAME_DISTANCE, distance);
        values.put(CreateTable.TableSensorData.COLUMN_NAME_SCAN_2D, scan2d);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = dbWrite.insert(CreateTable.TableSensorData.TABLE_NAME, null, values);

        return newRowId;
    }

    private void clearDb(SQLiteDatabase dbWritable) {
        dbWritable.execSQL("delete from "+ CreateTable.TableSensorData.TABLE_NAME);
    }

    public long getRowCount(SQLiteDatabase dbWritable) {
        long count = DatabaseUtils.queryNumEntries(dbWritable, CreateTable.TableSensorData.TABLE_NAME);
        return count;
    }

    public int getDbDistance() {

        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                CreateTable.TableSensorData.COLUMN_NAME_DISTANCE
        };

        String selection = CreateTable.TableSensorData.COLUMN_NAME_DISTANCE + " = ?";
        String[] selectionArgs = { "120" };

        String sortOrder = CreateTable.TableSensorData._ID + " DESC";
        Cursor cursor = dbRead.query(
                CreateTable.TableSensorData.TABLE_NAME,
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,           // don't group the rows
                null,            // don't filter by row groups
                sortOrder
        );

        cursor.moveToFirst();
        int distance = cursor.getInt(1);
        cursor.close();

        return distance;
    }

//    //----------------------------------------------------------------------------------------------
//    public HashMap<Long, String> selectStringQuery(String columnType, int startId, int endId) {
//        HashMap<Long, String> queryResult = new HashMap<Long, String>();
//
//        switch(columnType) {
//            case CreateTable.TableEntry.COLUMN_NAME_YEAR_INTEGER:
//                throw new IllegalArgumentException("COLUMN_NAME_YEAR point into integer values, columnType must point into string type return values from database, change columnType on string point type.");
//            case CreateTable.TableEntry.COLUMN_NAME_THEME_ID_INTEGER:
//                throw new IllegalArgumentException("COLUMN_NAME_THEME_ID point into integer values, columnType must point into string type return values from database, change columnType on string point type.");
//            case CreateTable.TableEntry.COLUMN_NAME_NUM_PARTS_INTEGER:
//                throw new IllegalArgumentException("COLUMN_NAME_NUM_PARTS point into integer values, columnType must point into string type return values from database, change columnType on string point type.");
//        }
//
//        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
//
//        String[] projection = {
//                BaseColumns._ID,
//                columnType,
//        };
//
//        Cursor cursor = dbRead.query(
//                CreateTable.TableEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                null,              // The columns for the WHERE clause
//                null,          // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                null               // The sort order
//        );
//
//        while(cursor.moveToNext()) {
//
//            long itemId = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(CreateTable.TableEntry._ID));
//
//            if(itemId > endId) break;
//
//            if(itemId >= startId && itemId <= endId) {
//                String stringResult = cursor.getString(cursor.getColumnIndexOrThrow(columnType));
//
//                queryResult.put((long)itemId, stringResult);
//            }
//
//        }
//
//        cursor.close();
//        return queryResult;
//    }
//
//    //----------------------------------------------------------------------------------------------
//    public HashMap<Long, Integer> selectNumberQuery(String columnType, int startId, int endId) {
//
//        HashMap<Long, Integer> queryResult = new HashMap<Long, Integer>();
//
//        switch (columnType) {
//            case CreateTable.TableEntry.COLUMN_NAME_SET_NUM_STRING:
//                throw new IllegalArgumentException("COLUMN_NAME_YEAR point into string values, columnType must point into integer type return values from database, change columnType on integer point type.");
//            case CreateTable.TableEntry.COLUMN_NAME_NAME_STRING:
//                throw new IllegalArgumentException("COLUMN_NAME_NAME point into string values, columnType must point into integer type return values from database, change columnType on integer point type.");
//            case CreateTable.TableEntry.COLUMN_NAME_SET_IMG_URL_STRING:
//                throw new IllegalArgumentException("COLUMN_NAME_SET_IMG_URL point into string values, columnType must point into integer type return values from database, change columnType on integer point type.");
//            case CreateTable.TableEntry.COLUMN_NAME_SET_URL_STRING:
//                throw new IllegalArgumentException("COLUMN_NAME_SET_URL point into string values, columnType must point into integer type return values from database, change columnType on integer point type.");
//            case CreateTable.TableEntry.COLUMN_NAME_LAST_MODIFIED_DT_STRING:
//                throw new IllegalArgumentException("COLUMN_NAME_LAST_MODIFIED_DT point into string values, columnType must point into integer type return values from database, change columnType on integer point type.");
//        }
//
//        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
//
//        String[] projection = {
//                BaseColumns._ID,
//                columnType,
//        };
//
//        Cursor cursor = dbRead.query(
//                CreateTable.TableEntry.TABLE_NAME,   // The table to query
//                projection,             // The array of columns to return (pass null to get all)
//                null,              // The columns for the WHERE clause
//                null,         // The values for the WHERE clause
//                null,                   // don't group the rows
//                null,                   // don't filter by row groups
//                null               // The sort order
//        );
//        while (cursor.moveToNext()) {
//
//            long itemId = cursor.getLong(
//                    cursor.getColumnIndexOrThrow(CreateTable.TableEntry._ID));
//
//            if (itemId > endId) break;
//
//            if (itemId >= startId && itemId <= endId) {
//
//                int numberResult = cursor.getInt(cursor.getColumnIndexOrThrow(columnType));
//
//                queryResult.put(itemId, numberResult);
//            }
//
//        }
//
//        cursor.close();
//        return queryResult;
//    }
//
//    //==============================================================================================
//    public  ArrayList<BricksSingleSet> getAllSets() {
//        return getAllSets(0);
//    }
//
//    //==============================================================================================
//    public ArrayList<BricksSingleSet> getAllSets(int limit) {
//        ArrayList<BricksSingleSet> results = new ArrayList<>();
//        ArrayList<Map<String, String>> entriesAsMap = getAllEntries(limit);
//
//        entriesAsMap.forEach(entryAsMapItem -> {
//            BricksSingleSet entryAsSingleSetObject = convertMapSetToBricksSingleSet(entryAsMapItem);
//            results.add(entryAsSingleSetObject);
//        });
//
//        return results;
//    }
//
//    //==============================================================================================
//    public ArrayList<Map<String, String>> getAllEntries() {
//        return getAllEntries(0);
//    }
//
//    // =============================================================================================
//    public ArrayList<Map<String, String>> getAllEntries(int limit) {
//        //Log.d("DEBUG", String.format("==> getAllSets"));
//
//        SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
//
//        String[] projection = null;
//        String selection = null;
//        String[] selectionArgs = null;
//
//        String sortOrder = CreateTable.TableEntry._ID + " ASC";
//        Cursor cursor = dbRead.query(
//                CreateTable.TableEntry.TABLE_NAME,
//                projection,             // The array of columns to return (pass null to get all)
//                selection,              // The columns for the WHERE clause
//                selectionArgs,          // The values for the WHERE clause
//                null,           // don't group the rows
//                null,            // don't filter by row groups
//                sortOrder,
//                limit > 0 ? String.valueOf(limit) : null
//        );
//
//        return getQueryResults(cursor);
//    }
//
//    //----------------------------------------------------------------------------------------------
//    public ArrayList<BricksSingleSet> getSetsByName(String name) {
//        ArrayList<BricksSingleSet> results = new ArrayList<>();
//        ArrayList<Map<String, String>> entriesAsMap = getEntriesByName(name);
//
//        entriesAsMap.forEach(entryAsMapItem -> {
//            BricksSingleSet entryAsSingleSetObject = convertMapSetToBricksSingleSet(entryAsMapItem);
//            results.add(entryAsSingleSetObject);
//        });
//
//        return results;
//    }

    // ---------------------------------------------------------------------------------------------


//    //==============================================================================================
//    public ArrayList<Map<String, String>> getQueryResults(Cursor cursor) {
//        //Log.d("DEBUG", String.format("==> getQueryResults"));
//        ArrayList<Map<String, String>> results = new ArrayList<>();
//
//        cursor.moveToFirst();
//        for (int rowIndex = 0; rowIndex < cursor.getCount(); rowIndex++) {
//            Map<String, String> rowData;
//            rowData = getQueryResultAsSingleDbRow(cursor);
//            results.add(rowData);
//            cursor.moveToNext();
//        }
//
//        cursor.close();
//        return results;
//    }
//
//    //==============================================================================================
//    public int getQueryResultAsSingleDbRow(Cursor cursor) {
//
//        for (int columnIndex = 0; columnIndex < cursor.getColumnCount(); columnIndex++) {
//            String currentCursorColumnName = cursor.getColumnName(columnIndex);
//            int distance = cursor.getInt(1);
//            //Log.d("DEBUG", String.format("Column name: %s\tValue: %s", currentCursorColumnName, currentCursorColumnValue));
//            result.put(currentCursorColumnName, currentCursorColumnValue);
//        }
//        return result;
//    }
//
//    //==============================================================================================
//    public BricksSingleSet convertMapSetToBricksSingleSet(Map<String, String> singleSetAsMap) {
//        BricksSingleSet singleSet = new BricksSingleSet();
//        List<Method> singleSetMethods = Arrays.asList(singleSet.getClass().getDeclaredMethods());
//
//        for (Map.Entry<String, String> singleSetDbColumnAsEntry : singleSetAsMap.entrySet()) {
//            String columnName = singleSetDbColumnAsEntry.getKey().toLowerCase();
//            String columnValue = singleSetDbColumnAsEntry.getValue();
//
//            Method singleSetMatchedMethod = singleSetMethods
//                    .stream()
//                    .filter(method -> method
//                            .getName()
//                            .toLowerCase()
//                            .equals(String.format("set%s", columnName)))
//                    .collect(Collectors.toList()).get(0);
//
//            Class<?> singleSetMethodParameterType = singleSetMatchedMethod.getParameterTypes()[0];
//
//            Object parsedColumnValue = castColumnValueToType(singleSetMethodParameterType, columnValue);
//
//            try {
//                singleSetMatchedMethod.invoke(singleSet, parsedColumnValue);
//            } catch (IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
//        }
//        return singleSet;
//    }
//
//    //----------------------------------------------------------------------------------------------
//    public <Any> Any castColumnValueToType(Class<?> type, String columnValue) {
//        return (Any) (
//                type.getName().equals("int")
//                        ? Integer.parseInt(columnValue)
//                        : (type.getName().equals("long")
//                        ? Long.parseLong(columnValue)
//                        : columnValue
//                )
//        );
//    }
}
