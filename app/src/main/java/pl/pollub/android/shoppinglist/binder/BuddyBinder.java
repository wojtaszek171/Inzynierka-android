package pl.pollub.android.shoppinglist.binder;

import pl.pollub.android.shoppinglist.model.Buddy;
import pl.pollub.android.shoppinglist.util.recyclerview.binder.ConditionalDataBinder;
import pl.pollub.android.shoppinglist.viewmodel.BuddiesViewModel;

/**
 * @author Adrian
 * @since 2017-10-11
 */

public class BuddyBinder extends ConditionalDataBinder<Buddy> {

    public BuddyBinder(int bindingVariable, int layoutId) {
        super(bindingVariable, layoutId);
    }

    @Override
    public boolean canHandle(Buddy model) {
        return true;
    }
}
