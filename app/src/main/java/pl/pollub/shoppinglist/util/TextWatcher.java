package pl.pollub.shoppinglist.util;

import android.text.Editable;

/**
 * Wrapper for original TextWatcher providing default empty impl
 *
 * @author Adrian
 * @since 2017-12-12
 */
@FunctionalInterface
public interface TextWatcher extends android.text.TextWatcher {
    @Override
    default void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    default void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    void afterTextChanged(Editable s);
}
