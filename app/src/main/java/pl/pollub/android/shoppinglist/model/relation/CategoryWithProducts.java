package pl.pollub.android.shoppinglist.model.relation;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import java.util.List;

import pl.pollub.android.shoppinglist.model.Category;
import pl.pollub.android.shoppinglist.model.Product;

/**
 * @author by Adrian
 * @since 2017-10-04
 */
public class CategoryWithProducts {
    @Embedded
    private Category category;
    @Relation(parentColumn = "id", entityColumn = "category_id", entity = Product.class)
    List<Product> products;
}
