package pl.pollub.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.shoppinglist.model.ShoppingListGroup;

/**
 * DAO of intermediate table, see {@link ShoppingListGroup}
 *
 * @author Adrian
 * @since 2017-10-09
 */
@Dao
public interface ShoppingListGroupDao extends BaseDao<ShoppingListGroup> {
    @Override
    @Query("SELECT * FROM shopping_list_group")
    List<ShoppingListGroup> findAll();

    @Override
    @Query("SELECT * FROM shopping_list_group WHERE id = :id")
    ShoppingListGroup findById(int id);

    @Override
    @Query("SELECT COUNT(*) FROM shopping_list_group")
    int count();
}
