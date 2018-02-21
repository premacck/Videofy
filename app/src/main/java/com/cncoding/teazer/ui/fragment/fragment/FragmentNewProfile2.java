package com.cncoding.teazer.ui.fragment.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cncoding.teazer.R;
import com.cncoding.teazer.adapter.ProfileCreationReactionPagerAdapter;
import com.cncoding.teazer.adapter.ProfileMyCreationAdapter;
import com.cncoding.teazer.adapter.ProfileMyReactionAdapter;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.customViews.CircularAppCompatImageView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaRegularCheckedTextView;
import com.cncoding.teazer.customViews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.friends.PublicProfile;
import com.cncoding.teazer.model.user.UserProfile;
import com.cncoding.teazer.ui.fragment.activity.EditProfile;
import com.cncoding.teazer.ui.fragment.activity.OpenProfilePicActivity;
import com.cncoding.teazer.ui.fragment.activity.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.SharingHelper;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.util.ShareSheetStyle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.FabricAnalyticsUtil.logProfileShareEvent;

/**
 * Created by farazhabib on 19/02/18.
 */

public class FragmentNewProfile2 extends BaseFragment implements ProfileMyCreationAdapter.OnChildFragmentUpdateVideos, ProfileMyReactionAdapter.OnChildFragmentUpdateReaction

{
    private static final int RC_REQUEST_STORAGE = 1001;

    PublicProfile userProfile;
    TextView _creations;
    TextView _followers;
    TextView _following;
    TextView _reactions;

    ProximaNovaRegularCheckedTextView _name;
    ProximaNovaSemiBoldTextView _username;
    ProximaNovaRegularCheckedTextView _detail;
    CircularAppCompatImageView profile_id;

    int totalfollowers;
    int totalfollowing;
    int totalvideos;
    int reactions;
    String firstname;
    String userId;
    String lastname;
    String username;
    String email;
    int accountType;
    boolean hasProfleMedia;
    boolean hasCoverMedia;
    Long mobilenumber;
    int gender;
    int countrycode;
    String detail;
    private String userProfileThumbnail;
    private String userCoverUrl;
    private String userProfileUrl;
    ImageView placeholder;

    Context context;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.sliding_tabs)
    TabLayout tabLayout;
    private FragmentNewProfile2.FollowerListListener mListener;
    AppBarLayout app_bar;

    public static boolean checkpostupdated = false;
    public static boolean checkprofileupdated = false;
    public static boolean checkpicUpdated = false;

    public static FragmentNewProfile2 newInstance(int page) {
        return new FragmentNewProfile2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_profile3, container, false);
        ButterKnife.bind(this, view);
        context=container.getContext();
        final android.support.v7.widget.Toolbar toolbar = view.findViewById(R.id.toolbar);
        getParentActivity().setSupportActionBar(toolbar);
        getParentActivity().getSupportActionBar().setTitle("");
        //  toolbar.setSubtitle("Android-er.blogspot.com");
        //  toolbar.setLogo(android.R.drawable.ic_menu_info_details);

        placeholder = view.findViewById(R.id.background);
        _name = view.findViewById(R.id.name);
        _username = view.findViewById(R.id.username);
        _detail = view.findViewById(R.id.detail);
        _creations = view.findViewById(R.id.creations);
        _followers = view.findViewById(R.id.follower);
        _following = view.findViewById(R.id.following);
        _reactions = view.findViewById(R.id.reaction);
        profile_id = view.findViewById(R.id.profilepic);
        app_bar = view.findViewById(R.id.app_bar);


        _followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFollowerListListener(String.valueOf(0), "User");
            }
        });

        _following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFollowingListListener(String.valueOf(0), "User");
            }
        });


        profile_id.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OpenProfilePicActivity.class);
                intent.putExtra("Image", userProfileUrl);
                intent.putExtra("candelete",true);
                intent.putExtra("gender",gender);
                Pair<View, String> p1 = Pair.create((View)profile_id, "profile");
                Pair<View, String> p2 = Pair.create((View)_username, "text");
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), profile_id, "profile");
                startActivity(intent, options.toBundle());
            }

        });



