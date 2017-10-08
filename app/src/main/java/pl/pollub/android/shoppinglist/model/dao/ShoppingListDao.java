package pl.pollub.android.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.android.shoppinglist.model.ShoppingList;

/**
 * @author Adrian
 * @since 2017-10-08
 */
@Dao
public interface ShoppingListDao extends BaseDao<ShoppingList> {
    @Override
    @Query("SELECT * FROM shopping_list")
    List<ShoppingList> findAll();

    @Override
    @Query("SELECT * FROM shopping_list WHERE id = :id")
    ShoppingList findById(int id);
}
