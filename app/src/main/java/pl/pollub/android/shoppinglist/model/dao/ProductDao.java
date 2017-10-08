package pl.pollub.android.shoppinglist.model.dao;

import pl.pollub.android.shoppinglist.model.Product;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import java.util.List;

/**
 * @author Adrian
 * @since 2017-10-03
 */
@Dao
public interface ProductDao extends BaseDao<Product> {
    @Override
    @Query("SELECT * FROM user")
    List<Product> findAll();

    @Override
    @Query("SELECT * FROM product WHERE id = :id")
    Product findById(int id);
}
