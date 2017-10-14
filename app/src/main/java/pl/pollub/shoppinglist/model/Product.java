package pl.pollub.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import lombok.*;
import pl.pollub.shoppinglist.util.Icon;
import pl.pollub.shoppinglist.util.Measure;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Pawel
 * @since 2017-07-27
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(foreignKeys = {
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class Product extends NamedEntity {
    @ColumnInfo(index = true, name = "category_id")
    private int categoryId;
    private Measure measure;
    private boolean predefined = true;
    private Icon icon;

    public int getCategoryId() {
        return categoryId;
    }

    public Measure getMeasure() {
        return measure;
    }

    public boolean isPredefined() {
        return predefined;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public void setPredefined(boolean predefined) {
        this.predefined = predefined;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
