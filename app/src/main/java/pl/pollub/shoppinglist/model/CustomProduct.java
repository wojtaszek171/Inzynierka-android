package pl.pollub.shoppinglist.model;

import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-26
 */
/*@EqualsAndHashCode(callSuper = true)
@ToString*/
@Deprecated
public class CustomProduct extends Product {

    public CustomProduct() {
        setPredefined(false);
    }
}
