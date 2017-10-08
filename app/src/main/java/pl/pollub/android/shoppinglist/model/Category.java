package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.ColumnInfo;
import lombok.*;

/**
 * @author Pawel
 * @since 2017-07-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Category extends NamedEntity {
    @ColumnInfo(index = true)
    private int parentId;
}
