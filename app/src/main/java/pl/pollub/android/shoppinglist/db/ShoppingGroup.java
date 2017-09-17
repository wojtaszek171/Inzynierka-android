package pl.pollub.android.shoppinglist.db;

import java.util.Date;

/**
 * @author Pawel on 28.07.2017.
 */

public class ShoppingGroup {
    int _id;
    String _name;
    String _description;
    Date _createDate;
    int _idIcon;

    ShoppingGroup() {
    }

    ShoppingGroup(String name, String description, Date createDate, int idIcon) {
        this._name = name;
        this._description = description;
        this._createDate = createDate;
        this._idIcon = idIcon;
    }

    ShoppingGroup(int id, String name, String description, Date createDate, int idIcon) {
        this._id = id;
        this._name = name;
        this._description = description;
        this._createDate = createDate;
        this._idIcon = idIcon;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String _name) {
        this._name = _name;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public Date getCreateDate() {
        return _createDate;
    }

    public void setCreateDate(Date _createDate) {
        this._createDate = _createDate;
    }

    public int getIdIcon() {
        return _idIcon;
    }

    public void setIdIcon(int _idIcon) {
        this._idIcon = _idIcon;
    }
}
