package pl.pollub.android.shoppinglist.db;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingGroupUser {
    private int _id;
    private int _idShoppingGroup;
    private int _idUser;
    private LocalDateTime _userAddDate;
    private String _pseudonym;
    private boolean _whetherAdminGroup;
}
