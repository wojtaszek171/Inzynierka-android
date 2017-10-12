package pl.pollub.android.shoppinglist.util.recyclerview.binder;

public interface ItemBinder<T> {
    int getLayoutRes(T model);
    int getBindingVariable(T model);
}
