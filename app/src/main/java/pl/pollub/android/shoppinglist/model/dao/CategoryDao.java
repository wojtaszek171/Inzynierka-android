package pl.pollub.android.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Category;

/**
 * @author Adrian
 * @since 2017-10-07
 */
@Dao
public interface CategoryDao extends BaseDao<Category> {
    @Override
    @Query("SELECT * FROM category")
    List<Category> findAll();

    @Override
    @Query("SELECT * FROM category WHERE id = :id")
    Category findById(int id);

    @Override
    @Query("SELECT COUNT(*) FROM category")
    int count();
}
