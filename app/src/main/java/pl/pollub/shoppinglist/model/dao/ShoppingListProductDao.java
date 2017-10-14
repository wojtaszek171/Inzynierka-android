package pl.pollub.shoppinglist.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

import pl.pollub.shoppinglist.model.ShoppingListProduct;

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

    @Override
    @Query("SELECT COUNT(*) FROM shopping_list_product")
    int count();
}
