package pl.pollub.android.shoppinglist.db;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingGroup {
    private int _id;
    private String _name;
    private String _description;
    private LocalDateTime _createDate;
    private int _idIcon;
}
