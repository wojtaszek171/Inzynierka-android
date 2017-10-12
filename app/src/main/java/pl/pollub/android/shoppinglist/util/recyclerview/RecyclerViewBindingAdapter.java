package pl.pollub.android.shoppinglist.util.recyclerview;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import pl.pollub.android.shoppinglist.BR;
import pl.pollub.android.shoppinglist.R;
import pl.pollub.android.shoppinglist.model.Buddy;
import pl.pollub.android.shoppinglist.util.recyclerview.BindingRecyclerViewAdapter;
import pl.pollub.android.shoppinglist.util.recyclerview.binder.ItemBinder;
import pl.pollub.android.shoppinglist.util.recyclerview.ClickHandler;
import pl.pollub.android.shoppinglist.util.recyclerview.LongClickHandler;
import pl.pollub.android.shoppinglist.util.recyclerview.binder.ItemBinderBase;

import java.util.Collection;

/**
 * @author Adrian
 * @since 2017-10-11
 */
@SuppressWarnings("unchecked")
public class RecyclerViewBindingAdapter {
    private static final int KEY_ITEMS = -123;
    private static final int KEY_CLICK_HANDLER = -124;
    private static final int KEY_LONG_CLICK_HANDLER = -125;

    @BindingAdapter("items")
    public static <T> void setItems(RecyclerView recyclerView, Collection<T> items) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setItems(items);
        } else {
            recyclerView.setTag(KEY_ITEMS, items);
        }
    }

    @BindingAdapter("clickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, ClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_CLICK_HANDLER, handler);
        }
    }

    @BindingAdapter("longClickHandler")
    public static <T> void setHandler(RecyclerView recyclerView, LongClickHandler<T> handler) {
        BindingRecyclerViewAdapter<T> adapter = (BindingRecyclerViewAdapter<T>) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.setLongClickHandler(handler);
        } else {
            recyclerView.setTag(KEY_LONG_CLICK_HANDLER, handler);
        }
    }

    @BindingAdapter("itemViewBinder")
    public static <T> void setItemViewBinder(RecyclerView recyclerView, ItemBinder<T> itemViewMapper) {
        Collection<T> items = (Collection<T>) recyclerView.getTag(KEY_ITEMS);
        ClickHandler<T> clickHandler = (ClickHandler<T>) recyclerView.getTag(KEY_CLICK_HANDLER);

        BindingRecyclerViewAdapter<T> adapter = new BindingRecyclerViewAdapter<>(itemViewMapper, items);
        if (clickHandler != null) {
            adapter.setClickHandler(clickHandler);
        }
        recyclerView.setAdapter(adapter);
    }
}