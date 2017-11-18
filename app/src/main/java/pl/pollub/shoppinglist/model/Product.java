package pl.pollub.shoppinglist.model;

import pl.pollub.shoppinglist.model.complextype.*;

/**
 * @author Adrian
 * @since 2017-11-12
 */
public interface Product {
    String getName();
    Category getCategory();
    Measure getMeasure();
    Icon getIcon();
}
