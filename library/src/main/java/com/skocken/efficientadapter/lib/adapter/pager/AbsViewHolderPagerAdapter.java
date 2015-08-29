package com.skocken.efficientadapter.lib.adapter.pager;

import com.skocken.efficientadapter.lib.util.AdapterUtil;
import com.skocken.efficientadapter.lib.viewholder.AbsViewHolder;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Common adapter to use AbsViewHolder into a ViewPager
 *
 * @param <T> the king of object into this adapter
 */
public abstract class AbsViewHolderPagerAdapter<T> extends PagerAdapter {

    private SparseArray<List<AbsViewHolder>> mRecycleViewHolders = new SparseArray<>();

    /**
     * Contains the list of objects that represent the data of this AbsViewHolderAdapter.
     * The content of this list is referred to as "the array" in the documentation.
     */
    private List<T> mObjects;

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private final Object mLock = new Object();

    /**
     * Describe if we need to notify the adapter on add, addAll, remove or not.
     */
    private boolean mNotifyOnChange = true;

    /**
     * Constructor
     *
     * @param objects The objects to represent in the RecyclerView.
     */
    public AbsViewHolderPagerAdapter(T... objects) {
        this(new ArrayList<>(Arrays.asList(objects)));
    }

    /**
     * Constructor
     *
     * @param objects The objects to represent in the ListView.
     */
    public AbsViewHolderPagerAdapter(List<T> objects) {
        if (objects == null) {
            objects = new ArrayList<>();
        }
        mObjects = objects;
    }

    /**
     * Get the view holder to instantiate for the object for this position
     *
     * @param viewType viewType return by getItemViewType()
     * @return the class of the view holder to instantiate
     */
    protected abstract Class<? extends AbsViewHolder> getViewHolderClass(int viewType);

    /**
     * The layout to inflate for the object for this viewType
     *
     * @param viewType viewType return by getItemViewType()
     * @return the resource ID of a layout to inflate
     */
    protected abstract int getLayoutResId(int viewType);

    /**
     * Set if we need to notify the adapter on add, addAll or remove object or not.
     */
    public void setNotifyOnChange(boolean enable) {
        mNotifyOnChange = enable;
    }

    /**
     * Determine if the object provide is in this adapter
     *
     * @return true if the object is in this adapter
     */
    public boolean hasItem(T object) {
        synchronized (mLock) {
            return mObjects.contains(object);
        }
    }

    /**
     * Searches this {@code List} for the specified object and returns the index of the
     * first occurrence.
     *
     * @param object the object to search for.
     * @return the index of the first occurrence of the object or -1 if the
     * object was not found.
     */
    public int indexOf(T object) {
        synchronized (mLock) {
            return mObjects.indexOf(object);
        }
    }

    /**
     * Adds the specified Collection at the end of the array.
     *
     * @param collection The Collection to add at the end of the array.
     */
    public void addAll(Collection<? extends T> collection) {
        synchronized (mLock) {
            mObjects.addAll(collection);
        }
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        addAll(Arrays.asList(items));
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(T object) {
        synchronized (mLock) {
            mObjects.add(object);
        }
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * Adds the specified object at the specified position of the array.
     *
     * @param position The position of object to add
     * @param object   The object to add at the end of the array.
     */
    public void add(int position, T object) {
        synchronized (mLock) {
            mObjects.add(position, object);
        }
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }


    /**
     * Remove the object at the specified position of the array.
     *
     * @param position The position of object to add
     */
    public void remove(int position) {
        synchronized (mLock) {
            mObjects.remove(position);
        }
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }


    /**
     * Remove the specified object of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void remove(T object) {
        int positionOfRemove;
        synchronized (mLock) {
            positionOfRemove = mObjects.indexOf(object);
        }
        if (positionOfRemove >= 0) {
            remove(positionOfRemove);
        }
    }

    /**
     * Move object
     */
    public void moved(int from, int to) {
        synchronized (mLock) {
            mObjects.add(to, mObjects.remove(from));
        }
        notifyDataSetChanged();
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }
        if (mNotifyOnChange) {
            notifyDataSetChanged();
        }
    }

    /**
     * Returns whether this {@code List} contains no elements.
     *
     * @return {@code true} if this {@code List} has no elements, {@code false}
     * otherwise.
     * @see #size
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of elements in this {@code List}.
     *
     * @return the number of elements in this {@code List}.
     */
    public int size() {
        synchronized (mLock) {
            return mObjects.size();
        }
    }

    @Override
    public int getCount() {
        return size();
    }

    public int getItemCount() {
        return size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((AbsViewHolder) object).getView();
    }

    /**
     * @return a copy of the {@code List} of elements.
     */
    public List<T> getObjects() {
        synchronized (mLock) {
            return new ArrayList<T>(mObjects);
        }
    }

    /**
     * Get the object at the position from the array.
     *
     * @param position position of the object in this adapter
     * @return the object found at this position
     */
    public T get(int position) {
        synchronized (mLock) {
            return mObjects.get(position);
        }
    }

    public AbsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = generateView(parent, viewType);
        return generateViewHolder(v, viewType);
    }

    public void onBindViewHolder(AbsViewHolder viewHolder, int position) {
        T object;
        synchronized (mLock) {
            object = mObjects.get(position);
        }
        viewHolder.onBindView(object);
    }

    public void onViewRecycled(AbsViewHolder holder) {
        holder.onViewRecycled();
    }

    public void onViewAttachedToWindow(AbsViewHolder holder) {
        holder.onViewAttachedToWindow();
    }

    public void onViewDetachedFromWindow(AbsViewHolder holder) {
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object object) {
        AbsViewHolder viewHolder = (AbsViewHolder) object;
        collection.removeView(viewHolder.getView());
        onViewDetachedFromWindow(viewHolder);

        int viewType = getItemViewType(position);
        List<AbsViewHolder> viewHolders = mRecycleViewHolders.get(viewType);
        if (viewHolders == null) {
            viewHolders = new ArrayList<>();
            mRecycleViewHolders.put(viewType, viewHolders);
        }
        viewHolders.add(viewHolder);
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        int viewType = getItemViewType(position);
        List<AbsViewHolder> viewHolders = mRecycleViewHolders.get(viewType);
        AbsViewHolder viewHolder;
        if (viewHolders == null || viewHolders.isEmpty()) {
            viewHolder = onCreateViewHolder(collection, viewType);
        } else {
            viewHolder = viewHolders.remove(0);
            onViewRecycled(viewHolder);
        }
        onBindViewHolder(viewHolder, position);
        collection.addView(viewHolder.getView(), 0);
        onViewAttachedToWindow(viewHolder);
        return viewHolder;
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     *
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * Generate a view to be used into the view holder
     */
    protected View generateView(ViewGroup parent, int viewType) {
        return AdapterUtil.generateView(parent, getLayoutResId(viewType));
    }

    /**
     * Generate a view holder for this view for this viewType
     */
    private AbsViewHolder generateViewHolder(View v, int viewType) {
        Class<? extends AbsViewHolder> viewHolderClass = getViewHolderClass(viewType);
        if (viewHolderClass == null) {
            throw new NullPointerException(
                    "You must supply a view holder class for the element for view type "
                            + viewType);
        }
        return AdapterUtil.generateViewHolder(v, viewHolderClass);
    }
}
