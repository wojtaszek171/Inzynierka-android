package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class NamedEntity extends BaseEntity {
    private String name;
    private String description;
}
