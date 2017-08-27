package pl.pollub.android.shoppinglist.db;

/**
 * Created by pawel on 28.07.2017.
 */

public class FriendsListDB {
    int _id;
    int _idUser;
    FriendsListDB(){

    }
    FriendsListDB(int id, int idUser){
        this._id=id;
        this._idUser=idUser;
    }
    public void setId(int id){
        this._id=id;
    }

    public void setIdUser(int _idUser) {
        this._idUser = _idUser;
    }

    public int getId() {
        return this._id;
    }

    public int getIdUser() {
        return this._idUser;
    }
}
