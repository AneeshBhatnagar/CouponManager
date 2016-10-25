package com.aneeshbhatnagar.couponmanager.model;

public class Coupon {
    int id, storeId, type, used;
    float value;
    String code, desc, expiry;

    public Coupon() {
        this.id = -1;
        this.storeId = -1;
        this.type = -1;
        this.value = -1;
        this.code = null;
        this.desc = null;
        this.expiry = null;
        this.used = -1;
    }

    public Coupon(int id, int storeId, int type, String code, String desc, String expiry) {
        this.id = id;
        this.storeId = storeId;
        this.type = type;
        this.code = code;
        this.desc = desc;
        this.expiry = expiry;
        this.value = -1;
        this.used = -1;
    }

    public Coupon(int storeId, int type, String code, String desc, String expiry) {
        this.storeId = storeId;
        this.type = type;
        this.code = code;
        this.desc = desc;
        this.expiry = expiry;
        this.value = -1;
        this.used = -1;
    }

    public Coupon(int id, int storeId, int type, String code, String desc, String expiry, float value) {
        this.id = id;
        this.storeId = storeId;
        this.type = type;
        this.code = code;
        this.desc = desc;
        this.expiry = expiry;
        this.value = value;
        this.used = -1;
    }

    public Coupon(int storeId, int type, String code, String desc, String expiry, float value) {
        this.storeId = storeId;
        this.type = type;
        this.code = code;
        this.desc = desc;
        this.expiry = expiry;
        this.value = value;
        this.used = -1;
    }

    //Methods to Store Data into class object
    public void setId(int id) {
        this.id = id;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUsed(int used) {
        this.used = used;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    //Methods to retrieve class data objects

    public int getId(){
        return this.id;
    }

    public int getStoreId(){
        return this.storeId;
    }

    public int getType(){
        return this.type;
    }

    public int getUsed(){
        return this.used;
    }

    public float getValue(){
        return this.value;
    }

    public String getCode(){
        return this.code;
    }

    public String getDesc(){
        return this.desc;
    }

    public String getExpiry(){
        return this.expiry;
    }
}
