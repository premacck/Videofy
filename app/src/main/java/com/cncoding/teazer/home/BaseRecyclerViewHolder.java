package com.cncoding.teazer.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * Created by Prem$ on 2/8/2018.
 */

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(int position);
}