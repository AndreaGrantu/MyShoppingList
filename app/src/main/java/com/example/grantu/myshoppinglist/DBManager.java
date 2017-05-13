package com.example.grantu.myshoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.grantu.myshoppinglist.Classes.ShoppingHistoryItem;
import com.example.grantu.myshoppinglist.Classes.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grantu on 10/12/2016.
 */
public class DBManager {

    private static final String DATABASE_NAME = "MyShoppingList.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "DBManager";


    public static final class ShoppingListEntry implements BaseColumns{
        public static final String TABLE_NAME = "SHOPPING_LIST";
        public static final String COLUMN_PRODUCT_NAME = "PRODUCT";
        public static final String COLUMN_AMOUNT = "AMOUNT";
        public static final String COLUMN_PRICE = "PRICE";
        public static final String COLUMN_IS_CHECKED = "IS_CHECKED";
    }

    public static final class ShoppingListHistory implements BaseColumns{
        public static final String TABLE_NAME = "SHOPPING_HISTORY";
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_DATE = "DATE";
        public static final String COLUMN_CONTENT = "CONTENT";
        public static final String COLUMN_NOTES = "NOTES";
        public static final String COLUMN_TOT_PRICE = "TOT_PRICE";
    }


    private final static String TABLE_SHOPPING_ENTRY_LIST_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ShoppingListEntry.TABLE_NAME
            + " ("
            + ShoppingListEntry._ID  		                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + ShoppingListEntry.COLUMN_PRODUCT_NAME			+ " TEXT NOT NULL,"
            + ShoppingListEntry.COLUMN_AMOUNT               + " TEXT,"
            + ShoppingListEntry.COLUMN_PRICE                + " TEXT,"
            + ShoppingListEntry.COLUMN_IS_CHECKED           + " BOOLEAN"
            + ");";
    private final static String TABLE_SHOPPING_HISTORY_CREATE = "CREATE TABLE IF NOT EXISTS "
            + ShoppingListHistory.TABLE_NAME
            + " ("
            + ShoppingListHistory._ID  		                + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + ShoppingListHistory.COLUMN_NAME            + " TEXT,"
            + ShoppingListHistory.COLUMN_DATE			    + " TEXT,"
            + ShoppingListHistory.COLUMN_CONTENT            + " TEXT,"
            + ShoppingListHistory.COLUMN_NOTES            + " TEXT,"
            + ShoppingListHistory.COLUMN_TOT_PRICE          + " TEXT"
            + ");";

    private final static String[] SHOPPING_LIST_PROJECTION = {
            ShoppingListEntry._ID,
            ShoppingListEntry.COLUMN_PRODUCT_NAME,
            ShoppingListEntry.COLUMN_AMOUNT,
            ShoppingListEntry.COLUMN_PRICE,
            ShoppingListEntry.COLUMN_IS_CHECKED
    };
    private final static String[] SHOPPING_HISTORY_PROJECTION = {
            ShoppingListHistory._ID,
            ShoppingListHistory.COLUMN_NAME,
            ShoppingListHistory.COLUMN_DATE,
            ShoppingListHistory.COLUMN_CONTENT ,
            ShoppingListHistory.COLUMN_NOTES ,
            ShoppingListHistory.COLUMN_TOT_PRICE
    };

    private static final String TABLE_SHOPPING_LIST_DELETE =
            "DROP TABLE IF EXISTS " + ShoppingListEntry.TABLE_NAME;

    private static final String TABLE_SHOPPING_HISTORY_DELETE =
            "DROP TABLE IF EXISTS " + ShoppingListHistory.TABLE_NAME;






    private static DBHelper dbHelper;
    private final Context mContext;
    private static DBManager dbManager = null;

    private DBManager(Context mContext){
        Context tmp = mContext.getApplicationContext();
        this.dbHelper = DBHelper.getInstance(tmp);
        this.mContext = tmp.getApplicationContext();
    }


    public static synchronized DBManager getInstance(Context context){

        if(dbManager == null){
            dbManager = new DBManager(context);
        }

        return dbManager;
    }


