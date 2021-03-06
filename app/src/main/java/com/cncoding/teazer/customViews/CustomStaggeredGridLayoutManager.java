package com.cncoding.teazer.customViews;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

/**
 *
 * Created by Prem $ on 10/29/2017.
 */

public class CustomStaggeredGridLayoutManager extends StaggeredGridLayoutManager {
    public CustomStaggeredGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
