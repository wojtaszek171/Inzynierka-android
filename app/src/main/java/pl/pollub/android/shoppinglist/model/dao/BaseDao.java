package pl.pollub.android.shoppinglist.model.dao;

import android.arch.persistence.room.*;

import java.util.List;

import pl.pollub.android.shoppinglist.model.BaseEntity;

/**
 * @author Adrian
 * @since 2017-10-03
 */
@Dao
public interface BaseDao<T extends BaseEntity> {
    List<T> findAll();
    T findById(int id);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... entity);

    @Update
    void update(T entity);

    @Delete
    void delete(T entity);
}
