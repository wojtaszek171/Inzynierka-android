package pl.pollub.shoppinglist.model.dao;

import android.arch.persistence.room.*;

import java.util.List;

import pl.pollub.shoppinglist.model.BaseEntity;

/**
 * @author Adrian
 * @since 2017-10-03
 */
@SuppressWarnings("unchecked")
@Dao
interface BaseDao<T extends BaseEntity> {
    List<T> findAll();
    T findById(int id);
    int count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(T... entity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(T... entity);

    @Delete
    void delete(T... entity);
}
