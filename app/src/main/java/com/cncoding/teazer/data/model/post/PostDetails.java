package com.cncoding.teazer.data.model.post;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.cncoding.teazer.data.model.BaseModel;
import com.cncoding.teazer.data.model.base.Category;
import com.cncoding.teazer.data.model.base.CheckIn;
import com.cncoding.teazer.data.model.base.Medias;
import com.cncoding.teazer.data.model.base.MiniProfile;
import com.cncoding.teazer.data.model.base.TaggedUser;
import com.cncoding.teazer.utilities.common.Annotations.CallType;
import com.cncoding.teazer.utilities.common.CommonUtilities;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 * Created by Prem $ on 12/14/2017.
 */
@Entity
public class PostDetails extends BaseModel implements Parcelable {

    @PrimaryKey @SerializedName("post_id") @Expose private Integer postId;
    @SerializedName("posted_by") @Expose private Integer postedBy;
    @SerializedName("likes") @Expose public Integer likes;
    @SerializedName("total_reactions") @Expose public Integer totalReactions;
    @SerializedName("total_tags") @Expose public Integer totalTags;
    @SerializedName("has_checkin") @Expose public Boolean hasCheckin;
    @SerializedName("title") @Expose public String title;
    @SerializedName("can_react") @Expose public Boolean canReact;
    @SerializedName("can_like") @Expose public Boolean canLike;
    @SerializedName("can_delete") @Expose private Boolean canDelete;
    @SerializedName("is_hided") @Expose private Boolean isHided;
    @SerializedName("is_hided_all") @Expose private Boolean isHidedAll;
    @Embedded(prefix = "postOwner_") @SerializedName("post_owner") @Expose private MiniProfile postOwner;
    @SerializedName("created_at") @Expose private String createdAt;                  //use DateTime.Now.ToString("yyyy-MM-ddThh:mm:sszzz");
    @Embedded(prefix = "checkIn_") @SerializedName("check_in") @Expose public CheckIn checkIn;
    @SerializedName("medias") @Expose public ArrayList<Medias> medias;
    @SerializedName("reactions") @Expose private ArrayList<PostReaction> reactions;
    @SerializedName("tagged_users") @Expose private ArrayList<TaggedUser> taggedUsers;
    @SerializedName("reacted_users") @Expose private ArrayList<ReactedUser> reactedUsers;
    @SerializedName("categories") @Expose private ArrayList<Category> categories;

    public PostDetails(Integer postId, Integer postedBy, Integer likes, Integer totalReactions, Integer totalTags,
                       Boolean hasCheckin, String title, Boolean canReact, Boolean canLike, Boolean canDelete,
                       Boolean isHided, Boolean isHidedAll, MiniProfile postOwner, String createdAt, CheckIn checkIn,
                       ArrayList<Medias> medias, ArrayList<PostReaction> reactions, ArrayList<TaggedUser> taggedUsers,
                       ArrayList<ReactedUser> reactedUsers, ArrayList<Category> categories) {
        this.postId = postId;
        this.postedBy = postedBy;
        this.likes = likes;
        this.totalReactions = totalReactions;
        this.totalTags = totalTags;
        this.hasCheckin = hasCheckin;
        this.title = title;
        this.canReact = canReact;
        this.canLike = canLike;
        this.canDelete = canDelete;
        this.isHided = isHided;
        this.isHidedAll = isHidedAll;
        this.postOwner = postOwner;
        this.createdAt = createdAt;
        this.checkIn = checkIn;
        this.medias = medias;
        this.reactions = reactions;
        this.taggedUsers = taggedUsers;
        this.reactedUsers = reactedUsers;
        this.categories = categories;
    }

    @Ignore public PostDetails(Throwable error) {
        this.error = error;
    }

    public PostDetails(String s) {

    }

    public PostDetails setCallType(@CallType int callType) {
        setCall(callType);
        return this;
    }

