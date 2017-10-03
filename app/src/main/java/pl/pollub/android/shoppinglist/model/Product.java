package pl.pollub.android.shoppinglist.model;

import lombok.*;

/**
 * @author Pawel on 27.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private int _idProduct;
    private String _name;
    private String _description;
    private int _idCategory;
    private Measure _measure;
}
