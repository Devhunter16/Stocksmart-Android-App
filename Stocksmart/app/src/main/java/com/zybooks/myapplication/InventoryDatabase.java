package com.zybooks.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class InventoryDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final int VERSION = 1;

    public InventoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    };

    public static final class InventoryTable {
        public static final String TABLE = "inventory";
        public static final String COL_ID = "_id";
        public static final String COL_DESCRIPTION = "description";
        public static final String COL_QUANTITY = "quantity";
        public static final String COL_UNIT = "unit";
    };

    @Override
    public void onCreate (SQLiteDatabase db) {
        db.execSQL("create table " + InventoryDatabase.  InventoryTable.TABLE + " (" +
                InventoryDatabase.InventoryTable.COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                InventoryDatabase.InventoryTable.COL_DESCRIPTION + " text, " +
                InventoryDatabase.InventoryTable.COL_QUANTITY + " integer, " +
                InventoryDatabase.InventoryTable.COL_UNIT + " text)");
    };

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InventoryDatabase.InventoryTable.TABLE);
        onCreate(db);
    };

    // Delete item from the database and return a list of them
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(InventoryDatabase.InventoryTable.TABLE, InventoryDatabase.InventoryTable.COL_ID + " = ?", new String[] { String.valueOf(item.getId()) });
        db.close();
    };

    // Get all of the items in the database
    public ArrayList<Item> getAllItems() {
        ArrayList<Item> itemList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = {InventoryTable.COL_ID, InventoryTable.COL_DESCRIPTION, InventoryTable.COL_QUANTITY, InventoryTable.COL_UNIT};

        Cursor cursor = db.query(InventoryTable.TABLE, columns, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int itemId = cursor.getInt(cursor.getColumnIndex(InventoryTable.COL_ID));
                String description = cursor.getString(cursor.getColumnIndex(InventoryTable.COL_DESCRIPTION));
                String quantity = cursor.getString(cursor.getColumnIndex(InventoryTable.COL_QUANTITY));
                String unit = cursor.getString(cursor.getColumnIndex(InventoryTable.COL_UNIT));

                Item item = new Item(itemId, description, quantity, unit);
                itemList.add(item);
            } while (cursor.moveToNext());

            cursor.close();
        };

        return itemList;
    };

    // Getting item count
    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + InventoryDatabase.InventoryTable.TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int itemsTotal = cursor.getCount();
        cursor.close();

        return itemsTotal;
    };
};