    protected PostDetails(Parcel in) {
        postId = in.readByte() == 0 ? null : in.readInt();
        postedBy = in.readByte() == 0 ? null : in.readInt();
        likes = in.readByte() == 0 ? null : in.readInt();
        totalReactions = in.readByte() == 0 ? null : in.readInt();
        totalTags = in.readByte() == 0 ? null : in.readInt();
        byte tmpHasCheckin = in.readByte();
        hasCheckin = tmpHasCheckin == 0 ? null : tmpHasCheckin == 1;
        title = in.readString();
        byte tmpCanReact = in.readByte();
        canReact = tmpCanReact == 0 ? null : tmpCanReact == 1;
        byte tmpCanLike = in.readByte();
        canLike = tmpCanLike == 0 ? null : tmpCanLike == 1;
        byte tmpCanDelete = in.readByte();
        canDelete = tmpCanDelete == 0 ? null : tmpCanDelete == 1;
        byte tmpIsHided = in.readByte();
        isHided = tmpIsHided == 0 ? null : tmpIsHided == 1;
        byte tmpIsHidedAll = in.readByte();
        isHidedAll = tmpIsHidedAll == 0 ? null : tmpIsHidedAll == 1;
        postOwner = in.readParcelable(MiniProfile.class.getClassLoader());
        createdAt = in.readString();
        checkIn = in.readParcelable(CheckIn.class.getClassLoader());
        medias = in.createTypedArrayList(Medias.CREATOR);
        reactions = in.createTypedArrayList(PostReaction.CREATOR);
        taggedUsers = in.createTypedArrayList(TaggedUser.CREATOR);
        reactedUsers = in.createTypedArrayList(ReactedUser.CREATOR);
        categories = in.createTypedArrayList(Category.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (postId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postId);
        }
        if (postedBy == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(postedBy);
        }
        if (likes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(likes);
        }
        if (totalReactions == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalReactions);
        }
        if (totalTags == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(totalTags);
        }
        dest.writeByte((byte) (hasCheckin == null ? 0 : hasCheckin ? 1 : 2));
        dest.writeString(title);
        dest.writeByte((byte) (canReact == null ? 0 : canReact ? 1 : 2));
        dest.writeByte((byte) (canLike == null ? 0 : canLike ? 1 : 2));
        dest.writeByte((byte) (canDelete == null ? 0 : canDelete ? 1 : 2));
        dest.writeByte((byte) (isHided == null ? 0 : isHided ? 1 : 2));
        dest.writeByte((byte) (isHidedAll == null ? 0 : isHidedAll ? 1 : 2));
        dest.writeParcelable(postOwner, flags);
        dest.writeString(createdAt);
        dest.writeParcelable(checkIn, flags);
        dest.writeTypedList(medias);
        dest.writeTypedList(reactions);
        dest.writeTypedList(taggedUsers);
        dest.writeTypedList(reactedUsers);
        dest.writeTypedList(categories);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostDetails> CREATOR = new Creator<PostDetails>() {
        @Override
        public PostDetails createFromParcel(Parcel in) {
            return new PostDetails(in);
        }

        @Override
        public PostDetails[] newArray(int size) {
            return new PostDetails[size];
        }
    };

    public Boolean isHided() {
        return isHided;
    }

    public Boolean isHidedAll() {
        return isHidedAll;
    }

    public Integer getPostId() {
        return postId;
    }

    public Integer getPostedBy() {
        return postedBy;
    }

    public Integer getLikes() {
        return likes;
    }

    public Integer getTotalReactions() {
        return totalReactions;
    }

    public Integer getTotalTags() {
        return totalTags;
    }

    public Boolean hasCheckin() {
        return hasCheckin;
    }

    public String getTitle() {
        return title;
    }

    public Boolean canReact() {
        return canReact;
    }

    public Boolean canLike() {
        return canLike;
    }

    public Boolean canDelete() {
        return canDelete;
    }

