package com.example.teamengineer.myengineer.db;

import java.util.Date;

/**
 * Created by pawel on 28.07.2017.
 */

public class ShoppingList {
    int _id;
    int _idShoppingGroup;
    int _idIcon;
    Date _createDate;
    Status _listStatus;
    Date _lastSynchronization;
    Date _modifyDate;

    ShoppingList(){

    }
    ShoppingList(int idShoppingGroup, int idIcon, Date createDate, Status listStatus, Date lastSynchronization, Date modifyDate ){
        this._idShoppingGroup=idShoppingGroup;
        this._idIcon=idIcon;
        this._createDate=createDate;
        this._listStatus=listStatus;
        this._lastSynchronization=lastSynchronization;
        this._modifyDate=modifyDate;
    }
    ShoppingList(int id, int idShoppingGroup, int idIcon, Date createDate, Status listStatus, Date lastSynchronization, Date modifyDate ){
        this._id=id;
        this._idShoppingGroup=idShoppingGroup;
        this._idIcon=idIcon;
        this._createDate=createDate;
        this._listStatus=listStatus;
        this._lastSynchronization=lastSynchronization;
        this._modifyDate=modifyDate;
    }
    public void setId(int id){
        this._id=id;
    }
    public int getId(){
        return this._id;
    }
    public void setIdIcon(int id){
        this._idIcon=id;
    }
    public int getIdIcon(){
        return this._idIcon;
    }
    public void setIdShoppingGroup(int id){
        this._idShoppingGroup=id;
    }
    public int getIdShoppingGroup(){
        return this._idShoppingGroup;
    }
    public void setCreateDate(Date date){
        this._createDate=date;
    }

    public Date getCreateDate(){
        return this._createDate;
    }

    public void setModifyDate(Date date){
        this._modifyDate=date;
    }

    public Date getModifyDate(){
        return this._modifyDate;
    }
    public void setListStatus(Status status){
        this._listStatus=status;
    }
    public Status getListStatus(){
        return this._listStatus;
    }
    public void setLastSynchronization(Date date){
        this._lastSynchronization=date;
    }
    public Date getLastSynchronization(){
        return this._lastSynchronization;
    }
}