//        ViewPager viewPager = view.findViewById(R.id.viewpager);
//        viewPager.setAdapter(new TestPagerAdapter(getActivity().getSupportFragmentManager()));
//        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
//        tabLayout.setupWithViewPager(viewPager);

        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // Collapsed
                    _name.setTextColor(Color.parseColor("#FFFFFF"));
                    _username.setTextColor(Color.parseColor("#FFFFFF"));
                    toolbar.setBackgroundResource(R.color.blur);
                    //Blurry.with(getContext()).from(photobitmap).into(placeholder);

                }
                else if (verticalOffset <-200&& verticalOffset>-400)
                {
                    _name.setTextColor(Color.parseColor("#c6c6c6"));
                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur2);


                }
                else if (verticalOffset <0 && verticalOffset>-200)
                {
                    _name.setTextColor(Color.parseColor("#88232323"));
                    _username.setTextColor(Color.parseColor("#88232323"));
                    toolbar.setBackgroundResource(R.color.blur2);

                }
                else if (verticalOffset == 0) {
                    // Expanded
                    _name.setTextColor(Color.parseColor("#000000"));
                    _username.setTextColor(Color.parseColor("#000000"));
                    toolbar.setBackgroundResource(R.color.blur2);


                } else {
                    // Somewhere in between
//                    _name.setTextColor(Color.parseColor("#c6c6c6"));
//                    _username.setTextColor(Color.parseColor("#c6c6c6"));
                    toolbar.setBackgroundResource(R.color.blur2);


                }
            }
        });
        return view;
    }






    @Override
    public void onResume() {
        super.onResume();
        getParentActivity().hideToolbar();

        if (FragmentNewProfile2.checkprofileupdated) {
            updateProfile();
        }

        if(FragmentNewProfile2.checkpostupdated)
        {

            viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext()));
            tabLayout.setupWithViewPager(viewPager);
            FragmentNewProfile2.checkpostupdated=false;
        }


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager.setAdapter(new ProfileCreationReactionPagerAdapter(getChildFragmentManager(), getContext()));
        tabLayout.setupWithViewPager(viewPager);

        getProfileDetail();
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof NewProfileFragment.FollowerListListener) {
            mListener = (FragmentNewProfile2.FollowerListListener) context;
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        getParentActivity().showToolbar();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
         menu.clear();
         inflater.inflate(R.menu.menu_new_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {

            case R.id.ic_settings:
                Intent mintent = new Intent(context, Settings.class);
                mintent.putExtra("AccountType", String.valueOf(accountType));
                mintent.putExtra("UserProfile", userProfile);
                startActivity(mintent);
                break;

            case R.id.share:



                String profileUrl = null;
                try {
                    profileUrl = userProfile.getProfileMedia() == null? null:userProfile.getProfileMedia().getMediaUrl();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                        .setCanonicalIdentifier(String.valueOf(userProfile.getUserId()))
                        .setTitle(userProfile.getFirstName())
                        .setContentDescription("Hi, follow me on Teazer and share cool videos")
                        .setContentImageUrl(profileUrl);

                LinkProperties linkProperties = new LinkProperties()
                        .setChannel("facebook")
                        .setFeature("sharing")
                        .addControlParameter("user_id", String.valueOf(userProfile.getUserId()))
                        .addControlParameter("$desktop_url", "https://teazer.online/")
                        .addControlParameter("$ios_url", "https://teazer.online/");

                ShareSheetStyle shareSheetStyle = new ShareSheetStyle(getActivity(), "Teazer", "Hi, follow me on Teazer and express better")
                        .setCopyUrlStyle(getResources().getDrawable(android.R.drawable.ic_menu_send), "Copy", "Added to clipboard")
                        .setMoreOptionStyle(getResources().getDrawable(android.R.drawable.ic_menu_search), "Show more")
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.INSTAGRAM)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.FACEBOOK)
                        .addPreferredSharingOption(SharingHelper.SHARE_WITH.WHATS_APP)
                        .setAsFullWidthStyle(true)
                        .setSharingTitle("Share With");

                branchUniversalObject.generateShortUrl(getContext(), linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            //fabric event
                            logProfileShareEvent("Branch", userProfile.getEmail(), "Profile", String.valueOf(userProfile.getUserId()));

                            //loader.setVisibility(View.GONE);
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
                            sendIntent.setType("text/plain");
                            startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        }
                        else
                        {

                        }
                        //  loader.setVisibility(View.GONE);
                    }
                });


                break;


            case R.id.edit:

                Intent intent = new Intent(context, EditProfile.class);
                intent.putExtra("UserName", username);
                intent.putExtra("FirstName", firstname);
                intent.putExtra("LastName", lastname);
                intent.putExtra("EmailId", email);
                intent.putExtra("MobileNumber", String.valueOf(mobilenumber));
                intent.putExtra("Gender", String.valueOf(gender));
                intent.putExtra("CountryCode", String.valueOf(countrycode));
                intent.putExtra("ProfileThumb", userProfileThumbnail);
                intent.putExtra("ProfileMedia", userProfileUrl);
                intent.putExtra("CoverMedia", userCoverUrl);

                if (detail == null)
                    intent.putExtra("Detail", "");
                else {
                    intent.putExtra("Detail", detail);
                }
                startActivity(intent);
                break;



        }

        return true;
    }
    @Override
    public void updateVideosCreation(int count) {


        int counter=totalvideos-count;
        totalvideos=counter;
        _creations.setText(String.valueOf(totalvideos));

    }

    @Override
    public void updateReaction(int count) {

        int counter=reactions-count;
        reactions=counter;
        _reactions.setText(String.valueOf(reactions));
    }


    public void getProfileDetail() {
        // blur_bacground.setVisibility(View.GONE);
        // loader.setVisibility(View.VISIBLE);
        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {


                    userProfile = response.body().getUserProfile();
                    firstname = userProfile.getFirstName();
                    lastname = userProfile.getLastName();
                    username = userProfile.getUserName();
                    email = userProfile.getEmail();
                    accountType = userProfile.getAccountType();
                    hasProfleMedia = userProfile.getHasProfileMedia();
                    hasCoverMedia = userProfile.getHasCoverMedia();
                    totalfollowers = response.body().getFollowers();
                    totalfollowing = response.body().getFollowings();
                    totalvideos = response.body().getTotalVideos();
                    userId = String.valueOf(userProfile.getUserId());
                    countrycode = userProfile.getCountryCode();
                    detail = userProfile.getDescription();
                    gender = userProfile.getGender();
                    reactions=response.body().getTotalReactions();

                    Long mobilno = userProfile.getPhoneNumber();

                    if (mobilno == null) {
                        mobilenumber = 0L;
                    } else {
                        mobilenumber = mobilno;
                    }


                    if (userProfile.getHasProfileMedia()) {

                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();

                        Glide.with(context)
                                .load(userProfileUrl)
                                .into(profile_id);
                    }
                    else
                    {
                        if(gender==1)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);
                        }
                        else if(gender==2)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_female_dp)
                                    .into(profile_id);
                        }
                        else
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);
                        }
                    }
                    if (hasCoverMedia)
                    {
                        userCoverUrl=userProfile.getCoverMedia().getMediaUrl();

                        Glide.with(context)
                                .load(userCoverUrl)
                                .into(placeholder);
                    }
                    else
                    {
                    }
                    _detail.setText(detail);
                    _name.setText(firstname + " " + lastname);
                    _username.setText(username);
                    _followers.setText(String.valueOf(totalfollowers));
                    _following.setText(String.valueOf(totalfollowing));
                    _creations.setText(String.valueOf(totalvideos));
                    _reactions.setText(String.valueOf(reactions));

