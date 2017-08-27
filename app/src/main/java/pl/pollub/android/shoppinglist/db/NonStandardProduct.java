package pl.pollub.android.shoppinglist.db;

import java.util.Date;

/**
 * Created by pawel on 27.07.2017.
 */

public class NonStandardProduct {

    int _idNonStandardProduct;
    String _name;
    String _description;
    Measure _measure;
    Date _addDate;
    Date _modifyDate;
    int _idUsers;
    int _idIcons;

    public NonStandardProduct(){

    }

    public NonStandardProduct( String name, String description, Measure measure,Date addDate, Date modifyDate, int idUsers, int idIcons){
        this._name=name;
        this._description=description;
        this._measure=measure;
        this._addDate=addDate;
        this._modifyDate=modifyDate;
        this._idUsers=idUsers;
        this._idIcons=idIcons;
    }

    public NonStandardProduct( int id, String name, String description, Measure measure,Date addDate, Date modifyDate, int idUsers, int idIcons){
        this._idNonStandardProduct = id;
        this._name=name;
        this._description=description;
        this._measure=measure;
        this._addDate=addDate;
        this._modifyDate=modifyDate;
        this._idUsers=idUsers;
        this._idIcons=idIcons;
    }

    public void setId(int id){
        this._idNonStandardProduct = id;
    }

    public int getId(){
        return this._idNonStandardProduct;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getName(){
        return this._name;
    }

    public void setDescription(String description){
        this._description=description;
    }

    public String getDescription(){
        return this._description;
    }

    public void setMeasure(Measure measure){
        this._measure=measure;
    }

    public Measure getMeasure(){
        return this._measure;
    }
}
