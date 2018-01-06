package com.cncoding.teazer.home.notifications;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cncoding.teazer.R;
import com.cncoding.teazer.apiCalls.ApiCallingService;
import com.cncoding.teazer.apiCalls.ResultObject;
import com.cncoding.teazer.customViews.EndlessRecyclerViewScrollListener;
import com.cncoding.teazer.customViews.ProximaNovaRegularTextView;
import com.cncoding.teazer.home.BaseFragment;
import com.cncoding.teazer.model.user.Notification;
import com.cncoding.teazer.model.user.NotificationsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.cncoding.teazer.utilities.SharedPrefs.setFollowingNotificationCount;

/**
 * A fragment representing a list of Items.
 */
public class FollowingNotificationsTabFragment extends BaseFragment {

    @BindView(R.id.list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_notifications)
    ProximaNovaRegularTextView noNotifications;

//    private OnListFragmentInteractionListener mListener;
    private NotificationsList notificationsList;
    private NotificationsAdapter adapter;
    private Call<NotificationsList> notificationsListCall;
    private Call<ResultObject> resetNotificationCall;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FollowingNotificationsTabFragment() {
    }

    public static FollowingNotificationsTabFragment newInstance() {
        return new FollowingNotificationsTabFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notifications_tab, container, false);
        ButterKnife.bind(this, rootView);
        notificationsList = new NotificationsList(new ArrayList<Notification>(), 0, false);

        adapter = new NotificationsAdapter(getContext(), true, notificationsList);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if (is_next_page)
                    getFollowingNotifications(page);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                scrollListener.resetState();
                getFollowingNotifications(1);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        resetFollowingNotification();

        if (notificationsList.getNotifications() != null && notificationsList.getNotifications().isEmpty())
            getFollowingNotifications(1);

    }
    
    private void getFollowingNotifications(final int page) {
        notificationsListCall = ApiCallingService.User.getFollowingNotifications(page, getContext());

        if (!notificationsListCall.isExecuted())
            notificationsListCall.enqueue(new Callback<NotificationsList>() {
                @Override
                public void onResponse(Call<NotificationsList> call, Response<NotificationsList> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                is_next_page = response.body().isNextPage();
                                if (response.body().getNotifications().size() > 0) {
                                    if (page == 1) notificationsList.getNotifications().clear();

                                    swipeRefreshLayout.setVisibility(View.GONE);
                                    noNotifications.setVisibility(View.VISIBLE);
                                    notificationsList.getNotifications().addAll(response.body().getNotifications());
                                    recyclerView.getRecycledViewPool().clear();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    if (page == 1) {
                                        swipeRefreshLayout.setVisibility(View.GONE);
                                        noNotifications.setVisibility(View.VISIBLE);
                                    }
                                }
                            } else {
                                Toast.makeText(getContext(), response.code() + " : " + response.message(),
                                        Toast.LENGTH_SHORT).show();
                            }
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<NotificationsList> call, Throwable t) {
                    t.printStackTrace();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
    }

    private void resetFollowingNotification() {
        resetNotificationCall = ApiCallingService.User.resetUnreadNotification(getContext(), 1);

        if (!resetNotificationCall.isExecuted())
            resetNotificationCall.enqueue(new Callback<ResultObject>() {
                @Override
                public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                    try {
                        if (isAdded()) {
                            if (response.code() == 200) {
                                setFollowingNotificationCount(getContext() ,0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResultObject> call, Throwable t) {
                    t.printStackTrace();
                }
            });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        }
//        else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (notificationsListCall != null)
            notificationsListCall.cancel();
        adapter = null;
    }

//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p/>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnListFragmentInteractionListener {
//        void onListFragmentInteraction(Pojos.User.Notification item);
//    }
}