    public MiniProfile getPostOwner() {
        return postOwner;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public CheckIn getCheckIn() {
        return checkIn;
    }

    public ArrayList<Medias> getMedias() {
        return medias;
    }

    public ArrayList<TaggedUser> getTaggedUsers() {
        return taggedUsers;
    }

    public ArrayList<ReactedUser> getReactedUsers() {
        return reactedUsers;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public ArrayList<PostReaction> getReactions() {
        return reactions;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public void setTotalReactions(Integer totalReactions) {
        this.totalReactions = totalReactions;
    }

    public void setTotalTags(Integer totalTags) {
        this.totalTags = totalTags;
    }

    public void setHasCheckin(Boolean hasCheckin) {
        this.hasCheckin = hasCheckin;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCanReact(Boolean canReact) {
        this.canReact = canReact;
    }

    public void setCanLike(Boolean canLike) {
        this.canLike = canLike;
    }

    public void setCanDelete(Boolean canDelete) {
        this.canDelete = canDelete;
    }

    public void setHided(Boolean hided) {
        isHided = hided;
    }

    public void setHidedAll(Boolean hidedAll) {
        isHidedAll = hidedAll;
    }

    public void setPostOwner(MiniProfile postOwner) {
        this.postOwner = postOwner;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setCheckIn(CheckIn checkIn) {
        this.checkIn = checkIn;
    }

    public void setMedias(ArrayList<Medias> medias) {
        this.medias = medias;
    }

    public void setReactions(ArrayList<PostReaction> reactions) {
        this.reactions = reactions;
    }

    public void setTaggedUsers(ArrayList<TaggedUser> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public void setReactedUsers(ArrayList<ReactedUser> reactedUsers) {
        this.reactedUsers = reactedUsers;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int hashCode() {
        int result = 10;
        result = 31 * result + postId;
        result = 31 * result + postedBy;
        result = 31 * result + likes;
        result = 31 * result + totalReactions;
        result = 31 * result + totalTags;
        result = 31 * result + (hasCheckin ? 1 : 0);
        result = 31 * result + title.hashCode();
        result = 31 * result + (canReact ? 1 : 0);
        result = 31 * result + (canLike ? 1 : 0);
        result = 31 * result + (canDelete ? 1 : 0);
        result = 31 * result + (isHided ? 1 : 0);
        result = 31 * result + (isHidedAll ? 1 : 0);
        result = 31 * result + createdAt.hashCode();
        result = 31 * result + checkIn.hashCode();
        result = 31 * result + CommonUtilities.hashCode(reactions);
        result = 31 * result + CommonUtilities.hashCode(taggedUsers);
        result = 31 * result + CommonUtilities.hashCode(reactedUsers);
        result = 31 * result + CommonUtilities.hashCode(categories);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PostDetails)) {
            return false;
        } else {
            boolean result;
            result = Objects.equals(postId, ((PostDetails) obj).getPostId()) &&
                    Objects.equals(likes, ((PostDetails) obj).getLikes()) &&
                    Objects.equals(totalReactions, ((PostDetails) obj).getTotalReactions()) &&
                    Objects.equals(hasCheckin, ((PostDetails) obj).hasCheckin()) &&
                    Objects.equals(title, ((PostDetails) obj).getTitle()) &&
                    Objects.equals(canReact, ((PostDetails) obj).canReact()) &&
                    Objects.equals(canLike, ((PostDetails) obj).canLike()) &&
                    Objects.equals(checkIn, ((PostDetails) obj).getCheckIn());
            if (reactions != null && ((PostDetails) obj).getReactions() != null) {
                result = CollectionUtils.isEqualCollection(reactions, ((PostDetails) obj).getReactions());
            }
            if (taggedUsers != null && ((PostDetails) obj).getTaggedUsers() != null) {
                result = CollectionUtils.isEqualCollection(taggedUsers, ((PostDetails) obj).getTaggedUsers());
            }
            if (reactedUsers != null && ((PostDetails) obj).getReactedUsers() != null) {
                result = CollectionUtils.isEqualCollection(reactedUsers, ((PostDetails) obj).getReactedUsers());
            }
            if (categories != null && ((PostDetails) obj).getCategories() != null) {
                result = CollectionUtils.isEqualCollection(categories, ((PostDetails) obj).getCategories());
            }
            return result;
        }
    }
}