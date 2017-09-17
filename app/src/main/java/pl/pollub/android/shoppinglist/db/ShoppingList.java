package pl.pollub.android.shoppinglist.db;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingList {
    private int _id;
    private int _idShoppingGroup;
    private int _idIcon;
    private LocalDateTime _createDate;
    private Status _listStatus;
    private LocalDateTime _lastSynchronization;
    private LocalDateTime _modifyDate;
}
