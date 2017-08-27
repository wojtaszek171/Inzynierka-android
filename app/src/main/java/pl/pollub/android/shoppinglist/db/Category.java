package pl.pollub.android.shoppinglist.db;

/**
 * Created by pawel on 27.07.2017.
 */

public class Category {
    int _idCategory;
    int _idParent;
    String _name;
    String _description;

    public Category(){

    }
    public Category(int id, String name, String description){
        this._idCategory = id;
        this._name = name;
        this._description = _description;
    }
    public Category(String name, String description){
        this._name = name;
        this._description = description;
    }

    public int getID(){
        return this._idCategory;
    }

    public void setID(int id){
        this._idCategory = id;
    }

    public String getName(){
        return this._name;
    }

    public void setName(String name){
        this._name = name;
    }

    public String getDescription(){
        return this._description;
    }

    public void setDescription(String phone_number){
        this._description = phone_number;
    }
}
