package com.cncoding.teazer.home.discover.adapters;

import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.CustomLinearLayoutManager;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.discover.BaseDiscoverFragment;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.utilities.diffutil.MyInterestsDiffCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.support.v7.widget.LinearLayoutManager.HORIZONTAL;
import static android.view.LayoutInflater.from;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListAdapter extends RecyclerView.Adapter<MyInterestsListAdapter.MyInterestsViewHolder> {

    private Map<String, ArrayList<PostDetails>> myInterests;
    private BaseDiscoverFragment fragment;

    public MyInterestsListAdapter(BaseDiscoverFragment fragment) {
        this.fragment = fragment;
        if (this.myInterests == null) this.myInterests = new HashMap<>();
    }

    @Override public MyInterestsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyInterestsViewHolder(from(parent.getContext()).inflate(R.layout.item_my_interests, parent, false));
    }

    @Override public void onBindViewHolder(MyInterestsViewHolder holder, int position) {
        try {
            if (position < 3) {
                String categoryName = (String) myInterests.keySet().toArray()[position];
                holder.header.setText(categoryName);

                holder.recyclerView.setLayoutManager(new CustomLinearLayoutManager(fragment.getContext(), HORIZONTAL, false));
                holder.recyclerView.setAdapter(new MyInterestsListItemAdapter(myInterests.get(categoryName), fragment));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void onBindViewHolder(MyInterestsViewHolder holder, int position, List<Object> payloads) {
        if (payloads == null || payloads.isEmpty()) {
            onBindViewHolder(holder, position);
            return;
        }
        if (payloads.get(0) instanceof List) {
            try {
                //noinspection unchecked
                List<PostDetails> postDetailsList = (List<PostDetails>) payloads.get(0);
                if (postDetailsList != null)
                    ((MyInterestsListItemAdapter) holder.recyclerView.getAdapter()).updatePosts(postDetailsList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyDataChanged() {
        fragment.getParentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        });
    }

    public void updatePosts(Map<String, ArrayList<PostDetails>> newMyInterests) {
        try {
            final DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyInterestsDiffCallback(myInterests, newMyInterests));
            myInterests.clear();
            myInterests.putAll(newMyInterests);
            fragment.getParentActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    result.dispatchUpdatesTo(MyInterestsListAdapter.this);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            try {
                addPosts(newMyInterests);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void addPosts(Map<String, ArrayList<PostDetails>> myInterestsMap) {
        if (myInterests == null) myInterests = new HashMap<>();
        else myInterests.clear();
        myInterests.putAll(myInterestsMap);
        notifyDataChanged();
    }

    @Override public int getItemCount() {
        return myInterests.size() >= 3 ? 3 : myInterests.size();
    }

    class MyInterestsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.interests_header) ProximaNovaRegularTextView header;
        @BindView(R.id.item_my_interests_list) RecyclerView recyclerView;

        MyInterestsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}