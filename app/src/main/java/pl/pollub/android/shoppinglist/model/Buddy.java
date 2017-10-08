package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Buddy extends BaseEntity {
    private String name;
    private String nickname;
    private String email;
    private LocalDateTime lastActiveAt;
}
