package pl.pollub.android.shoppinglist.db;

import java.util.Date;

/**
 * Created by pawel on 28.07.2017.
 */

public class ShoppingGroupUser {
    int _id;
    int _idShoppingGroup;
    int _idUser;
    Date _userAddDate;
    String _pseudonym;
    Boolean _whetherAdminGroup;

    ShoppingGroupUser(){

    }
    ShoppingGroupUser(int idShoppingGroup, int idUser, Date userAddDate, String pseudonym, Boolean whetherAdminGroup){
        this._idShoppingGroup=idShoppingGroup;
        this._idUser=idUser;
        this._userAddDate=userAddDate;
        this._pseudonym=pseudonym;
        this._whetherAdminGroup=whetherAdminGroup;
    }
    ShoppingGroupUser(int id, int idShoppingGroup, int idUser, Date userAddDate, String pseudonym, Boolean whetherAdminGroup){
        this._id=id;
        this._idShoppingGroup=idShoppingGroup;
        this._idUser=idUser;
        this._userAddDate=userAddDate;
        this._pseudonym=pseudonym;
        this._whetherAdminGroup=whetherAdminGroup;
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

    public void setIdShoppingGroup(int _idShoppingGroup) {
        this._idShoppingGroup = _idShoppingGroup;
    }

    public int getIdShoppingGroup() {
        return _idShoppingGroup;
    }

    public void setUserAddDate(Date _userAddDate) {
        this._userAddDate = _userAddDate;
    }

    public Date getUserAddDate() {
        return _userAddDate;
    }

    public void setPseudonym(String _pseudonym) {
        this._pseudonym = _pseudonym;
    }

    public String getPseudonym() {
        return _pseudonym;
    }

    public void setWhetherAdminGroup(Boolean _whetherAdminGroup) {
        this._whetherAdminGroup = _whetherAdminGroup;
    }

    public Boolean getWhetherAdminGroup() {
        return _whetherAdminGroup;
    }
}
