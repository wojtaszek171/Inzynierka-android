package pl.pollub.android.shoppinglist.db;

import lombok.*;

/**
 * @author Pawel on 27.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int _idCategory;
    private int _idParent;
    private String _name;
    private String _description;
}
