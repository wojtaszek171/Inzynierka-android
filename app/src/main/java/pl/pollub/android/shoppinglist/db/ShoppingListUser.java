package pl.pollub.android.shoppinglist.db;

import java.util.Date;

/**
 * @author Pawel on 28.07.2017.
 */

public class ShoppingListUser {
    int _id;
    int _idShoppingList;
    int _idUser;
    Date _syncTime;

    ShoppingListUser() {
    }

    ShoppingListUser(int idShoppingList, int idUser, Date syncTime) {
        this._idShoppingList = idShoppingList;
        this._idUser = idUser;
        this._syncTime = syncTime;
    }

    ShoppingListUser(int id, int idShoppingList, int idUser, Date syncTime) {
        this._id = id;
        this._idShoppingList = idShoppingList;
        this._idUser = idUser;
        this._syncTime = syncTime;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getId() {
        return _id;
    }

    public void setIdUser(int _idUser) {
        this._idUser = _idUser;
    }

    public int getIdUser() {
        return _idUser;
    }

    public void setIdShoppingList(int _idShoppingList) {
        this._idShoppingList = _idShoppingList;
    }

    public int getIdShoppingList() {
        return _idShoppingList;
    }

    public void setSyncTime(Date _syncTime) {
        this._syncTime = _syncTime;
    }

    public Date getSyncTime() {
        return _syncTime;
    }
}
