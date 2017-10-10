package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;

import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "shopping_group")
public class Group extends NamedEntity {
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
