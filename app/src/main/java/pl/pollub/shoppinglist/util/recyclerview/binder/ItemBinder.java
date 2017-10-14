package pl.pollub.shoppinglist.util.recyclerview.binder;

/**
 * @author Adrian
 * @since 2017-10-11
 */
public interface ItemBinder<T> {
    int getLayoutRes(T model);
    int getBindingVariable(T model);
}
