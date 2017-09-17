package pl.pollub.android.shoppinglist.db;

import pl.pollub.android.shoppinglist.FriendsList;

import java.util.Date;

/**
 * @author Pawel on 28.07.2017.
 */

public class FriendsListUser {
    int _id;
    int _idUser;
    int _idFriendsList;
    Date _addDate;

    FriendsListUser() {
    }

    FriendsListUser(int idUser, int idFriendsList, Date addDate) {
        this._idUser = idUser;
        this._idFriendsList = idFriendsList;
        this._addDate = addDate;
    }

    FriendsListUser(int id, int idUser, int idFriendsList, Date addDate) {
        this._id = id;
        this._idUser = idUser;
        this._idFriendsList = idFriendsList;
        this._addDate = addDate;
    }

    public void setIdUser(int _idUser) {
        this._idUser = _idUser;
    }

    public int getIdUser() {
        return _idUser;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public int getId() {
        return _id;
    }

    public int getIdFriendsList() {
        return _idFriendsList;
    }

    public void setIdFriendsList(int _idFriendsList) {
        this._idFriendsList = _idFriendsList;
    }

    public void setAddDate(Date _addDate) {
        this._addDate = _addDate;
    }

    public Date getAddDate() {
        return _addDate;
    }
}
