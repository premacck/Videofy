package com.cncoding.teazer.ui.common.tagsAndCategories;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.common.TypeFactory;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaRegularCheckedTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 
 * Created by Prem $ on 10/20/2017.
 */

class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> implements Filterable {

    private ArrayList<MiniProfile> circles;
    private ArrayList<MiniProfile> circlesFiltered;
    private Fragment fragment;
    private String selectedTagsString;
    private SparseBooleanArray selectedTagsArray;
    private SparseArray<MiniProfile> tagsSparseArray;
    private Typeface SEMI_BOLD;
    private Typeface REGULAR;

    TagsAdapter(Context context, ArrayList<MiniProfile> circles, Fragment fragment, String selectedTagsString) {
        this.circles = circles;
        circlesFiltered = circles;
        this.fragment = fragment;
        this.selectedTagsString = selectedTagsString;
        selectedTagsArray = new SparseBooleanArray();
        this.tagsSparseArray = new SparseArray<>();
        TypeFactory typeFactory = new TypeFactory(context);
        SEMI_BOLD = typeFactory.semiBold;
        REGULAR = typeFactory.regular;
    }

    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tags, parent, false);
        return new TagsAdapter.ViewHolder(view);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onBindViewHolder(final TagsAdapter.ViewHolder holder, int position) {
        final MiniProfile circle = circlesFiltered.get(position);
        String name = circle.getFirstName() + " " + circle.getLastName();
        holder.nameView.setText(name);

        if (selectedTagsArray.size() == 0) {
            setCheck(holder.nameView, position, circle,
                    selectedTagsString != null && selectedTagsString.contains(circle.getFirstName()));
        } else {
            setCheck(holder.nameView, position, circle, selectedTagsArray.get(circle.getUserId()));
        }

        Glide.with(fragment)
                .load(circle.getProfileMedia() != null ? circle.getProfileMedia().getThumbUrl() : R.drawable.ic_user_male_dp_small)
                .apply(new RequestOptions().placeholder(R.drawable.ic_user_male_dp_small))
                .into(holder.image);

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTagsArray.put(circle.getUserId(), !holder.nameView.isChecked());
                setCheck(holder.nameView, holder.getAdapterPosition(), circle, !holder.nameView.isChecked());
            }
        });
    }

    private void setCheck(AppCompatCheckedTextView textView, int position, MiniProfile circle, boolean checked) {
        textView.setChecked(checked);
        if (textView.isChecked()) {
            textView.setTextColor(Color.parseColor("#26C6DA"));
            textView.setTypeface(SEMI_BOLD);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_accent, 0);
            tagsSparseArray.put(position, circle);
        }
        else {
            textView.setTextColor(Color.parseColor("#333333"));
            textView.setTypeface(REGULAR);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            tagsSparseArray.delete(position);
        }
    }

    @Override
    public int getItemCount() {
        return circlesFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    circlesFiltered = circles;
                } else {
                    ArrayList<MiniProfile> filteredList = new ArrayList<>();
                    for (MiniProfile profile : circles) {
                        if (profile.getFirstName().toLowerCase().contains(charString.toLowerCase()) ||
                                profile.getLastName().toLowerCase().contains(charString.toLowerCase()) ||
                                profile.getUserName().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(profile);
                        }
                    }
                    circlesFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = circlesFiltered;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                circlesFiltered = (ArrayList<MiniProfile>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tags_item_layout) RelativeLayout rootLayout;
        @BindView(R.id.list_item_checked_thumbnail_image) CircularAppCompatImageView image;
        @BindView(R.id.chip) ProximaNovaRegularCheckedTextView nameView;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    SparseArray<MiniProfile> getSelectedTags() {
        return tagsSparseArray;
    }
}