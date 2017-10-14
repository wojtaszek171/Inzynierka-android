package pl.pollub.shoppinglist.viewmodel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.pollub.shoppinglist.model.Buddy;

/**
 * @author Adrian
 * @since 2017-10-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BuddyDetailsViewModel extends BaseViewModel<Buddy> {
}
