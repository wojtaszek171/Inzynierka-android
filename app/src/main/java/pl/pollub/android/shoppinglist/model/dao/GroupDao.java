package pl.pollub.android.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.android.shoppinglist.model.Group;

/**
 * @author Adrian
 * @since 2017-10-08
 */
@Dao
public interface GroupDao extends BaseDao<Group> {
    @Override
    @Query("SELECT * FROM shopping_group")
    List<Group> findAll();

    @Override
    @Query("SELECT * FROM shopping_group WHERE id = :id")
    Group findById(int id);

    @Override
    @Query("SELECT COUNT(*) FROM shopping_group")
    int count();
}
