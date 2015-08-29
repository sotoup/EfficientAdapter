package com.skocken.efficientadapter.lib.adapter.pager;

import java.util.List;

/**
 * Common adapter to use an heterogeneous list of ViewHolder into a ViewPager
 */
public abstract class HeterogeneousPagerAdapter extends AbsViewHolderPagerAdapter<Object> {

    /**
     * Constructor
     *
     * @param objects The objects to represent in the RecyclerView.
     */
    public HeterogeneousPagerAdapter(Object... objects) {
        super(objects);
    }

    /**
     * Constructor
     *
     * @param objects The objects to represent in the RecyclerView.
     */
    public HeterogeneousPagerAdapter(List<Object> objects) {
        super(objects);
    }

    @Override
    public abstract int getItemViewType(int position);

}
