package pl.pollub.android.shoppinglist.db;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 27.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NonStandardProduct {
    private int _idNonStandardProduct;
    private String _name;
    private String _description;
    private Measure _measure;
    private LocalDateTime _addDate;
    private LocalDateTime _modifyDate;
    private int _idUsers;
    private int _idIcons;
}
