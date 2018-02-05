package com.cncoding.teazer.apiCalls;

import android.support.annotation.Nullable;

import com.cncoding.teazer.model.application.DeactivateTypes;
import com.cncoding.teazer.model.application.ReportPostTitlesResponse;
import com.cncoding.teazer.model.base.Authorize;
import com.cncoding.teazer.model.base.Category;
import com.cncoding.teazer.model.discover.VideosList;
import com.cncoding.teazer.model.friends.CircleList;
import com.cncoding.teazer.model.friends.FollowersList;
import com.cncoding.teazer.model.friends.FollowingsList;
import com.cncoding.teazer.model.friends.ProfileInfo;
import com.cncoding.teazer.model.friends.UsersList;
import com.cncoding.teazer.model.giphy.TrendingGiphy;
import com.cncoding.teazer.model.post.LandingPosts;
import com.cncoding.teazer.model.post.LikedUserPost;
import com.cncoding.teazer.model.post.PostDetails;
import com.cncoding.teazer.model.post.PostList;
import com.cncoding.teazer.model.post.PostReactionsList;
import com.cncoding.teazer.model.post.PostUploadResult;
import com.cncoding.teazer.model.post.ReportPost;
import com.cncoding.teazer.model.post.TaggedUsersList;
import com.cncoding.teazer.model.post.UpdatePostRequest;
import com.cncoding.teazer.model.react.GiphyReactionRequest;
import com.cncoding.teazer.model.react.ReactionResponse;
import com.cncoding.teazer.model.react.ReactionUploadResult;
import com.cncoding.teazer.model.react.ReactionsList;
import com.cncoding.teazer.model.react.ReportReaction;
import com.cncoding.teazer.model.updatemobilenumber.ChangeMobileNumber;
import com.cncoding.teazer.model.user.BlockedUsersList;
import com.cncoding.teazer.model.user.DeactivateAccountRequest;
import com.cncoding.teazer.model.user.NotificationsList;
import com.cncoding.teazer.model.user.Profile;
import com.cncoding.teazer.model.user.ProfileUpdateRequest;
import com.cncoding.teazer.model.user.ReportUser;
import com.cncoding.teazer.model.user.SetPasswordRequest;
import com.cncoding.teazer.model.user.UpdateCategories;
import com.cncoding.teazer.model.user.UpdatePasswordRequest;
import com.cncoding.teazer.model.user.UserProfile;
import com.cncoding.teazer.ui.fragment.activity.UpdateMobileNumber;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * Created by Prem $ on 10/3/2017.
 */

 class TeazerApiCall {

    static final int RESPONSE_CODE_200 = 200;
    static final int RESPONSE_CODE_201 = 201;

    /**Invalid JSON Format present in Request Body**/
    public static final int RESPONSE_CODE_400 = 400;

    /**Un-Authorized access**/
    static final int RESPONSE_CODE_401 = 401;

    /**Validation failed**/
    static final int RESPONSE_CODE_412 = 412;

    /**
     * Application interfaces
     * */
    interface ApplicationCalls {

        /**
         * To get the post report types
         * */
        @GET("/api/v1/application/post/report/types")
        Call<List<ReportPostTitlesResponse>> getPostReportTypes();

        /**
         * To get the profile report types
         * */
        @GET("/api/v1/application/profile/report/types")
        Call<List<ReportPostTitlesResponse>> getProfileReportTypes();

       @GET("/api/v1/application/deactivate/types")
        Call<List<DeactivateTypes>> getDeactivationTypesList();

        /**
         * To get the categories list
         * */
        @GET("/api/v1/application/categories")
        Call<ArrayList<Category>> getCategories();



    }

    interface AuthenticationCalls {

        /**
         * Call this service for Signup into application
         * Signup Step 1.
         * @return :
         *      200: If status is true OTP will send to given mobile number.
         *           If Status is false the Username, Email and Phone Number already exist or you may reached maximum OTP retry attempts.
         *   or 400: Invalid JSON Format present in Request Body
         *   or 412: Validation Failed
         * @param signUpBody containing username, first name, last name, email, password, phone number and country code.
         */
        @POST("/api/v1/authentication/signup")
        Call<ResultObject> signUp(@Body Authorize signUpBody);

        /**
         * Call this service for verify the OTP and complete the signup process.
         * Signup Step 1.
         * @return :
         *      200: If status is true OTP will send to given mobile number.
         *           If Status is false the Username, Email and Phone Number already exist or you may reached maximum OTP retry attempts.
         *   or 400: Invalid JSON Format present in Request Body
         *   or 412: Validation Failed
         * @param verifySignUp containing fcm_token, device_id, device_type, username, first name, last name,
         *                     email, password, phone number, country code and otp.
         */
        @POST("/api/v1/authentication/signup/verify")
        Call<ResultObject> verifySignUp(@Body Authorize verifySignUp);

        @POST("/api/v1/authentication/social/signup")
        Call<ResultObject> socialSignUp(@Body Authorize socialSignUpDetails);

        /**
         * Perform sign in with password.
         */
        @POST("/api/v1/authentication/signin/with/password")
        Call<ResultObject> loginWithPassword(@Body Authorize loginWithPassword);

        /**
         * Perform sign in with OTP.
         * Login step 1.
         */
        @POST("/api/v1/authentication/signin/with/otp")
        Call<ResultObject> loginWithOtp(@Body Authorize phoneNumberDetails);

        /**
         * Verify sign in with OTP.
         * Login step 2.
         */
        @POST("/api/v1/authentication/signin/with/otp/verify")
        Call<ResultObject> verifyLoginWithOtp(@Body Authorize verifyLoginWithOtp);

        /**
         * Check username availability, Pass in @param String username
         * */
        @GET("/api/v1/authentication/check/username/{username}")
        Call<ResultObject> checkUsernameAvailability(@Path("username") String username);

        /**
         * Check email availability, Pass in @param String email
         * */
        @GET("/api/v1/authentication/check/email/{email}")
        Call<ResultObject> checkEmailAvailability(@Path("email") String email);

        /**
         * Check phone number availability, Pass in @param String phoneNumber
         * */
        @GET("/api/v1/authentication/check/phonenumber")
        Call<ResultObject> checkPhoneNumberAvailability(
                @Query("countryCode") int countryCode, @Query("phonenumber") long phoneNumber);

        /**
         * To verify the forgot password OTP
         * */
        @GET("/api/v1/authentication/reset/password/verify/otp/{otp}")
        Call<ResultObject> verifyForgotPasswordOtp(@Path("otp") int otp);

        /**
         * Forgot password reset by email
         * Call this service to get OTP for reset the password from forgot password
         * */
        @POST("/api/v1/authentication/reset/password/by/{email_id}")
        Call<ResultObject> requestResetPasswordByEmail(@Path("email_id") String email);

        /**
         * Reset password by OTP
         * Call this service to reset the password by OTP received in email or phone number
         */
        @POST("/api/v1/authentication/reset/password/by/phonenumber")
        Call<ResultObject> requestResetPasswordByPhone(@Body Authorize phoneNumberDetails);

        /**
         * Call this service to reset the password by OTP received in email or phone number.
         * Either you need to send Email or Phone number and Country code along with OTP and New password
         * */
        @POST("/api/v1/authentication/password/reset")
        Call<ResultObject> resetPasswordByOtp(@Body Authorize resetPasswordDetails);



    }

    /**
     * Discover interfaces
     */
    interface DiscoverCalls {

        /**
         * Call this service to get the discover page featured videos lists.
         */
        @GET("/api/v1/discover/featured/videos/{page}")
        Call<PostList> getFeaturedPosts(@Path("page") int page);

        /**
         * Call this service to get the discover page interested category videos when user clicks "View all".
         */
        @GET("/api/v1/discover/interested/category/videos/{category_id}/{page}")
        Call<PostList> getAllInterestedCategoriesVideos(@Path("page") int page, @Path("category_id") int categoryId);

        /**
         * Call this service to get the most popular videos. Call this service when user taps "View All".
         */
        @GET("/api/v1/discover/most/popular/videos/{page}")
        Call<PostList> getAllMostPopularVideos(@Path("page") int page);

        /**
         * Call this service to get the discover page trending category videos of the respected category.
         */
        @GET("/api/v1/discover/trending/category/videos/{category_id}/{page}")
        Call<PostList> getTrendingVideos(@Path("page") int page, @Path("category_id") int categoryId);

        /**
         * Call this service to get discover page landing posts.
         */
        @GET("/api/v1/discover/landing")
        Call<LandingPosts> getDiscoverPagePosts();

        /**
         * Call this service to get users list to send follow request.
         */
        @GET("/api/v1/discover/users/{page}")
        Call<UsersList> getUsersListToFollow(@Path("page") int page);

        /**
         * Call this service to get users list to send follow request with search term.
         */
        @GET("/api/v1/discover/users")
        Call<UsersList> getUsersListToFollowWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

        /**
         * Call this service to get users list to send follow request with search term.
         */
        @GET("/api/v1/discover/videos")
        Call<VideosList> getVideosWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

//        @GET("/api/v1/discover/userinterests/and/trendingcategories")
//        Call
    }

    /**
     * Friends interface
     * */
   interface FriendsCalls {

        /**
         * Get the "my circle" with search term
         * */
        @GET("/api/v1/friend/my/circle")
        Call<ResultObject> getMyCircleWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

        /**
         * Call this service to get the my followings list
         * */
        @GET("/api/v1/friend/my/followings/{page}")
        Call<CircleList> getMyFollowings(@Path("page") int page);

        //by arif

        @GET("/api/v1/friend/my/followings/{page}")
        Call<FollowingsList> getMyFollowing(@Path("page") int page);

        /**
         * Call this service to send a join request by user ID
         * */
        @POST("/api/v1/friend/join/request/by/userid/{user_id}")
        Call<ResultObject> sendJoinRequestByUserId(@Path("user_id") int userId);

        /**
         * Call this service to send a join request by username
         * */
        @POST("/api/v1/friend/join/request/by/username/{user_name}")
        Call<ResultObject> sendJoinRequestByUsername(@Path("user_name") String username);

        /**
         * Call this service to accept the join request
         * */
        @POST("/api/v1/friend/join/request/accept/{notification_id}")
        Call<ResultObject> acceptJoinRequest(@Path("notification_id") int notificationId);

        /**
         * Call this service to delete the join request
         * */
        @DELETE("/api/v1/friend/join/request/delete/{notification_id}")
        Call<ResultObject> deleteJoinRequest(@Path("notification_id") int notificationId);

        /**
         * Call this service to get the my circle list
         * @return If “nextPage” is true some more records present. So, you can call again with increase the page count by 1.
         * If “next_page” is false no more records present.
         * */
        @GET("/api/v1/friend/my/circle/{page}")
        Call<CircleList> getMyCircle(@Path("page") int page);

        /**
         * Call this service to get the my followings list with search term
         * */
        @GET("/api/v1/friend/my/followings")
        Call<ResultObject> getMyFollowingsWithSearchTerm(@Path("page") int page, @Query("searchTerm") String searchTerm);

        /**
         * Call this service to get the friends followings list
         * */
        @GET("/api/v1/friend/followings/{user_id}/{page}")
        Call<FollowingsList> getFriendsFollowings(@Path("page") int page, @Path("user_id") int userId);

        /**
         * Call this service to get the friends followings list with search term
         * */
        @GET("/api/v1/friend/followings/{user_id}")
        Call<ResultObject> getFriendsFollowingsWithSearchTerm(
                @Path("user_id") int userId,
                @Query("page") int page,
                @Query("searchTerm") String searchTerm);

        /**
         * Call this service to get the my followers list
         * */
        @GET("/api/v1/friend/my/followers/{page}")
        Call<FollowersList> getMyFollowers(@Path("page") int page);

        /**
         * Call this service to get the my followers list with search term
         * */
        @GET("/api/v1/friend/my/followers")
        Call<ResultObject> getMyFollowersWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

        /**
         * Call this service to get the friends followers list
         * */
        @GET("/api/v1/friend/followers/{user_id}/{page}")
        Call<FollowersList> getFriendsFollowers(@Path("page") int page, @Path("user_id") int userId);

        /**
         * Call this service to get the friends followers list with search term
         * */
        @GET("/api/v1/friend/followers/{user_id}")
        Call<ResultObject> getFriendsFollowersWithSearchTerm(
                @Path("user_id") int userId,
                @Query("page") int page,
                @Query("searchTerm") String searchTerm);

        /**
         * Call this service to unfollow a user
         * */

        @DELETE("/api/v1/friend/unfollow/{user_id}")
        Call<ResultObject> unfollowUser(@Path("user_id") int userId);

        @DELETE("/api/v1/friend/cancel/join/request/{user_id}")
        Call<ResultObject> cancelRequest(@Path("user_id") int userId);


        @POST("/api/v1/friend/join/request/by/userid/{user_id}")
        Call<ResultObject>followUser(@Path("user_id") int userId);

        /**
         * Call this service to get other's profile information
         * @return “account_type” 1 is a Private account, 2 is a Public account.
         *          “can_join” tell whether you peoples are already friends.
         *          Based on “account_type” you can read either private or public profile.
         * */
        @GET("/api/v1/friend/profile/{user_id}")
        Call<ProfileInfo> getOthersProfileInfo(@Path("user_id") int userId);


//        @GET("/api/v1/friend/profile/{user_id}")
//        Call<Profile> getOthersProfileInfoNoti(@Path("user_id") int userId);

        /**
         * Call this service to Block/Unblock a user
         * @param status should be 1 for block and 2 for unblock
         */
        @POST("/api/v1/friend/block/{user_id}/{status}")
        Call<ResultObject> blockUnblockUser(@Path("user_id") int userId, @Path("status") int status);

        /**
         * Call this service to get blocked users list by you.
         */
        @GET("/api/v1/friend/blocked/users/{page}")
        Call<BlockedUsersList> getBlockedUsers(@Path("page") int page);

        /**
         * Call this service to get users list to send follow request.
         */
        @GET("/api/v1/friend/application/users/{page}")
        Call<UsersList> getUsersListToFollow(@Path("page") int page);

        /**
         * Call this service to get users list to send follow request with search term.
         */
        @GET("/api/v1/friend/application/users")
        Call<UsersList> getUsersListToFollowWithSearchTerm(@Query("page") int page, @Query("searchTerm") String searchTerm);

        @GET("/api/v1/post/liked/users/{post_id}/{page}")
        Call<LikedUserPost>getLikedUsers(@Path("post_id") int postId, @Path("page") int page);


    }

    /**
     * Reaction's interface
     * */
    interface ReactCalls {

        /**
         * Call this service to react for a video
         * @return {@value RESPONSE_CODE_200} : Reacted to video has failed. Due to incorrect post id, not having access to react, already done a reaction.
         *               On all above cases the “status” will be false.
         *         {@value RESPONSE_CODE_201} : Reacted to video Successfully.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @Multipart
        @POST("/api/v1/react/create")
        Call<ReactionUploadResult> uploadReaction(@Part MultipartBody.Part video, @Part("post_id") int postId, @Part("title") String title);

        /**
         * Call this service to like/dislike a reacted video.
         * ‘Status’ should be 1 for like and 2 for dislike
         * @return {@value RESPONSE_CODE_200} : Pre validation failed.
         *         {@value RESPONSE_CODE_201} : Liked Successfully.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @POST("/api/v1/react/like/{react_id}/{status}")
        Call<ResultObject> likeDislikeReaction(@Path("react_id") int reactId, @Path("status") int status);

        /**
         * Call this service to increase the reacted video view count
         * @return {@value RESPONSE_CODE_200} : If “status” is true reacted video view count increased successfully.
         *                                      If “status” is false reacted video view count increased failed.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @POST("/api/v1/react/increase/view/{media_id}")
        Call<ResultObject> incrementReactionViewCount(@Path("media_id") int mediaId);

        /**
         * Call this service to delete a reaction.
         * Only the Video Owner or Reacted Owner has the delete access.
         * @return {@value RESPONSE_CODE_200} : If “status” is true post deleted successfully.
         *                              If “status” is false post deleted failed.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @DELETE("/api/v1/react/delete/{react_id}")
        Call<ResultObject> deleteReaction(@Path("react_id") int reactId);

        /**
         * Call this service to report the reaction.
         * @return {@value RESPONSE_CODE_200} : If “status” is true reported successfully.
         *                                      If “status” is false reported failed or you have already reported.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @POST("/api/v1/react/report")
        Call<ResultObject> reportReaction(@Body ReportReaction reportReaction);

        /**
         * Call this service to hide or show a reaction.
         * ‘Status’ should be 1 to hide and 2 to show the reaction.
         * @return {@value RESPONSE_CODE_200} : If status is true Hided/Un Hided Successfully.
         *                                      If status is false Hide/Un Hide failed.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @POST("/api/v1/react/hide/{react_id}/{status}")
        Call<ResultObject> hideOrShowReaction(@Path("react_id") int reactId, @Path("status") int status);

        /**
         * Call this service to get the reactions of 
         * @return {@value RESPONSE_CODE_200} : If “nextPage” is true some more records present,
         *                                      so you can call again after incrementing the page count by 1,
         *                                      If “next_page” is false no more records present.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @GET("/api/v1/react/my/reactions/{page}")
        Call<ReactionsList> getMyReactions(@Path("page") int page);
        /**
         * Call this service to get the reactions of friends.
         * @return {@value RESPONSE_CODE_200} : If “nextPage” is true some more records present,
         *                                      so you can call again after incrementing the page count by 1,
         *                                      If “next_page” is false no more records present.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @GET("/api/v1/react/friend/reactions/{friend_id}/{page}")
        Call<ResultObject> getFriendsReactions(@Path("page") int page, @Path("friend_id") int friend_id);

        /**
         * Call this service to get the reactions hidden by 
         * @return {@value RESPONSE_CODE_200} : If “nextPage” is true some more records present,
         *                                      so you can call again after incrementing the page count by 1,
         *                                      If “next_page” is false no more records present.
         *         {@value RESPONSE_CODE_401} : Un-Authorized access.
         *         {@value RESPONSE_CODE_412} : Validation failed.
         * */
        @GET("/api/v1/react/my/hided/reactions/{page}")
        Call<ResultObject> getHiddenReactions(@Path("page") int page);

        //Call this service to get reaction data by reaction id
        @GET("/api/v1/react/details/{react_id}")
        Call<ReactionResponse> getReactionDetail(@Path("react_id") int reactId);

        //Call this service to post reaction using Giphy
        @POST("/api/v1/react/by/gif")
        Call<ResultObject> createReactionByGiphy(@Body GiphyReactionRequest giphyReactionRequest);

    }

    /**
     * Posts interface
     */
    interface Posts {

        /**
         * Call this service to post a video
         * */
        @Multipart
        @POST("/api/v1/post/create")
        Call<PostUploadResult> uploadVideo(
                @Part MultipartBody.Part video,
                @Part("title") String title,
                @Part("location") String location,
                @Part("latitude") double latitude,
                @Part("longitude") double longitude,
                @Part("tags") String tags,
                @Part("categories") String categories
        );

        @PUT("/api/v1/post/update")
        Call<PostUploadResult> updatePost(@Body UpdatePostRequest updatePostRequest);

        /**
         * Call this service to like/dislike a video.
         * @param status should be 1 for like and 2 for dislike
         * @return 201 : Liked Successfully
         *      or 200 : Pre validation failed
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         */
        @POST("/api/v1/post/like/{post_id}/{status}")
        Call<ResultObject> likeDislikePost(@Path("post_id") int postId, @Path("status") int status);

        /**
         * Call this service to increase the video view count
         * @return 200 : status <code>true</code> if the video view count increased successfully and
         *                      <code>false</code> if the video view count increased failed.
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @POST("/api/v1/post/increase/view/{media_id}")
        Call<ResultObject> incrementViewCount(@Path("media_id") int mediaId);

        /**
         * Call this service to delete a post
         * @return 200 : status <code>true</code> if the post is deleted successfully and
         *                      <code>false</code> if deleting the post failed.
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @DELETE("/api/v1/post/delete/{post_id}")
        Call<ResultObject> deletePost(@Path("post_id") int postId);


        @DELETE("/api/v1/post/delete/{post_id}")
        Call<ResultObject> deletePostVideo(@Path("post_id") int postId);

        /**
         * Call this service to report a post
         * @return 200 : status <code>true</code> if the post is reported successfully and
         *                      <code>false</code> if reporting the post failed or you have already reported.
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @POST("/api/v1/post/report")
        Call<ResultObject> reportPost(@Body ReportPost reportPostDetails);



        /**
         * Call this service to like a video.
         * @param status should be 1 for hiding and 2 for showing the post.
         * @return 200 : status <code>true</code> if the post is Hided/Shown successfully and
         *                      <code>false</code> if hiding/showing the post failed.
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @POST("/api/v1/post/hide/{post_id}/{status}")
        Call<ResultObject> hideOrShowPost(@Path("post_id") int postId, @Path("status") int status);

        /**
         * Call this service to get the post details
         * @return 200 : PostDetails details
         *               Returns tagged user to {@link PostDetails}
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @GET("api/v1/post/video/details/{post_id}")
        Call<PostDetails> getPostDetails(@Path("post_id") int postId);

        /**
         * Call this service to get the tagged users of post.
         * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
         *               If “next_page” is false no more records present.
         *               Returns tagged user to {@link TaggedUsersList}
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @GET("/api/v1/post/tagged/users/{post_id}/{page}")
        Call<TaggedUsersList> getTaggedUsers(@Path("post_id") int postId, @Path("page") int page);

        /**
         * Call this service to get videos hided by the 
         * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
         *               If “next_page” is false no more records present.
         *               Returns tagged user to {@link PostDetails}
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @GET("/api/v1/post/my/hided/videos/{page}")
        Call<PostList> getHiddenPosts(@Path("page") int page);

        /**
         * Call this service to get the home page post lists
         * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
         *               If “next_page” is false no more records present.
         *               Returns tagged user to {@link PostList}
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @GET("/api/v1/post/home/posts/{page}")
        Call<PostList> getHomePagePosts(@Path("page") int page);

        /**
         * Call this service to get the videos posted by user
         * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
         *               If “next_page” is false no more records present.
         *               Returns tagged user to {@link PostList}
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @GET("/api/v1/post/my/videos/{page}")
        Call<PostList> getPostedVideos(@Path("page") int page);


        @GET("/api/v1/post/friend/videos/{friend_id}/{page}")
        Call<PostList> getVideosPostedByFriends(@Path("page") int page, @Path("friend_id") int friendId);

        @GET("/api/v1/post/friend/videos/{friend_id}/{page}")
        Call<PostList> getVideosPostedByFriend(@Path("page") int page, @Path("friend_id") int friendId);

        /**
         * Call this service to get the reactions of a post.
         * @return 200 : If “nextPage” is true some more records present so you can call again with increase the page count by 1,
         *               If “next_page” is false no more records present.
         *               Returns tagged user to {@link PostReactionsList}
         *      or 401 : Un-Authorized access
         *      or 412 : Validation Failed
         * */
        @GET("/api/v1/post/video/reactions/{post_id}/{page}")
        Call<PostReactionsList> getReactionsOfPost(@Path("post_id") int postId, @Path("page") int page);


        @GET("/api/v1/post/my/hided/videos/{page}")
        Call<PostList>getHiddenVideosList(@Path("page") int page);

        @POST("/api/v1/post/unhide/all/posts/{user_id}")
        Call<ResultObject> getAllHiddenVideosList(@Path("user_id") int postId);




    }

    /**
     * User actions
     */
    interface UserCalls {


//        @GET("/api/DepartmentCRUD/FindAllDepartment")
//        Call<List<Test1>>  getUserDertail();

        /**
         * To update a user profile media
         * Call this service to update a user profile media
         * */
        @Multipart
        @POST("/api/v1/user/update/profile/media")
        Call<ResultObject> updateUserProfileMedia(@Part MultipartBody.Part media);

        /**
         * Reset the FCM Token
         * Call this service for update the FCM Token.
         * */
        @PUT("/api/v1/user/update/fcm/{fcmToken}")
        Call<ResultObject> resetFcmToken(
                @Nullable @Part("Authorization") String header,
                @Path("fcmToken") String token
        );

        /**
         * Set account as Private or Public
         * Call this service to make a profile as Private or Public.
         * Send accountType as @int 1 for Private, @int 2 for Public
         * */
        @PUT("/api/v1/user/profile/visibility")
        Call<ResultObject> setAccountVisibility(@Query("accountType") int accountType);

        /**
         * Get user profile
         * Call this service to get user profile details
         * @return {@link UserProfile}
         * */
        @GET("/api/v1/user/profile")
        Call<UserProfile> getUserProfile();


        @POST("/api/v1/user/change/phonenumber")
        Call<ResultObject> changeMobileNumber(@Body ChangeMobileNumber changeMobileNumber);


        @PUT("/api/v1/user/change/phonenumber/verify/otp")
        Call<ResultObject> updateMobileNumber(@Body UpdateMobileNumber updateMobileNumber);


        @GET("/api/v1/user/profile")
        Call<Profile> getUserProfileDetail();

        /**
         * Update user profile
         * Call this service to update user profile.
         * */
        @PUT("/api/v1/user/update/profile")
        Call<ResultObject> updateUserProfile(@Body ProfileUpdateRequest updateProfileDetails);

        /**
         * Call this service to update account password.
         * */
        @PUT("/api/v1/user/update/password")
        Call<ResultObject> updatePassword(@Body UpdatePasswordRequest updatePasswordDetails);

        /**
         * Call this service to update account password.
         * */
        @PUT("api/v1/user/set/new/password")
        Call<ResultObject> setPassword(@Body SetPasswordRequest setPasswordDetails);

        /**
         * Call this service to get Following Notification.
         * */
        @GET("/api/v1/user/notifications/followings/{page}")
        Call<NotificationsList> getFollowingNotifications(@Path("page") int page);

        /**
         * Call this service to get Request Notification.
         * */
        @GET("/api/v1/user/notifications/requests/{page}")
        Call<NotificationsList> getRequestNotifications(@Path("page") int page);

        @POST("/api/v1/user/update/categories")
        Call<ResultObject> updateCategories(@Body UpdateCategories categories);

        /**
         * Invalidate the AuthToken
         * Call this service for Logout the 
         * */
        @DELETE("/api/v1/user/logout")
        Call<ResultObject> logout(@Header("Authorization") String header);

        @DELETE("/api/v1/user/remove/profile/media")
        Call<ResultObject> removeProfilePic();

        @POST("/api/v1/user/report")
        Call<ResultObject> reportUser(@Body ReportUser reportuser);

        @POST("/api/v1/user/deactivate")
        Call<ResultObject> deactivateAccount(@Body DeactivateAccountRequest deactivateAccountRequest);

        @PUT("api/v1/user/reset/notification/count")
        Call<ResultObject> resetUnreadNotification(@Query("type") int notificatioType);
    }

    /**
     * Giphy actions
     */
    interface GiphyCalls {

        @GET("/v1/gifs/trending")
        Call<TrendingGiphy> getTrendingGiphys(@Query("api_key") String api_key, @Query("limit") int limit, @Query("offset") int offset, @Query("rating") String rating);

        @GET("/v1/gifs/search")
        Call<TrendingGiphy> searchGiphy(@Query("api_key") String api_key, @Query("limit") int limit, @Query("offset") int offset, @Query("rating") String rating, @Query("lang") String lang, @Query("q") String query);

    }
}