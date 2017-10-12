package pl.pollub.android.shoppinglist.viewmodel;

import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import lombok.*;

import pl.pollub.android.shoppinglist.BR;
import pl.pollub.android.shoppinglist.R;
import pl.pollub.android.shoppinglist.model.Buddy;
import pl.pollub.android.shoppinglist.util.recyclerview.binder.ItemBinder;
import pl.pollub.android.shoppinglist.util.recyclerview.binder.ItemBinderBase;

/**
 * @author Adrian
 * @since 2017-10-11
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BuddiesViewModel extends BaseViewModel<ObservableList<Buddy>> {

    public BuddiesViewModel(ObservableList<Buddy> model) {
        super(model);
    }

    public void addBuddy(String name, String nickname) {
        Buddy buddy = new Buddy();
        buddy.setName(name);
        buddy.setNickname(nickname);
        getModel().add(buddy);
    }

    public ItemBinder<Buddy> itemViewBinder() {
        return new ItemBinderBase<>(BR.buddy, R.layout.item_buddy);
    }
}
