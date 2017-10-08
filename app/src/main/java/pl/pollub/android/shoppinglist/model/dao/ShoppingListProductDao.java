package pl.pollub.android.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.android.shoppinglist.model.ShoppingListProduct;

/**
 * DAO of intermediate table, see {@link ShoppingListProduct}
 *
 * @author Adrian
 * @since 2017-10-08
 */
@Dao
public interface ShoppingListProductDao extends BaseDao<ShoppingListProduct> {
    @Override
    @Query("SELECT * FROM shopping_list_product")
    List<ShoppingListProduct> findAll();

    @Override
    @Query("SELECT * FROM shopping_list_product WHERE id = :id")
    ShoppingListProduct findById(int id);
}
