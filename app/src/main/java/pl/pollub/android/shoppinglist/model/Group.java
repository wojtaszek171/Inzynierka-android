package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;

import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Group extends NamedEntity {
    private String icon;
}
