package pl.pollub.android.shoppinglist.util.recyclerview.binder;

/**
 * @author Adrian
 * @since 2017-10-11
 */
public class ItemBinderBase<T> implements ItemBinder<T> {
    protected final int bindingVariable;
    protected final int layoutId;

    public ItemBinderBase(int bindingVariable, int layoutId) {
        this.bindingVariable = bindingVariable;
        this.layoutId = layoutId;
    }

    public int getLayoutRes(T model) {
        return layoutId;
    }

    public int getBindingVariable(T model) {
        return bindingVariable;
    }
}
