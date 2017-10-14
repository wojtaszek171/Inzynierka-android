package pl.pollub.shoppinglist.binder;

import pl.pollub.shoppinglist.model.Buddy;
import pl.pollub.shoppinglist.util.recyclerview.binder.ConditionalDataBinder;
import pl.pollub.shoppinglist.viewmodel.BuddiesViewModel;

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
