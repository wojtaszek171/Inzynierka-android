package com.example.teamengineer.myengineer.db;

import java.sql.Blob;

/**
 * Created by pawel on 28.07.2017.
 */

public class Icon {
    int _id;
    String _name;
    Blob _icon;

    Icon(){

    }
    Icon(String name, Blob icon){
        this._name=name;
        this._icon=icon;
    }
    Icon(int id, String name, Blob icon){
        this._id=id;
        this._name=name;
        this._icon=icon;
    }
    public void setId(int id){
        this._id=id;
    }
    public int getId(){
        return this._id;
    }
    public void setName(String name){
        this._name=name;
    }
    public String getName(){
        return this._name;
    }
    public void setIcon(Blob icon){
        this._icon=icon;
    }
    public Blob getIcon(){
        return this._icon;
    }

}
