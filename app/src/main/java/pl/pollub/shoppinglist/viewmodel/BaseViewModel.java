package pl.pollub.shoppinglist.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import lombok.*;

/**
 * @author Adrian
 * @since 2017-10-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseViewModel<T> extends BaseObservable {
    @Bindable
    private T model;
}
