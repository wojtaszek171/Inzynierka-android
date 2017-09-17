package pl.pollub.android.shoppinglist.db;

import org.threeten.bp.LocalDateTime;
import lombok.*;
import pl.pollub.android.shoppinglist.FriendsList;

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
