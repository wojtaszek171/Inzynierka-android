package pl.pollub.shoppinglist.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.parse.ParseACL;
import com.parse.ParseRole;

/**
 * @author Adrian
 * @since 2017-10-26
 */
@EqualsAndHashCode(callSuper = true)
@ToString
public class Role extends ParseRole {
    public static final String CLASS_NAME = "_Role";

    public Role(String name) {
        super(name);
    }

    public Role(String name, ParseACL acl) {
        super(name, acl);
    }
}
