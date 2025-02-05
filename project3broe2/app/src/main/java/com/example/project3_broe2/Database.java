package com.example.project3_broe2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    final public static String TABLE_NAME = "inventory";
    final static String ITEM_NAME = "task";
    final static String _ID = "_id";
    final static String ITEM_PRICE = "price";
    final static String ITEM_STOCK = "stock";
    final static String ITEM_DESCR = "descr";
    final static String ITEM_ORDER = "orderr";
    final public static String NAME = "list_db";
    final private static Integer VERSION = 2;
    final private Context context;
    final static String[] allColumns = { _ID, ITEM_NAME,
            ITEM_PRICE, ITEM_STOCK, ITEM_DESCR, ITEM_ORDER};
    public Database(Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("Made it here!");
        String CREATE_CMD = "CREATE TABLE " + TABLE_NAME + " (" + _ID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ITEM_NAME + " TEXT NOT NULL, " + ITEM_PRICE + " FLOAT NOT NULL, "
                + ITEM_STOCK + " INTEGER NOT NULL, " + ITEM_DESCR + " TEXT NOT NULL, " + ITEM_ORDER + " INTEGER NOT NULL)";
        System.out.println("No problem here!");
        db.execSQL(CREATE_CMD);
        // these inserts only run when the database is first created
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, "Mega Mushroom");
        values.put(ITEM_PRICE, 5.99f);
        values.put(ITEM_STOCK, 21);
        values.put(ITEM_DESCR, "Stomp all the competition!");
        values.put(ITEM_ORDER, 0);
        db.insert(TABLE_NAME,null,values);
        values.clear();
        values.put(ITEM_NAME, "Power Star");
        values.put(ITEM_PRICE, 999.99f);
        values.put(ITEM_STOCK, 1);
        values.put(ITEM_DESCR, "Blast your enemies to the stars!");
        values.put(ITEM_ORDER, 0);
        db.insert(TABLE_NAME,null,values);
        values.clear();
        values.put(ITEM_NAME, "Blooper");
        values.put(ITEM_PRICE, 65.88f);
        values.put(ITEM_STOCK, 14);
        values.put(ITEM_DESCR, "Blot out those who stand in your way!");
        values.put(ITEM_ORDER, 0);
        db.insert(TABLE_NAME,null,values);
        values.clear();
    }

    public void insert(String item_name, float item_price, int item_stock, String item_descr, int orderr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, item_name);
        values.put(ITEM_PRICE, item_price);
        values.put(ITEM_STOCK, item_stock);
        values.put(ITEM_DESCR, item_descr);
        values.put(ITEM_ORDER, orderr);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void delete(String item_name) {
        System.out.println("Delete!");
        SQLiteDatabase db = this.getWritableDatabase();
        int status =  db.delete(TABLE_NAME, ITEM_NAME + "=?",
                new String[] { item_name });
        db.close();
    }

    public Cursor readAll() {
        Cursor c = null;
        SQLiteDatabase db = this.getWritableDatabase();
        c =  db.query(TABLE_NAME, allColumns, null, new String[] {}, null, null,
                null);
        //       db.close();
        return c;
    }

    public void update(String oldName, float price, int stock, String descr, int orderr) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_NAME, oldName);
        values.put(ITEM_PRICE, price);
        values.put(ITEM_STOCK, stock);
        values.put(ITEM_DESCR, descr);
        values.put(ITEM_ORDER, orderr);
        //values.put(ITEM_PRICE, 5.99f);
        //values.put(ITEM_STOCK, 21);
        int status = db.update(TABLE_NAME, values, ITEM_NAME + "=?",
                new String[] { oldName });
        db.close();
    }

    private void clearAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);


    }

    void deleteDatabase ( ) {
        context.deleteDatabase(NAME);
    }
}

