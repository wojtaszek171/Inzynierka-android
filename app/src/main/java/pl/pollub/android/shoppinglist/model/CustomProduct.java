package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import lombok.*;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Pawel
 * @since 2017-07-27
 */
/*@EqualsAndHashCode(callSuper = true)
@ToString
@Entity(tableName = "product", inheritSuperIndices = true, foreignKeys = {
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})*/
public class CustomProduct extends Product {

    public CustomProduct() {
        setPredefined(false);
    }
}
