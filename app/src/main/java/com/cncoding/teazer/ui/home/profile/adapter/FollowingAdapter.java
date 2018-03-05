package com.cncoding.teazer.ui.home.profile.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.cncoding.teazer.R;
import com.cncoding.teazer.data.apiCalls.ApiCallingService;
import com.cncoding.teazer.data.apiCalls.ResultObject;
import com.cncoding.teazer.data.model.friends.UserInfo;
import com.cncoding.teazer.ui.base.BaseFragment;
import com.cncoding.teazer.ui.customviews.common.CircularAppCompatImageView;
import com.cncoding.teazer.ui.customviews.proximanovaviews.ProximaNovaSemiBoldTextView;
import com.cncoding.teazer.ui.home.profile.activity.FollowersListFragment;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewOtherProfile;
import com.cncoding.teazer.ui.home.profile.fragment.FragmentNewProfile2;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.common.ViewUtils.setActionButtonText;

/**
 *
 * Created by farazhabib on 10/11/17.
 */

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private List<UserInfo> list;
    private List<UserInfo> otherlist;
    private BaseFragment fragment;
    private int userfollowingstatus = 0;
    private final int UNBLOCK_STATUS = 2;
    boolean[] selectedPositions;
    List<UserInfo> updateFollowings;

    public FollowingAdapter(BaseFragment fragment, List<UserInfo> otherlist) {
        this.fragment = fragment;
        this.otherlist = otherlist;
        selectedPositions = new boolean[otherlist.size()];
        for (int i = 0; i < otherlist.size(); i++) {
            selectedPositions[i] = false;
        }

     //   for (int i = 0; i < otherlist.size(); i++) {

//            Integer userId, String userName, String firstName, String lastName, Boolean isBlockedYou, Boolean youBlocked,
//                    Boolean mySelf, Integer accountType, Boolean hasProfileMedia, Boolean following, Boolean follower, Boolean requestSent,
////                    Integer requestId, Boolean requestRecieved, Medias profileMedia
//
//            updateFollowings.add(new UserInfo(otherlist.get(i).getUserId(), otherlist.get(i).getIsBlockedYou(), otherlist.get(i).getYouBlocked(), otherlist.get(i).isMySelf(), otherlist.get(i).getAccountType(), otherlist.get(i).getFollowing(),
//                    otherlist.get(i).getFollower(), otherlist.get(i).getRequestSent(), otherlist.get(i).getRequestRecieved()));
     //   }
    }

    public FollowingAdapter(BaseFragment fragment, List<UserInfo> list, int userfollowingstatus) {
        this.fragment = fragment;
        this.list = list;
        this.userfollowingstatus = userfollowingstatus;
//        selectedPositions = new boolean[list.size()];
//        for(int i = 0;i<list.size();i++)
//        {
//            selectedPositions[i] = false;
//        }
//
//        updateFollowings=new ArrayList<>();
    }

    @Override
    public FollowingAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_profile_following, viewGroup, false);
        return new FollowingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FollowingAdapter.ViewHolder viewHolder, final int i) {
        try {

            final int followerId;
            if (userfollowingstatus == FollowersListFragment.USERS_FOLLOWER) {
                final UserInfo cont = list.get(i);
                final String followingname = cont.getUserName();
                final int accounttype = cont.getAccountType();
                final String userType;
                followerId = cont.getUserId();
                userType = fragment.getString(R.string.follow);

                final boolean isfollowersDp = cont.getHasProfileMedia();


                setActionButtonText(fragment.getContext(), viewHolder.action, R.string.following);

                if (isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Glide.with(fragment)
                            .load(followrsDp)
                            .apply(new RequestOptions().skipMemoryCache(false))
                            .into(viewHolder.dp);
                } else {
                    Glide.with(fragment)
                            .load(R.drawable.ic_user_male_dp_small)
                            .apply(new RequestOptions().centerInside().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(viewHolder.dp);
                }


                viewHolder.name.setText(followingname);

                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

//                        if (viewHolder.action.getText().equals(context.getString(R.string.follow))) {
//
//                            followUser(followerId, context, viewHolder, accounttype, i);
//                        }

                        if (viewHolder.action.getText().equals(fragment.getString(R.string.following))) {
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragment.context);
                            dialogBuilder.setMessage("Are you sure you want to Unfollow " + followingname + "?");
                            dialogBuilder.setPositiveButton("CONFIRM", null);
                            dialogBuilder.setNegativeButton("CANCEL", null);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            // override the text color of positive button
                            positiveButton.setTextColor(Color.parseColor("#666666"));
                            // provides custom implementation to positive button click
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    unFollowUser(followerId, fragment.getContext(), viewHolder, accounttype,i,true);
                                    list.remove(i);
                                    notifyItemRemoved(i);
                                    notifyItemRangeChanged(i, list.size());
                                    alertDialog.dismiss();
                                }
                            });

                            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                            negativeButton.setTextColor(Color.parseColor("#999999"));

                            negativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });


                        }


                    }
                });
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fragment.navigation.pushFragment(FragmentNewOtherProfile.newInstance(String.valueOf(followerId)));
                    }
                });
            }

            else {

                final UserInfo cont = otherlist.get(i);
                final String usertype;
                final int requestId;
                final boolean myself = cont.getMySelf();
                final String followername = cont.getUserName();
                final int accounttype = cont.getAccountType();
                final boolean isfollowersDp = cont.getHasProfileMedia();
                final boolean youblocked = cont.getYouBlocked();
                followerId = cont.getUserId();
                final boolean isblockedyou = cont.getIsBlockedYou();
                final boolean isfollower = cont.getFollower();
                final boolean isfollowing = cont.getFollowing();
                final boolean isrequestsent = cont.getRequestSent();
                final boolean isrequestRecieved = cont.getRequestRecieved();

                if (isrequestRecieved) {
                    requestId = cont.getRequestId();}
                else {
                    requestId = 0;
                }
                viewHolder.name.setText(followername);

                if (isfollowersDp) {
                    String followrsDp = cont.getProfileMedia().getMediaUrl();
                    Glide.with(fragment)
                            .load(followrsDp)
                            .apply(new RequestOptions().skipMemoryCache(false).placeholder(R.drawable.ic_user_male_dp_small))
                            .into(viewHolder.dp);
                } else {
                    Glide.with(fragment)
                            .load(R.drawable.ic_user_male_dp_small)
                            .apply(new RequestOptions().centerInside().diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(viewHolder.dp);
                }
                if (myself) {
                    usertype = "";
                    viewHolder.name.setTextColor(Color.parseColor("#333333"));
                    viewHolder.action.setVisibility(View.INVISIBLE);
                } else {
                    if (isblockedyou) {
                        viewHolder.name.setTextColor(Color.GRAY);
                        viewHolder.action.setVisibility(View.INVISIBLE);
                        usertype = "";
                    } else if (youblocked) {
                        viewHolder.name.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                        viewHolder.action.setVisibility(View.VISIBLE);
                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.unblock);
                        usertype = "";
                    } else {
                        viewHolder.name.setTextColor(Color.parseColor("#333333"));
                        viewHolder.action.setVisibility(View.VISIBLE);
                        if (accounttype == 1) {

                            if (isfollowing) {


                                if (isrequestRecieved == true) {
                                    setActionButtonText(fragment.getContext(), viewHolder.action, R.string.accept);
                                    usertype = "Accept";
                                } else {

                                    setActionButtonText(fragment.getContext(), viewHolder.action, R.string.following);
                                    usertype = "Following";
                                }

                            } else {

                                if (isrequestsent) {

                                    if (isrequestRecieved == true) {
                                        Log.d(String.valueOf(cont.getRequestRecieved()), cont.getUserName());
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.accept);
                                        usertype = "Accept";
                                    } else {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.requested);
                                        usertype = "Requested";
                                    }
                                } else {

                                    if (isrequestRecieved == true && isfollower) {

                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    }
                                    else if(isrequestRecieved == true && !isfollower)
                                    {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.accept);
                                        usertype = "Accept";

                                    }
                                    else if(isrequestRecieved == false && isfollower)
                                    {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    }
                                    else if(isrequestRecieved == false)
                                    {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    }
                                    else
                                    {
                                        usertype = "Follow";

                                    }
                                }
                            }

                        } else {

                            if (isfollowing) {


                                if (isrequestRecieved) {
                                    setActionButtonText(fragment.getContext(), viewHolder.action, R.string.accept);
                                    usertype = "Accept";
                                }
                                else {

                                    setActionButtonText(fragment.getContext(), viewHolder.action, R.string.following);
                                    usertype = "Following";
                                }
                            }
                            else {
                                if (isrequestsent) {

                                    if (isrequestRecieved) {
                                        Log.d(String.valueOf(cont.getRequestRecieved()), cont.getUserName());

                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.accept);
                                        usertype = "Accept";
                                    } else {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.requested);
                                        usertype = "Requested";
                                    }
                                } else {


                                    if (isrequestRecieved && isfollower) {

                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    }
                                    else if(isrequestRecieved == true && !isfollower)
                                    {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.accept);
                                        usertype = "Accept";

                                    }
                                    else if(isrequestRecieved == false && isfollower)
                                    {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    }
                                    else if(isrequestRecieved == false)
                                    {
                                        setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                        usertype = "Follow";
                                    }
                                    else
                                    {
                                        usertype = "Follow";

                                    }

                                }
                            }
                        }
                    }
                }

                viewHolder.action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (viewHolder.action.getText().equals(fragment.getString(R.string.accept))) {

                            acceptUser(requestId, viewHolder, accounttype, isfollowing, isrequestsent, i,true);
                        }
                        if (viewHolder.action.getText().equals(fragment.getString(R.string.unblock))) {

                            blockUnblockUsers(followerId, UNBLOCK_STATUS, followername, viewHolder,i);
                        }
                        if (viewHolder.action.getText().equals(fragment.getString(R.string.follow))) {

                            if(isrequestRecieved && isfollower) {

                                acceptUser(requestId, viewHolder, accounttype, isfollowing, isrequestsent, i,false);
                            }
                            else
                            {
                                followUser(followerId, fragment.getContext(), viewHolder, accounttype, i);

                            }


                        }
                        if (viewHolder.action.getText().equals(fragment.getString(R.string.requested))) {
                            cancelRequest(followerId, fragment.getContext(), viewHolder, accounttype,i);

                        }
                        if (viewHolder.action.getText().equals(fragment.getString(R.string.following))) {

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(fragment.context);
                            dialogBuilder.setMessage("Are you sure you want to Unfollow " + followername + "?");
                            dialogBuilder.setPositiveButton("CONFIRM", null);
                            dialogBuilder.setNegativeButton("CANCEL", null);

                            final AlertDialog alertDialog = dialogBuilder.create();
                            alertDialog.show();

                            Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                            // override the text color of positive button
                            positiveButton.setTextColor(Color.parseColor("#666666"));
                            // provides custom implementation to positive button click
                            positiveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    unFollowUser(followerId, fragment.getContext(), viewHolder, accounttype,i,false);
                                    alertDialog.dismiss();
                                }
                            });

                            Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

                            negativeButton.setTextColor(Color.parseColor("#999999"));

                            negativeButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialog.dismiss();
                                }
                            });


                        }

                    }
                });
                viewHolder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (myself) {
                            fragment.navigation.pushFragment(FragmentNewProfile2.newInstance());
                        } else {
                            if (isblockedyou) {
                                Toast.makeText(fragment.getContext(), "you can not view this user profile", Toast.LENGTH_LONG).show();
                            } else {
                                fragment.navigation.pushFragment(FragmentNewOtherProfile.newInstance(String.valueOf(followerId)));
                            }
                        }
                    }
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptUser(final int requestId, final FollowingAdapter.ViewHolder viewHolder, final int accounttype, final boolean isfollowing, final boolean requestSent, final int i,final boolean isAcceptFollow) {

        ApiCallingService.Friends.acceptJoinRequest(requestId, fragment.getContext())
                .enqueue(new Callback<ResultObject>() {
                    @Override
                    public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                        try {
                            if (response.code() == 200) {
                                if (response.body().getStatus()) {

                                    if(isAcceptFollow) {

                                        Toast.makeText(fragment.getContext(), "Request Accepted", Toast.LENGTH_LONG).show();

                                        if (isfollowing) {
                                            setActionButtonText(fragment.getContext(), viewHolder.action, R.string.following);
                                            otherlist.get(i).setFollowing(true);
                                            otherlist.get(i).setRequestRecieved(false);

                                        }else if (requestSent) {

                                            setActionButtonText(fragment.getContext(), viewHolder.action, R.string.requested);
                                            otherlist.get(i).setRequestSent(true);
                                            otherlist.get(i).setRequestSent(false);

                                        } else {
                                            setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                            otherlist.get(i).setFollower(true);
                                        }
                                    }
                                    else
                                        {
                                            if (response.body().getFollowInfo().getFollowing()) {
                                                setActionButtonText(fragment.getContext(), viewHolder.action, R.string.following);

                                                otherlist.get(i).setFollowing(true);
                                                otherlist.get(i).setRequestRecieved(false);

                                                Toast.makeText(fragment.getContext(), "You also have started following", Toast.LENGTH_LONG).show();

                                            } else if (response.body().getFollowInfo().getRequestSent()) {
                                                Toast.makeText(fragment.getContext(), "Your request has been sent", Toast.LENGTH_LONG).show();
                                                setActionButtonText(fragment.getContext(), viewHolder.action, R.string.requested);
                                                otherlist.get(i).setRequestSent(true);
                                                otherlist.get(i).setRequestRecieved(false);


                                            } else {
                                                setActionButtonText(fragment.getContext(), viewHolder.action, R.string.follow);
                                                otherlist.get(i).setFollower(true);
                                            }
                                        }
                                    }




                                else {
                                    Toast.makeText(fragment.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(fragment.getContext(), "Something went wrong, Please try again..", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResultObject> call, Throwable t) {
                        t.printStackTrace();
                        //loader.setVisibility(View.GONE);
                    }
                });
    }

    private void followUser(final int userId, final Context context, final FollowingAdapter.ViewHolder viewHolder,
                            final int accounttype, final int i) {


        ApiCallingService.Friends.followUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {
                            if (accounttype == 1) {
                                setActionButtonText(context, viewHolder.action, R.string.requested);
                                Toast.makeText(context, "You have sent following request", Toast.LENGTH_LONG).show();
                                otherlist.get(i).setRequestSent(true);


                            } else {
                                setActionButtonText(context, viewHolder.action, R.string.following);
                                otherlist.get(i).setFollowing(true);
                                Toast.makeText(context, "You have started following", Toast.LENGTH_LONG).show();
                            }


                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.following);
                            Toast.makeText(context, "You are already following", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(context, "Oops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Oops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cancelRequest(final int userId, final Context context, final FollowingAdapter.ViewHolder viewHolder, final int accounttype,final int i) {

        ApiCallingService.Friends.cancelRequest(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b) {

                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has been cancelled", Toast.LENGTH_LONG).show();
                            otherlist.get(i).setRequestSent(false);
                            otherlist.get(i).setFollowing(false);
                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "Your request has alread been cancelled", Toast.LENGTH_LONG).show();
                            otherlist.get(i).setFollowing(false);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void unFollowUser(int userId, final Context context, final FollowingAdapter.ViewHolder viewHolder, final int accountType,final int i,final boolean isUserProfile)

    {
        ApiCallingService.Friends.unfollowUser(userId, context).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if (response.code() == 200) {
                    try {
                        boolean b = response.body().getStatus();
                        if (b == true) {

                            Toast.makeText(context, "User has been unfollowed", Toast.LENGTH_LONG).show();
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            if(isUserProfile) list.get(i).setFollowing(false);
                            else otherlist.get(i).setFollowing(false);
                        } else {
                            setActionButtonText(context, viewHolder.action, R.string.follow);
                            Toast.makeText(context, "You have already unfollowed", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                        Toast.makeText(context, "Ooops! Something went wrong", Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(context, "Ooops! Something went wrong, please try again..", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void blockUnblockUsers(final int userId, final int status, final String username, final FollowingAdapter.ViewHolder viewHolder,final int i) {
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(fragment.context);
        dialogBuilder.setMessage("Are you sure you want to Unblock " + username + "?");
        dialogBuilder.setPositiveButton("CONFIRM", null);
        dialogBuilder.setNegativeButton("CANCEL", null);

        final android.support.v7.app.AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(Color.parseColor("#666666"));
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blockunBlock(userId, status, viewHolder,i);
                alertDialog.dismiss();
            }
        });
    }

    public void blockunBlock(int userId, final int status, final FollowingAdapter.ViewHolder holder,final int i) {
        ApiCallingService.Friends.blockUnblockUser(userId, status, fragment.getContext()).enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                try {
                    boolean b = response.body().getStatus();
                    if (b == true) {

                        Toast.makeText(fragment.getContext(), "You have Unblocked this user", Toast.LENGTH_SHORT).show();
                        setActionButtonText(fragment.getContext(), holder.action, R.string.follow);
                        otherlist.get(i).setYouBlocked(false);

                    } else {

                        otherlist.get(i).setYouBlocked(false);
                        Toast.makeText(fragment.getContext(), "Already unblocked this user", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(fragment.getContext(), "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {

                Toast.makeText(fragment.getContext(), "Ooops! Something went wrong, please try again..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (userfollowingstatus == FollowersListFragment.USERS_FOLLOWER) {
            return list.size();
        } else {
            return otherlist.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_layout)
        LinearLayout layout;
        @BindView(R.id.dp)
        CircularAppCompatImageView dp;
        @BindView(R.id.name)
        ProximaNovaSemiBoldTextView name;
        @BindView(R.id.action)
        ProximaNovaSemiBoldTextView action;
        @BindView(R.id.decline)
        AppCompatImageView declineBtn;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}