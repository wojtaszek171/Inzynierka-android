package pl.pollub.android.shoppinglist.model;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendsListUser {
    private int _id;
    private int _idUser;
    private int _idFriendsList;
    private LocalDateTime _addDate;
}
