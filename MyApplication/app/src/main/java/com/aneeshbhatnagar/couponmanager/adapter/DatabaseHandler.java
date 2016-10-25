package com.aneeshbhatnagar.couponmanager.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aneeshbhatnagar.couponmanager.model.Coupon;
import com.aneeshbhatnagar.couponmanager.model.Store;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    //Static Variables
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "couponManager";
    private static final String TABLE_STORE = "stores";
    private static final String TABLE_COUPON = "coupons";

    //Columns for Store Table
    private static final String STORE_ID = "store_id";
    private static final String STORE_NAME = "store_name";
    private static final String STORE_TYPE = "store_type";
    private static final String STORE_URL = "store_url";

    //Columns for Coupon table
    private static final String COUPON_ID = "coupon_id";
    private static final String COUPON_CODE = "coupon_code";
    private static final String COUPON_DESC = "coupon_desc";
    private static final String COUPON_USED = "coupon_used";
    private static final String COUPON_EXPIRY = "coupon_expiry";
    private static final String COUPON_STORE_ID = "coupon_store_id";
    private static final String COUPON_TYPE = "coupon_type";
    private static final String COUPON_VALUE = "coupon_value";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STORE_TABLE = "CREATE TABLE " + TABLE_STORE + "("
                + STORE_ID + " INTEGER PRIMARY KEY," + STORE_NAME + " TEXT,"
                + STORE_TYPE + " INTEGER," + STORE_URL + " TEXT" + ")";
        String CREATE_COUPON_TABLE = "CREATE TABLE " + TABLE_COUPON + "("
                + COUPON_ID + " INTEGER PRIMARY KEY," + COUPON_CODE + " TEXT,"
                + COUPON_DESC + " TEXT," + COUPON_EXPIRY + " TEXT,"
                + COUPON_STORE_ID + " INTEGER," + COUPON_TYPE + " INTEGER,"
                + COUPON_USED + " INTEGER," + COUPON_VALUE + " REAL" +  ")";

        db.execSQL(CREATE_STORE_TABLE);
        db.execSQL(CREATE_COUPON_TABLE);
        Log.d("DATABASE CREATED", "Complered");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUPON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORE);
        onCreate(db);
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    /* Methods Related to Coupon Class*/
    public void addCoupon(Coupon coupon){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COUPON_CODE, coupon.getCode());
        values.put(COUPON_DESC, coupon.getDesc());
        values.put(COUPON_EXPIRY, coupon.getExpiry());
        values.put(COUPON_STORE_ID, coupon.getStoreId());
        values.put(COUPON_TYPE, coupon.getType());
        values.put(COUPON_USED, coupon.getUsed());
        values.put(COUPON_VALUE, coupon.getValue());
        db.insert(TABLE_COUPON, null, values);
    }

    public Coupon getCoupon(int id){
        Coupon coupon = new Coupon();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_COUPON, new String[]{COUPON_ID, COUPON_CODE, COUPON_DESC,
                        COUPON_EXPIRY, COUPON_STORE_ID, COUPON_TYPE, COUPON_USED, COUPON_VALUE},
                COUPON_ID + "=?", new String[]{Integer.toString(id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            coupon.setId(Integer.parseInt(c.getString(0)));
            coupon.setCode(c.getString(1));
            coupon.setDesc(c.getString(2));
            coupon.setExpiry(c.getString(3));
            coupon.setStoreId(Integer.parseInt(c.getString(4)));
            coupon.setType(Integer.parseInt(c.getString(5)));
            coupon.setUsed(Integer.parseInt(c.getString(6)));
            coupon.setValue(Float.parseFloat(c.getString(7)));
        }
        return coupon;
    }

    public List<Coupon> getCouponsUsed(int id){
        List<Coupon> couponList = new ArrayList<Coupon>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_COUPON, new String[]{COUPON_ID, COUPON_CODE, COUPON_DESC,
                        COUPON_EXPIRY, COUPON_STORE_ID, COUPON_TYPE, COUPON_USED, COUPON_VALUE},
                COUPON_USED + "=?", new String[]{Integer.toString(id)}, null, null, null);

        if(c.moveToFirst()){
            do{
                Coupon coupon = new Coupon();
                coupon.setId(Integer.parseInt(c.getString(0)));
                coupon.setCode(c.getString(1));
                coupon.setDesc(c.getString(2));
                coupon.setExpiry(c.getString(3));
                coupon.setStoreId(Integer.parseInt(c.getString(4)));
                coupon.setType(Integer.parseInt(c.getString(5)));
                coupon.setUsed(Integer.parseInt(c.getString(6)));
                coupon.setValue(Float.parseFloat(c.getString(7)));
                couponList.add(coupon);
            }while(c.moveToNext());
        }
        return couponList;
    }

    public List<Coupon> getAllCoupons(){
        List<Coupon> couponList = new ArrayList<Coupon>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_COUPON, null);

        if(c.moveToFirst()){
            do{
                Coupon coupon = new Coupon();
                coupon.setId(Integer.parseInt(c.getString(0)));
                coupon.setCode(c.getString(1));
                coupon.setDesc(c.getString(2));
                coupon.setExpiry(c.getString(3));
                coupon.setStoreId(Integer.parseInt(c.getString(4)));
                coupon.setType(Integer.parseInt(c.getString(5)));
                coupon.setUsed(Integer.parseInt(c.getString(6)));
                coupon.setValue(Float.parseFloat(c.getString(7)));
                couponList.add(coupon);
            }while(c.moveToNext());
        }
        return couponList;
    }

    public List<Coupon> getStoreCoupons(int store_id){
        List<Coupon> couponList = new ArrayList<Coupon>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_COUPON, new String[]{COUPON_ID, COUPON_CODE, COUPON_DESC,
                        COUPON_EXPIRY, COUPON_STORE_ID, COUPON_TYPE, COUPON_USED, COUPON_VALUE},
                COUPON_STORE_ID + "=?", new String[]{Integer.toString(store_id)}, null, null, null);

        if(c.moveToFirst()){
            do{
                Coupon coupon = new Coupon();
                coupon.setId(Integer.parseInt(c.getString(0)));
                coupon.setCode(c.getString(1));
                coupon.setDesc(c.getString(2));
                coupon.setExpiry(c.getString(3));
                coupon.setStoreId(Integer.parseInt(c.getString(4)));
                coupon.setType(Integer.parseInt(c.getString(5)));
                coupon.setUsed(Integer.parseInt(c.getString(6)));
                coupon.setValue(Float.parseFloat(c.getString(7)));
                couponList.add(coupon);
            }while(c.moveToNext());
        }
        return couponList;
    }

    public int updateCoupon(Coupon coupon){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUPON_CODE,coupon.getCode());
        values.put(COUPON_DESC,coupon.getDesc());
        values.put(COUPON_EXPIRY,coupon.getExpiry());
        values.put(COUPON_STORE_ID,coupon.getStoreId());
        values.put(COUPON_TYPE,coupon.getType());
        values.put(COUPON_USED,coupon.getUsed());
        values.put(COUPON_VALUE, coupon.getValue());

        return db.update(TABLE_COUPON,values,COUPON_ID + "=?", new String[]{String.valueOf(coupon.getId())});
    }

    public void deleteCoupon(Coupon coupon){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_COUPON, COUPON_ID + "=?", new String[]{String.valueOf(coupon.getId())});
    }

    public int getCouponCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_COUPON;
        Cursor c = db.rawQuery(query,null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public int getStoreCouponCount(int store_id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_COUPON, new String[]{COUPON_ID, COUPON_CODE, COUPON_DESC,
                        COUPON_EXPIRY, COUPON_STORE_ID, COUPON_TYPE, COUPON_USED, COUPON_VALUE},
                COUPON_STORE_ID + "=?", new String[]{Integer.toString(store_id)}, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    /* Methods Related to Store Class*/

    public void addStore(Store store){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STORE_NAME,store.getName());
        values.put(STORE_TYPE, store.getType());
        values.put(STORE_URL, store.getUrl());
        db.insert(TABLE_STORE, null, values);
    }

    public List<Store> getAllStores(){
        List<Store> storeList = new ArrayList<Store>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STORE, null);

        if(c.moveToFirst()){
            do{
                Store store = new Store();
                store.setId(Integer.parseInt(c.getString(0)));
                store.setName(c.getString(1));
                store.setType(Integer.parseInt(c.getString(2)));
                store.setUrl(c.getString(3));
                storeList.add(store);
            }while(c.moveToNext());
        }
        return storeList;
    }

    public Store getStore(int store_id){
        Store store = new Store();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_STORE, new String[]{STORE_ID, STORE_NAME, STORE_TYPE,
                        STORE_URL},
                STORE_ID + "=?", new String[]{Integer.toString(store_id)}, null, null, null);
        if(c != null){
            c.moveToFirst();
            store.setId(Integer.parseInt(c.getString(0)));
            store.setName(c.getString(1));
            store.setType(Integer.parseInt(c.getString(2)));
            store.setUrl(c.getString(3));
        }
        return store;
    }

    public String getStoreName(int store_id){
        String name = "Unavailable";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_STORE, new String[]{STORE_NAME},
                STORE_ID + "=?", new String[]{Integer.toString(store_id)}, null, null,null);
        if(c!=null){
            c.moveToFirst();
            name = c.getString(0);
        }
        return name;
    }

    public List<String> getAllStoreName(){
        List<String> storeList = new ArrayList<String>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_STORE, null);

        if(c.moveToFirst()){
            do{
                storeList.add(c.getString(1));
            }while(c.moveToNext());
        }
        return storeList;
    }

    public int getStoreId(String store_name){
        int id = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_STORE, new String[]{STORE_ID},
                STORE_NAME + "=?", new String[]{store_name}, null, null, null);
        if(c!=null){
            c.moveToFirst();
            id = Integer.parseInt(c.getString(0));
        }
        return id;
    }

    public void deleteStore(Store store){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STORE, STORE_ID + "=?", new String[]{String.valueOf(store.getId())});
        db.delete(TABLE_COUPON, COUPON_STORE_ID + "=?", new String[]{String.valueOf(store.getId())});
    }

    public int getStoreCount(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_STORE;
        int count=0;
        Cursor c = db.rawQuery(query,null);
        if(c!=null)
            count = c.getCount();
        c.close();
        return count;
    }

    public int updateStore(Store store){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(STORE_NAME,store.getName());
        values.put(STORE_TYPE, store.getType());
        values.put(STORE_URL, store.getUrl());

        return db.update(TABLE_STORE,values,STORE_ID + "=?", new String[]{String.valueOf(store.getId())});
    }


}
