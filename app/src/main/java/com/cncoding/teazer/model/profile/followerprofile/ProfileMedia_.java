package com.cncoding.teazer.model.profile.followerprofile;

/**
 * Created by farazhabib on 13/11/17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileMedia_ {

    @SerializedName("picture_id")
    @Expose
    private Integer pictureId;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("is_image")
    @Expose
    private Boolean isImage;
    @SerializedName("dimension")
    @Expose
    private String dimension;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("media_dimension")
    @Expose
    private MediaDimension_ mediaDimension;

    public Integer getPictureId() {
        return pictureId;
    }

    public void setPictureId(Integer pictureId) {
        this.pictureId = pictureId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Boolean getIsImage() {
        return isImage;
    }

    public void setIsImage(Boolean isImage) {
        this.isImage = isImage;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public MediaDimension_ getMediaDimension() {
        return mediaDimension;
    }

    public void setMediaDimension(MediaDimension_ mediaDimension) {
        this.mediaDimension = mediaDimension;
    }

}