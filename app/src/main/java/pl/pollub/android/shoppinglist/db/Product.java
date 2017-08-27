package pl.pollub.android.shoppinglist.db;

/**
 * Created by pawel on 27.07.2017.
 */

public class Product {
    int _idProduct;
    String _name;
    String _description;
    int _idCategory;
    Measure _measure;

    public Product(){

    }
    public Product(int id, String name, String description, int idCategory, Measure measure){
        this._idProduct = id;
        this._name = name;
        this._description = description;
        this._idCategory = idCategory;
        this._measure = measure;
    }

    public Product(String name, String description, int idCategory, Measure measure){
        this._name = name;
        this._description = description;
        this._idCategory = idCategory;
        this._measure = measure;
    }

    public void setId(int id){
        this._idProduct = id;
    }

    public int getId(){
        return this._idProduct;
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

    public void setIdCategory(int id){
        this._idCategory=id;
    }

    public int getIdCategory(){
        return this._idCategory;
    }

    public void setMeasure(Measure measure){
        this._measure=measure;
    }

    public Measure getMeasure(){
        return this._measure;
    }
}
