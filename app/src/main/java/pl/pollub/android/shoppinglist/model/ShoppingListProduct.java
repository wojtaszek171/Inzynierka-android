package pl.pollub.android.shoppinglist.model;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListProduct {
    private int _id;
    private int _idShoppingList;
    private int _idProduct;
    private boolean _productType;
    private float _price;
    private String _description;
    private boolean _status;
    private LocalDateTime _addDate;
    private LocalDateTime _modifyDate;
    private int _quantity;
}
