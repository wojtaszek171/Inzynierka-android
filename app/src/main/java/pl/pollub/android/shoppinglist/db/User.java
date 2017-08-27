package pl.pollub.android.shoppinglist.db;

import java.util.Date;

/**
 * Created by pawel on 28.07.2017.
 */

public class User {
    int _id;
    String _email;
    String _password;
    Date _registerDate;
    Date _lastLoginDate;
    int _idIcon;
    Boolean _stayLogged;

    User(){

    }
    User(String email, String password, Date registerDate, Date lastLogin, int idIcon, Boolean stayLogged){
        this._email=email;
        this._password=password;
        this._registerDate=registerDate;
        this._lastLoginDate=lastLogin;
        this._idIcon=idIcon;
        this._stayLogged=stayLogged;
    }
    User(int id, String email, String password, Date registerDate, Date lastLogin, int idIcon, Boolean stayLogged){
        this._id=id;
        this._email=email;
        this._password=password;
        this._registerDate=registerDate;
        this._lastLoginDate=lastLogin;
        this._idIcon=idIcon;
        this._stayLogged=stayLogged;
    }

    public void setId(int id){
        this._id=id;
    }
    public int getId(){
        return this._id;
    }
    public void setEmail(String email){
        this._email=email;
    }
    public String getEmail(){
        return this._email;
    }
    public void  setRegisterDate(Date date){
        this._registerDate=date;
    }
    public Date getRegisterDate(){
        return this._registerDate;
    }
    public void setLastLoginDate(Date date){
        this._lastLoginDate=date;
    }
    public Date getLastLoginDate(){
        return this._lastLoginDate;
    }
    public void setIdIcon(int id){
        this._idIcon=id;
    }
    public int getIdIcon(){
        return this._idIcon;
    }
    public void setStayLogged(Boolean value){
        this._stayLogged=value;
    }
    public Boolean getStayLogged(){
        return this._stayLogged;
    }
}
