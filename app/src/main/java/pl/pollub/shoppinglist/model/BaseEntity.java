package pl.pollub.shoppinglist.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import org.threeten.bp.LocalDateTime;
import lombok.*;

import java.io.Serializable;

/**
 * @author Adrian
 * @since 2017-10-03
 */
@EqualsAndHashCode
@ToString
public class BaseEntity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    protected BaseEntity() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
