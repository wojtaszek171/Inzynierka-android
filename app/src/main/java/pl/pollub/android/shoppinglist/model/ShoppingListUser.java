package pl.pollub.android.shoppinglist.model;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingListUser {
    private int _id;
    private int _idShoppingList;
    private int _idUser;
    private LocalDateTime _syncTime;
}
