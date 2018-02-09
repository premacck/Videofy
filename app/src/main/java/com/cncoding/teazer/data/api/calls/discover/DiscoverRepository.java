package com.cncoding.teazer.data.api.calls.discover;

import android.arch.lifecycle.LiveData;

import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.post.LandingPosts;
import com.cncoding.teazer.model.post.PostList;

/**
 * 
 * Created by Prem$ on 1/23/2018.
 */

public interface DiscoverRepository {

    LiveData<PostList> getFeaturedPosts(int page);

    LiveData<PostList> getAllInterestedCategoriesVideos(int page, int categoryId);

    LiveData<PostList> getAllMostPopularVideos(int page);

    LiveData<PostList> getTrendingVideos(int page, int categoryId);

    LiveData<LandingPosts> getDiscoverPagePosts();

    LiveData<UsersList> getUsersListToFollow(int page);

    LiveData<UsersList> getUsersListToFollowWithSearchTerm(int page, String searchTerm);

    LiveData<VideosList> getVideosWithSearchTerm(int page, String searchTerm);
}