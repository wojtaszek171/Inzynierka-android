package pl.pollub.android.shoppinglist.model;

import org.threeten.bp.LocalDateTime;
import lombok.*;

/**
 * @author Pawel on 28.07.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int _id;
    private String _email;
    private String _password;
    private LocalDateTime _registerDate;
    private LocalDateTime _lastLoginDate;
    private int _idIcon;
    private boolean _stayLogged;
}
