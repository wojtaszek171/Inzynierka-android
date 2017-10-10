package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-04
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class NamedEntity extends BaseEntity {
    private String name;
    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
