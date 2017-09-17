package pl.pollub.android.shoppinglist.db;

import java.sql.Blob;

import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Icon {
    private int _id;
    private String _name;
    private Blob _icon;
}
