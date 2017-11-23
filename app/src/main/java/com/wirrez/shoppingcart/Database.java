package com.wirrez.shoppingcart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import android.util.Log;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walteda on 19.11.2017.
 */

public class Database extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "shoppingList.db";

    // Used Items
    String[] UsedItemsEntryColumn = {UsedItemsEntry._ID, UsedItemsEntry.KEY_NAME};

    public static class UsedItemsEntry implements BaseColumns {
        public static final String TABLE_NAME = "UsedItems";
        public static final String KEY_NAME = "name";
    }

    // Used units
    String[] UsedUnitsEntryColumn = {UsedUnitsEntry._ID, UsedUnitsEntry.KEY_NAME};

    public static class UsedUnitsEntry implements BaseColumns {
        public static final String TABLE_NAME = "UsedUnits";
        public static final String KEY_NAME = "name";
    }

    // Items Table Columns names
    String[] ItemEntryColumn = {ItemEntry._ID, ItemEntry.KEY_NAME, ItemEntry.KEY_CROSS, ItemEntry.KEY_COUNT, ItemEntry.KEY_UNIT};

    public static class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "Items";
        public static final String KEY_NAME = "name";
        public static final String KEY_COUNT = "count";
        public static final String KEY_UNIT = "unit";
        public static final String KEY_CAT_ID = "categoryID";
        public static final String KEY_CROSS = "cross";
        public static final String KEY_ICON = "icon";
    }

    // Category Items Table Columns names
    String[] CategoryItemEntryColumn = {CategoryItemEntry._ID, CategoryItemEntry.KEY_NAME, CategoryItemEntry.KEY_ICON};

    public static class CategoryItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "CategoryItems";
        public static final String KEY_NAME = "name";
        public static final String KEY_ICON = "icon";
    }

    //Statemens
    private static final String SQL_CREATE_ENTRIES[] =
            {
                    "CREATE TABLE " + ItemEntry.TABLE_NAME + " (" +
                            ItemEntry._ID + " INTEGER PRIMARY KEY," +
                            ItemEntry.KEY_NAME + " TEXT," +
                            ItemEntry.KEY_COUNT + " TEXT," +
                            ItemEntry.KEY_UNIT + " TEXT," +
                            ItemEntry.KEY_CROSS + " TEXT," +
                            ItemEntry.KEY_CAT_ID + " TEXT," +
                            ItemEntry.KEY_ICON + " TEXT)",
                    "CREATE TABLE " + CategoryItemEntry.TABLE_NAME + " (" +
                            CategoryItemEntry._ID + " INTEGER PRIMARY KEY," +
                            CategoryItemEntry.KEY_NAME + " TEXT," +
                            CategoryItemEntry.KEY_ICON + " TEXT)",
                    "CREATE TABLE " + UsedItemsEntry.TABLE_NAME + " (" +
                            UsedItemsEntry._ID + " INTEGER PRIMARY KEY," +
                            UsedItemsEntry.KEY_NAME + " TEXT)",
                    "CREATE TABLE " + UsedUnitsEntry.TABLE_NAME + " (" +
                            UsedUnitsEntry._ID + " INTEGER PRIMARY KEY," +
                            UsedUnitsEntry.KEY_NAME + " TEXT)",

            };

    private static final String SQL_DELETE_ENTRIES[] =
            {
                    "DROP TABLE IF EXISTS " + ItemEntry.TABLE_NAME,
                    "DROP TABLE IF EXISTS " + CategoryItemEntry.TABLE_NAME,
                    "DROP TABLE IF EXISTS " + UsedUnitsEntry.TABLE_NAME,
                    "DROP TABLE IF EXISTS " + UsedItemsEntry.TABLE_NAME
            };

    //Functions
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database", "Crated");
        for (String stat : SQL_CREATE_ENTRIES) {
            db.execSQL(stat);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        for (String stat : SQL_DELETE_ENTRIES) {
            db.execSQL(stat);
        }
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //Inserting
    public long InsertCategoryItem(String name, GoogleMaterial.Icon icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryItemEntry.KEY_NAME, name);
//        values.put(CategoryItemEntry.KEY_ICON, icon.getName()); // TODO
        values.put(CategoryItemEntry.KEY_ICON, ""); // TODO
        long id = db.insert(CategoryItemEntry.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long InsertItem(String name, String count, long catId, String unit) {
        this.removeUsed(name, unit);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ItemEntry.KEY_COUNT, count);
        values.put(ItemEntry.KEY_UNIT, unit);
        values.put(ItemEntry.KEY_NAME, name);
        values.put(ItemEntry.KEY_CAT_ID, catId);
        long id = db.insert(ItemEntry.TABLE_NAME, null, values);
        //Used item and units
        values.clear();
        values.put(UsedUnitsEntry.KEY_NAME, unit);
        db.insert(UsedUnitsEntry.TABLE_NAME, null, values);
        values.clear();
        values.put(UsedItemsEntry.KEY_NAME, name);
        db.insert(UsedItemsEntry.TABLE_NAME, null, values);

        db.close();
        return id;
    }

    //Getters
    public PrimaryDrawerItem[] GetCategoryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(CategoryItemEntry.TABLE_NAME, CategoryItemEntryColumn, null, null, null, null, null);
        List<PrimaryDrawerItem> Items = new ArrayList<>();

        while (cur.moveToNext()) {
            long id = cur.getLong(cur.getColumnIndexOrThrow(CategoryItemEntry._ID));
            String valueIcon = cur.getString(cur.getColumnIndex(CategoryItemEntry.KEY_ICON));
            GoogleMaterial.Icon icon = null;
            if (!valueIcon.isEmpty()) {
                icon = GoogleMaterial.Icon.valueOf(valueIcon);
            }
            Items.add(new CategoryItem(id, cur.getString(cur.getColumnIndex(CategoryItemEntry.KEY_NAME)),
                    icon, getCountItemsOfCategory(id)).getPrimaryDrawer());
        }

        cur.close();
        db.close();
        return Items.toArray(new PrimaryDrawerItem[Items.size()]);
    }
    public long getFirstCategoryID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(CategoryItemEntry.TABLE_NAME, CategoryItemEntryColumn, null, null, null, null, null);
            cur.moveToFirst();
            try{
                return cur.getLong(cur.getColumnIndex(CategoryItemEntry._ID));
            }catch (Exception e)
            {
                return 1;
            }
    }

    public ArrayList<Item> getItems(long id) {
        ArrayList<Item> res = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(ItemEntry.TABLE_NAME, ItemEntryColumn, ItemEntry.KEY_CAT_ID + "= ?", new String[]{String.valueOf(id)}, null, null, null);

        while (cur.moveToNext()) {
            res.add(new Item(cur.getLong(cur.getColumnIndexOrThrow(ItemEntry._ID)),
                    cur.getString(cur.getColumnIndex(ItemEntry.KEY_NAME)),
                    cur.getInt(cur.getColumnIndex(ItemEntry.KEY_COUNT)),
                    cur.getString(cur.getColumnIndex(ItemEntry.KEY_UNIT)),
                    null,
                    cur.getInt(cur.getColumnIndex(ItemEntry.KEY_CROSS)) > 0));
        }
        cur.close();
        db.close();
        return res;

    }

    //Update
    public boolean updateCategory(long id, String title, Drawable icon) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        ContentValues values = new ContentValues();
        values.put(CategoryItemEntry.KEY_NAME, title);
        values.put(CategoryItemEntry.KEY_ICON, "");

        result = db.update(CategoryItemEntry.TABLE_NAME, values, CategoryItemEntry._ID + " = ?", new String[]{String.valueOf(id)});

        db.close();
        return (result == 1);
    }

    public boolean updateItem(long id, String name, String qty, String units) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.query(ItemEntry.TABLE_NAME, null, ItemEntry._ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        cur.moveToFirst();
        String OldName = cur.getString(cur.getColumnIndex(ItemEntry.KEY_NAME));
        String OldUnit = cur.getString(cur.getColumnIndex(ItemEntry.KEY_UNIT));
        cur.close();
        db.close();
        this.removeUsed(OldName, OldUnit);

        db = this.getWritableDatabase();
        int result;
        ContentValues values = new ContentValues();
        values.put(ItemEntry.KEY_NAME, name);
        values.put(ItemEntry.KEY_COUNT, qty);
        values.put(ItemEntry.KEY_UNIT, units);
        result = db.update(ItemEntry.TABLE_NAME, values, ItemEntry._ID + " = ?", new String[]{String.valueOf(id)});

        //Used item and units
        values.clear();
        values.put(UsedUnitsEntry.KEY_NAME, units);
        db.insert(UsedUnitsEntry.TABLE_NAME, null, values);
        values.clear();
        values.put(UsedItemsEntry.KEY_NAME, name);
        db.insert(UsedItemsEntry.TABLE_NAME, null, values);

        db.close();

        return (result == 1);
    }

    //Delete
    public long deleteCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteCountItems = db.delete(ItemEntry.TABLE_NAME, ItemEntry.KEY_CAT_ID + "=?", new String[]{String.valueOf(id)});
        int deleteCount = db.delete(CategoryItemEntry.TABLE_NAME, CategoryItemEntry._ID + "=?", new String[]{String.valueOf(id)});

        db.close();
        if (deleteCount > 0) {
            long retId;
            //find first Id
            db = this.getReadableDatabase();
            Cursor cur = db.query(CategoryItemEntry.TABLE_NAME, CategoryItemEntryColumn, null, null, null, null, null);
            cur.moveToFirst();
            try {
                retId = cur.getLong(cur.getColumnIndexOrThrow(CategoryItemEntry._ID));
            } catch (Exception e) {
                retId = 0;
            }
            cur.close();
            db.close();
            return retId;
        }
        return id;

    }

    public void removeUsed(String item, String unit) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UsedItemsEntry.TABLE_NAME, UsedItemsEntry.KEY_NAME + "=?", new String[]{item});
        db.delete(UsedUnitsEntry.TABLE_NAME, UsedUnitsEntry.KEY_NAME + "=?", new String[]{unit});
        db.close();
    }

    //Remove from autocomplete
    public boolean deleteItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.query(ItemEntry.TABLE_NAME, null, ItemEntry._ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        cur.moveToFirst();
        String name = cur.getString(cur.getColumnIndex(ItemEntry.KEY_NAME));
        String unit = cur.getString(cur.getColumnIndex(ItemEntry.KEY_UNIT));
        cur.close();
        int deleteCount = db.delete(ItemEntry.TABLE_NAME, ItemEntry._ID + "=?", new String[]{String.valueOf(id)});
        db.close();

        this.removeUsed(name, unit);

        return deleteCount > 0;
    }

    public void cleanCrossed(long lastSelection) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deleteCount = db.delete(ItemEntry.TABLE_NAME, ItemEntry.KEY_CAT_ID + "=? and  " + ItemEntry.KEY_CROSS + "!= 0", new String[]{String.valueOf(lastSelection)});
        db.close();
    }

    //Other
    public long getCountItemsOfCategory(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.query(ItemEntry.TABLE_NAME, null, ItemEntry.KEY_CAT_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        cur.moveToFirst();
        long count = cur.getCount();
        cur.close();
        db.close();
        return count;
    }

    public boolean crossItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;
        Cursor cur = db.query(ItemEntry.TABLE_NAME, null, ItemEntry._ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        cur.moveToFirst();
        boolean cross = cur.getInt(cur.getColumnIndex(ItemEntry.KEY_CROSS)) > 0;

        cur.close();
        ContentValues values = new ContentValues();
        values.put(ItemEntry.KEY_CROSS, !cross);

        result = db.update(ItemEntry.TABLE_NAME, values, ItemEntry._ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return (result == 1);

    }

    public List<String> getUsedUnits() {
        ArrayList<String> values = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cur = db.query(UsedUnitsEntry.TABLE_NAME, UsedUnitsEntryColumn, null, null, null, null, null);

        while (cur.moveToNext()) {
            values.add(cur.getString(cur.getColumnIndex(UsedUnitsEntry.KEY_NAME)));
        }

        db.close();
        return values;
    }

    public String[] getUsedItems() {
        ArrayList<String> values = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.query(UsedItemsEntry.TABLE_NAME, UsedItemsEntryColumn, null, null, null, null, null);

        while (cur.moveToNext()) {
            values.add(cur.getString(cur.getColumnIndex(UsedItemsEntry.KEY_NAME)));
        }
        db.close();

        String[] stockArr = new String[values.size()];
        stockArr = values.toArray(stockArr);

        return stockArr;
    }
}
