package pl.pollub.android.shoppinglist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.threeten.bp.LocalDateTime;
import lombok.*;

import java.io.Serializable;

/**
 * @author Adrian
 * @since 2017-10-03
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BaseEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
