package com.cncoding.teazer.data.model.post;

import com.cncoding.teazer.data.model.base.ProfileMedia;
import com.cncoding.teazer.data.model.friends.FollowInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * Created by farazhabib on 29/12/17.
 */

public class LikedUser {

    @SerializedName("user_id") @Expose private Integer userId;
    @SerializedName("user_name") @Expose private String userName;
    @SerializedName("first_name") @Expose private String firstName;
    @SerializedName("last_name") @Expose private String lastName;
    @SerializedName("gender") @Expose private Integer gender;
    @SerializedName("my_self") @Expose private Boolean mySelf;
    @SerializedName("is_blocked_you") @Expose private Boolean isBlockedYou;
    @SerializedName("has_profile_media") @Expose private Boolean hasProfileMedia;
    @SerializedName("follow_info") @Expose private FollowInfo followInfo;
    @SerializedName("you_blocked") @Expose private Boolean youBlocked;
    @SerializedName("account_type") @Expose private Integer accountType;
    @SerializedName("profile_media") @Expose private ProfileMedia profileMedia;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getMySelf() {
        return mySelf;
    }

    public void setMySelf(Boolean mySelf) {
        this.mySelf = mySelf;
    }

    public Boolean getIsBlockedYou() {
        return isBlockedYou;
    }

    public void setIsBlockedYou(Boolean isBlockedYou) {
        this.isBlockedYou = isBlockedYou;
    }

    public Boolean getHasProfileMedia() {
        return hasProfileMedia;
    }

    public void setHasProfileMedia(Boolean hasProfileMedia) {
        this.hasProfileMedia = hasProfileMedia;
    }

    public Boolean getYouBlocked() {
        return youBlocked;
    }

    public void setYouBlocked(Boolean youBlocked) {
        this.youBlocked = youBlocked;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }

    public FollowInfo getFollowInfo() {
        return followInfo;
    }

    public void setFollowInfo(FollowInfo followInfo) {
        this.followInfo = followInfo;
    }

    public ProfileMedia getProfileMedia() {
        return profileMedia;
    }

    public void setProfileMedia(ProfileMedia profileMedia) {
        this.profileMedia = profileMedia;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }
}