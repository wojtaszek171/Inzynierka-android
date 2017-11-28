package pl.pollub.shoppinglist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Adrian
 * @since 2017-11-16
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder> {
    private List<T> items;
    private Context context;
    private OnViewHolderClick<T> listener;

    public interface OnViewHolderClick<T> {
        void onClick(View view, int position, T item);
    }

    protected abstract View createView(Context context, ViewGroup viewGroup, int viewType);

    protected abstract void bindView(T item, int position, BaseRecyclerViewAdapter.ViewHolder viewHolder);

    public BaseRecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public BaseRecyclerViewAdapter(Context context, OnViewHolderClick<T> listener) {
        super();
        this.context = context;
        this.listener = listener;
        items = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(createView(context, viewGroup, viewType), listener);
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        bindView(getItem(position), position, holder);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public T getItem(int index) {
        return ((items != null && index < items.size()) ? items.get(index) : null);
    }

    public Context getContext() {
        return context;
    }

    public void setList(List<T> newItems) {
        items = new ArrayList<>(newItems);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return Collections.unmodifiableList(items);
    }

    public void setOnClickListener(OnViewHolderClick<T> listener) {
        this.listener = listener;
    }

    public void addAllItems(Collection<T> newItems) {
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(T item) {
        items.remove(item);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Map<Integer, View> views;

        public ViewHolder(View view, OnViewHolderClick listener) {
            super(view);
            views = new HashMap<>();
            views.put(0, view);

            if (listener != null)
                view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (listener != null)
                listener.onClick(view, getAdapterPosition(), getItem(getAdapterPosition()));
        }

        public void initViewList(int[] idList) {
            for (int id : idList)
                initViewById(id);
        }

        public void initViewById(int id) {
            View view = (getView() != null ? getView().findViewById(id) : null);

            if (view != null)
                views.put(id, view);
        }

        public View getView() {
            return getView(0);
        }

        public View getView(int id) {
            if (views.containsKey(id))
                return views.get(id);
            else
                initViewById(id);

            return views.get(id);
        }
    }
}