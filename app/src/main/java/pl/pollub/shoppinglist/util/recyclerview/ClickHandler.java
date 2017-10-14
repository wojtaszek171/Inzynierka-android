package pl.pollub.shoppinglist.util.recyclerview;

/**
 * @author Adrian
 * @since 2017-10-11
 */
public interface ClickHandler<T> {
    void onClick(T viewModel);
}