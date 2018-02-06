package com.cncoding.teazer.home.discover.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cncoding.teazer.R;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.discover.DiscoverFragment;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.post.PostDetails;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * Created by Prem $ on 11/19/2017.
 */

public class MyInterestsListAdapter extends RecyclerView.Adapter<MyInterestsListAdapter.ViewHolder> {

    private ArrayList<Category> myInterestsCategoriesArrayList;
    private Map<String, ArrayList<PostDetails>> myInterests;
    private Context context;
    private DiscoverFragment.OnDiscoverInteractionListener mListener;

    public MyInterestsListAdapter(ArrayList<Category> myInterestsCategoriesArrayList, Map<String, ArrayList<PostDetails>> myInterests,
                                  Context context, DiscoverFragment.OnDiscoverInteractionListener mListener) {
        this.myInterestsCategoriesArrayList = myInterestsCategoriesArrayList;
        this.myInterests = myInterests;
        this.context = context;
        this.mListener = mListener;
    }

    @Override
    public MyInterestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_interests, parent, false);
        return new MyInterestsListAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyInterestsListAdapter.ViewHolder holder, int position) {
        try {
            if (position < 3) {
                String categoryName = (String) myInterests.keySet().toArray()[position];
                holder.header.setText(categoryName);

                holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                holder.recyclerView.setAdapter(
                        new MyInterestsListItemAdapter(
                                myInterests.get(categoryName), context, mListener));
//                ArrayList<PostDetails> postDetailsArrayList =
//                      myInterests.get(myInterestsCategoriesArrayList.get(position).getCategoryName());
//                if (!postDetailsArrayList.isEmpty()) {
//                } else {
//                    holder.layout.setVisibility(GONE);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return myInterests.size() >= 3 ? 3 : myInterests.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout) LinearLayout layout;
        @BindView(R.id.interests_header) ProximaNovaRegularTextView header;
        @BindView(R.id.item_my_interests_list) RecyclerView recyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}