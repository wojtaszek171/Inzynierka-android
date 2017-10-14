package pl.pollub.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;

import lombok.*;
import pl.pollub.shoppinglist.util.Icon;

/**
 * @author Pawel
 * @since 2017-07-27
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "category")
public class Category extends NamedEntity {
    @ColumnInfo(index = true, name = "parent_id")
    private int parentId;
    private Icon icon;

    public int getParentId() {
        return parentId;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
