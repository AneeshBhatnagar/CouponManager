package com.aneeshbhatnagar.couponmanager.model;

public class Store {
    int id,type;
    String name,url;

    public Store(){
        this.id = -1;
        this.name = null;
        this.type = -1;
        this.url = null;
    }

    public Store(int id, String name, int type){
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = null;
    }

    public Store(String name, int type){
        this.name = name;
        this.type = type;
        this.url = null;
    }

    public Store(int id, String name, int type, String url){
        this.id = id;
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public Store(String name, int type, String url){
        this.name = name;
        this.type = type;
        this.url = url;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public int getType(){
        return this.type;
    }

    public String getUrl(){
        return url;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setType(int type){
        this.type = type;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setUrl(String url){
        this.url = url;
    }
}

