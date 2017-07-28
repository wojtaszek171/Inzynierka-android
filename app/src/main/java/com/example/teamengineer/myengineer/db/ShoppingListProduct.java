package com.example.teamengineer.myengineer.db;

import java.util.Date;

/**
 * Created by pawel on 28.07.2017.
 */

public class ShoppingListProduct {
    int _id;
    int _idShoppingList;
    int _idProduct;
    Boolean _productType;
    Float _price;
    String _description;
    Boolean _status;
    Date _addDate;
    Date _modifyDate;
    int _quantity;

    ShoppingListProduct(){

    }

    ShoppingListProduct(int idShoppingList, int idProduct, Boolean productType, Float price, String description, Boolean status, Date addDate, Date modifyDate, int quantity){
        this._idShoppingList=idShoppingList;
        this._idProduct=idProduct;
        this._productType=productType;
        this._price=price;
        this._description=description;
        this._status=status;
        this._addDate=addDate;
        this._modifyDate=modifyDate;
        this._quantity=quantity;
    }

    ShoppingListProduct(int id, int idShoppingList, int idProduct, Boolean productType, Float price, String description, Boolean status, Date addDate, Date modifyDate, int quantity){
        this._id=id;
        this._idShoppingList=idShoppingList;
        this._idProduct=idProduct;
        this._productType=productType;
        this._price=price;
        this._description=description;
        this._status=status;
        this._addDate=addDate;
        this._modifyDate=modifyDate;
        this._quantity=quantity;
    }

    public void setId(int id){
        this._id=id;
    }

    public int getId(){
        return this._id;
    }

    public void setIdShoppingList(int id){
        this._idShoppingList=id;
    }

    public int getIdShoppingList(){
        return this._idShoppingList;
    }

    public void setIdProduct(int id){
        this._idProduct=id;
    }

    public int getIdProduct(){
        return this._idProduct;
    }

    public void setProductType(Boolean type){
        this._productType=type;
    }

    public Boolean getProductType(){
        return this._productType;
    }

    public void setPrice(float price){
        this._price=price;
    }

    public float getPrice(){
        return this._price;
    }

    public void setDescription(String description){
        this._description=description;
    }

    public String getDescription(){
        return this._description;
    }

    public void setStatus(Boolean status){
        this._status=status;
    }

    public Boolean getStatus(){
        return this._status;
    }

    public void setAddDate(Date date){
        this._addDate=date;
    }

    public Date getAddDate(){
        return this._addDate;
    }

    public void setModifyDate(Date date){
        this._modifyDate=date;
    }

    public Date getModifyDate(){
        return this._modifyDate;
    }

    public void setQuantity(int quantity){
        this._quantity=quantity;
    }

    public int getQuantity(){
        return this._quantity;
    }
}