    public SQLiteDatabase getWritableDatabase() {
        return dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDatabase() {
        return dbHelper.getReadableDatabase();
    }

    private static synchronized SQLiteDatabase getDatabase(){

        SQLiteDatabase db = dbManager.getWritableDatabase();
        if(db ==null || !db.isOpen() || db.isDbLockedByCurrentThread()){
            return null;
        }
        return  db;
    }



    // ShoppingItem Management
    public boolean insertProduct(ShoppingItem s){
        ContentValues cv = getContentValuesShoppingItem(s);
        SQLiteDatabase db = getDatabase();
        if(null == db){
            return false;
        }

        if(db.insert(ShoppingListEntry.TABLE_NAME,null,cv) == -1){
            return false;
        } else {
            return true;
        }

    }

    public boolean deleteProduct(ShoppingItem s){
        SQLiteDatabase db = getDatabase();
        int id=s.getId();

        try {
            if (db == null) {
                return false;
            }

            if (db.delete(ShoppingListEntry.TABLE_NAME, ShoppingListEntry._ID + " = ?", new String[]{String.valueOf(id)}) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            Log.d(TAG,Log.getStackTraceString(e));
              return false;
        }
    }

    public boolean updateProduct(ShoppingItem s){
        ContentValues cv = getContentValuesShoppingItem(s);
        int id=s.getId();
        SQLiteDatabase db = getDatabase();
        try {
            if (db == null) {
                return false;
            }

            if (db.update(ShoppingListEntry.TABLE_NAME, cv, ShoppingListEntry._ID + " = ?", new String[]{String.valueOf(id)}) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            Log.d(TAG,Log.getStackTraceString(e));
            return false;
        }

    }


    private ContentValues getContentValuesShoppingItem(ShoppingItem s){

        ContentValues cv = new ContentValues();
        cv.put(ShoppingListEntry.COLUMN_PRODUCT_NAME,s.getName());
        cv.put(ShoppingListEntry.COLUMN_PRICE,s.getPrice());
        cv.put(ShoppingListEntry.COLUMN_IS_CHECKED,s.isChecked());
        cv.put(ShoppingListEntry.COLUMN_AMOUNT,s.getAmount());
        return cv;

    }

    public List<ShoppingItem> getAllShoppingItems(){
        List<ShoppingItem> list = new ArrayList<>();
        SQLiteDatabase db = getDatabase();
        if(db==null){
            return list;
        }
        Cursor cursor=db.query(ShoppingListEntry.TABLE_NAME, SHOPPING_LIST_PROJECTION, null, null, null, null, null);
        if(cursor.moveToFirst()){
         do{
             ShoppingItem s = new ShoppingItem();
             s.setId(cursor.getInt(cursor.getColumnIndex(ShoppingListEntry._ID)));
             s.setName(cursor.getString(cursor.getColumnIndex(ShoppingListEntry.COLUMN_PRODUCT_NAME)));
             s.setAmount(cursor.getString(cursor.getColumnIndex(ShoppingListEntry.COLUMN_AMOUNT)));
             s.setPrice(cursor.getString(cursor.getColumnIndex(ShoppingListEntry.COLUMN_PRICE)));
             s.setIsChecked((cursor.getInt(cursor.getColumnIndex(ShoppingListEntry.COLUMN_IS_CHECKED)) > 0));

             list.add(s);
         } while(cursor.moveToNext());
        }
        return list;
    }

    public ShoppingItem getShopItem(int id){
        ShoppingItem item = new ShoppingItem();
        SQLiteDatabase db = getDatabase();
        if(db==null){
            return item;
        }
        Cursor cursor=db.query(ShoppingListEntry.TABLE_NAME, SHOPPING_LIST_PROJECTION, ShoppingListEntry._ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.moveToFirst()){
            do{
                item.setId(cursor.getInt(cursor.getColumnIndex(ShoppingListEntry._ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(ShoppingListEntry.COLUMN_PRODUCT_NAME)));
                item.setAmount(cursor.getString(cursor.getColumnIndex(ShoppingListEntry.COLUMN_AMOUNT)));
                item.setPrice(cursor.getString(cursor.getColumnIndex(ShoppingListEntry.COLUMN_PRICE)));
                item.setIsChecked((cursor.getInt(cursor.getColumnIndex(ShoppingListEntry.COLUMN_IS_CHECKED)) > 0));
            } while(cursor.moveToNext());
        }
        return item;


    }

    //SHoppingItemHistory Management

    private ContentValues getContentValuesShoppingHistoryItem(ShoppingHistoryItem s){

        ContentValues cv = new ContentValues();
        cv.put(ShoppingListHistory.COLUMN_NAME,s.getName());
        cv.put(ShoppingListHistory.COLUMN_TOT_PRICE,s.getTot_price());
        cv.put(ShoppingListHistory.COLUMN_CONTENT,s.getContent());
        cv.put(ShoppingListHistory.COLUMN_NOTES,s.getNotes());
        cv.put(ShoppingListHistory.COLUMN_DATE, s.getDate());
        return cv;

    }


    public boolean insertHistoryList(ShoppingHistoryItem s){
        ContentValues cv = getContentValuesShoppingHistoryItem(s);
        SQLiteDatabase db = getDatabase();
        if(db ==null){
            return false;
        }

        if(db.insert(ShoppingListHistory.TABLE_NAME,null,cv) == -1){
            return false;
        } else {
            return true;
        }

    }

    public boolean deleteHistoryList(int id){
        SQLiteDatabase db = getDatabase();
        try {
            if (db == null) {
                return false;
            }

            if (db.delete(ShoppingListHistory.TABLE_NAME, ShoppingListHistory._ID + " = ?", new String[]{String.valueOf(id)}) > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e){
            Log.d(TAG,Log.getStackTraceString(e));
            return false;
        }
    }

    public List<ShoppingHistoryItem> getAllShoppingHistory(){
        List<ShoppingHistoryItem> list = new ArrayList<>();
        SQLiteDatabase db = getDatabase();
        if(db==null){
            return list;
        }
        Cursor cursor=db.query(ShoppingListHistory.TABLE_NAME, SHOPPING_HISTORY_PROJECTION, null, null, null, null, null);
        if(cursor.moveToFirst()){
            while(cursor.moveToNext()){
                ShoppingHistoryItem s = new ShoppingHistoryItem();
                s.setId(cursor.getInt(cursor.getColumnIndex(ShoppingListHistory._ID)));
                s.setName(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_NAME)));
                s.setContent(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_CONTENT)));
                s.setDate(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_DATE)));
                s.setTot_price(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_TOT_PRICE)));
                s.setNotes(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_NOTES)));
                list.add(s);
            }
        }
        return list;
    }


    public ShoppingHistoryItem getHistoryItem(int id){
        ShoppingHistoryItem item = new ShoppingHistoryItem();
        SQLiteDatabase db = getDatabase();
        if(db==null){
            return item;
        }
        Cursor cursor=db.query(ShoppingListHistory.TABLE_NAME, SHOPPING_HISTORY_PROJECTION, ShoppingListHistory._ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        if(cursor.moveToFirst()){
            do{
                item.setId(cursor.getInt(cursor.getColumnIndex(ShoppingListEntry._ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_NAME)));
                item.setTot_price(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_TOT_PRICE)));
                item.setContent(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_CONTENT)));
                item.setNotes(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_NOTES)));
                item.setDate(cursor.getString(cursor.getColumnIndex(ShoppingListHistory.COLUMN_DATE)));
            } while(cursor.moveToNext());
        }
        return item;


    }
    //class helper
    private static class DBHelper extends SQLiteOpenHelper {

        private static DBHelper sInstance;


        private static synchronized DBHelper getInstance(Context context){
            if(sInstance == null){
                sInstance = new DBHelper(context.getApplicationContext());
            }

            return sInstance;
        }

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //I create the db
            db.execSQL(TABLE_SHOPPING_ENTRY_LIST_CREATE);
            db.execSQL(TABLE_SHOPPING_HISTORY_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if(newVersion != oldVersion){
                db.execSQL(TABLE_SHOPPING_HISTORY_DELETE);
                db.execSQL(TABLE_SHOPPING_LIST_DELETE);
            }

            onCreate(db);
        }


    }



}
