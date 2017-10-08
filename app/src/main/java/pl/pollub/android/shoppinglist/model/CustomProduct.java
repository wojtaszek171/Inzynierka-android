package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import lombok.*;
import pl.pollub.android.shoppinglist.util.Measure;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * @author Pawel
 * @since 2017-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity(foreignKeys = {
        @ForeignKey(
                entity = Category.class,
                parentColumns = "id",
                childColumns = "category_id",
                onDelete = CASCADE,
                onUpdate = CASCADE
        )
})
public class CustomProduct extends Product {
    private int iconId;
    private Measure measure;

    public CustomProduct() {
        setPredefined(false);
    }
}