//                    blur_bacground.setVisibility(View.VISIBLE);
//                    loader.setVisibility(View.GONE);
                }
                catch (Exception e) {
//                    blur_bacground.setVisibility(View.GONE);
//                    loader.setVisibility(View.GONE);
//                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //blur_bacground.setVisibility(View.GONE);
                // loader.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });

    }


    public void updateProfile() {

        FragmentNewProfile2.checkprofileupdated = false;
        // loader.setVisibility(View.VISIBLE);
        //blur_bacground.setVisibility(View.GONE);
        ApiCallingService.User.getUserProfile(context).enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                try {

                    userProfile = response.body().getUserProfile();
                    firstname = userProfile.getFirstName();
                    lastname = userProfile.getLastName();
                    username = userProfile.getUserName();
                    email = userProfile.getEmail();
                    accountType = userProfile.getAccountType();
                    hasProfleMedia = userProfile.getHasProfileMedia();
                    hasCoverMedia = userProfile.getHasCoverMedia();
                    totalfollowers = response.body().getFollowers();
                    totalfollowing = response.body().getFollowings();
                    totalvideos = response.body().getTotalVideos();
                    userId = String.valueOf(userProfile.getUserId());
                    gender = userProfile.getGender();

                    Long mobilno = userProfile.getPhoneNumber();
                    if (mobilno == null) {
                        mobilenumber = 0L;
                    } else {
                        mobilenumber = mobilno;
                    }
                    countrycode = userProfile.getCountryCode();
                    detail = userProfile.getDescription();

                    if(FragmentNewProfile2.checkpicUpdated){
                        FragmentNewProfile2.checkpicUpdated=false;
                        userProfileUrl=null;
                    }

                    if (userProfile.getHasProfileMedia()) {
                        userProfileThumbnail = userProfile.getProfileMedia().getThumbUrl();
                        userProfileUrl = userProfile.getProfileMedia().getMediaUrl();

                        Glide.with(context)
                                .load(userProfileUrl)
                                .into(profile_id);

                    }
                    else
                    {
                        if(gender==1)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);
                        }
                        else if(gender==2)
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_female_dp)
                                    .into(profile_id);

                        }
                        else
                        {
                            Glide.with(context)
                                    .load(R.drawable.ic_user_male_dp)
                                    .into(profile_id);

                        }
                        userCoverUrl=null;

                    }
                    if (hasCoverMedia)
                    {
                        userCoverUrl=userProfile.getCoverMedia().getMediaUrl();
                        //userCoverUrl=userProfile.getCoverMedia().getThumbUrl();


                        Glide.with(context)
                                .load(userCoverUrl)
                                .into(placeholder);
                    }
                    else
                    {

                    }
                    _detail.setText(detail);
                    _name.setText(firstname);
                    _username.setText(username);
                    _followers.setText(String.valueOf(totalfollowers));
                    _following.setText(String.valueOf(totalfollowing));
                    _creations.setText(String.valueOf(totalvideos));
                    _reactions.setText(String.valueOf(reactions));

                    //   loader.setVisibility(View.GONE);
                    //  blur_bacground.setVisibility(View.VISIBLE);
                }
                catch (Exception e) {
                    //  loader.setVisibility(View.GONE);
                    e.printStackTrace();

                }
            }
            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                //loader.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    public interface FollowerListListener {

        void onFollowerListListener(String id, String identifier);
        void onFollowingListListener(String id, String identifier);
    }


    static class TestPagerAdapter extends FragmentPagerAdapter {

        public TestPagerAdapter(FragmentManager fm) {

            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new TestFragment();
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "Tab " + (position + 1);
        }
    }




    public static class TestFragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.tab_page, container, false);

            RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(new TestAdapter());

            return view;
        }
    }

    static class TestAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public int getItemCount() {
            return 100;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ((TextView) holder.itemView).setText("List Item " + (position + 1));
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

}

