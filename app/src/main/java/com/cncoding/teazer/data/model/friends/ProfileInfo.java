package com.cncoding.teazer.data.model.friends;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.user.PrivateProfile;
import com.cncoding.teazer.data.model.user.userProfile.TopReactedUser;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * Created by farazhabib on 13/11/17.
 */

public class ProfileInfo extends BaseModel {

    @SerializedName("total_videos") @Expose private Integer totalVideos;
    @SerializedName("total_reactions") @Expose private Integer totalReactions;
    @SerializedName("private_profile") @Expose private PrivateProfile privateProfile;
    @SerializedName("public_profile") @Expose private PublicProfile publicProfile;
    @SerializedName("followers") @Expose private Integer followers;
    @SerializedName("followings") @Expose private Integer followings;
    @SerializedName("follow_info") @Expose private FollowInfo followInfo;
    @SerializedName("is_hided_all_posts") @Expose private Boolean isHidedAllPosts;

    public Boolean getCanLike() {
        return canLike;
    }

    @SerializedName("can_like")
    @Expose
    private Boolean canLike;
    @SerializedName("top_reacted_users")
    @Expose
    private List<TopReactedUser> topReactedUsers = null;

    @SerializedName("total_profile_likes")
    @Expose
    private Integer totalProfileLikes;

    public Boolean getHidedAllPosts() {
        return isHidedAllPosts;
    }
    public List<TopReactedUser> getTopReactedUsers() {
        return topReactedUsers;
    }

    public Integer getTotalProfileLikes() {
        return totalProfileLikes;
    }

    public ProfileInfo(Throwable error) {
        this.error = error;
    }

    public ProfileInfo setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    public Integer getTotalVideos() {
        return totalVideos;
    }

    public void setTotalVideos(Integer totalVideos) {
        this.totalVideos = totalVideos;
    }

    public PrivateProfile getPrivateProfile() {
        return privateProfile;
    }

    public void setPrivateProfile(PrivateProfile privateProfile) {
        this.privateProfile = privateProfile;
    }

    public PublicProfile getPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile(PublicProfile publicProfile) {
        this.publicProfile = publicProfile;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowings() {
        return followings;
    }

    public void setFollowings(Integer followings) {
        this.followings = followings;
    }

    public FollowInfo getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(FollowInfo followInfo) {
        this.followInfo = followInfo;
    }

    public Boolean getIsHidedAllPosts() {
        return isHidedAllPosts;
    }

    public Integer getTotalReactions() {
        return totalReactions;
    }

    public void setTotalReactions(Integer totalReactions) {
        this.totalReactions = totalReactions;
    }

    public void setIsHidedAllPosts(Boolean isHidedAllPosts) {
        this.isHidedAllPosts = isHidedAllPosts;
    }
}