package com.example.retro.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.example.retro.utils.AppUtils;
import com.google.gson.annotations.SerializedName;

import javax.xml.transform.Source;

public class RetroPhoto implements Parcelable {

    @SerializedName("albumID")
    private Integer albumId;

    @SerializedName("id")
    private Integer id;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    private Source source;
    private static int increment = 0;

    public RetroPhoto() {
        id = ++increment;
    }

    protected RetroPhoto(Parcel in) {
        albumId = in.readInt();
        id = in.readInt();
        title = in.readString();
        url = in.readString();
        thumbnailUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(thumbnailUrl);
        dest.writeInt(albumId);
    }

    public static final Creator<RetroPhoto> CREATOR = new Creator<RetroPhoto>() {
        @Override
        public RetroPhoto createFromParcel(Parcel in) {
            return new RetroPhoto(in);
        }

        @Override
        public RetroPhoto[] newArray(int size) {
            return new RetroPhoto[size];
        }
    };

    public Integer getAlbumId() {
        return albumId;
    }

    public void setAlbumId(Integer albumId) {
        this.albumId = albumId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static DiffUtil.ItemCallback<RetroPhoto> DIFF_CALLBACK = new DiffUtil.ItemCallback<RetroPhoto>() {
        @Override
        public boolean areItemsTheSame(@NonNull RetroPhoto oldItem, @NonNull RetroPhoto newItem) {
            return oldItem.id.equals(newItem.id);
        }

        @Override
        public boolean areContentsTheSame(@NonNull RetroPhoto oldItem, @NonNull RetroPhoto newItem) {
            return oldItem.equals(newItem);
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        RetroPhoto photo = (RetroPhoto) obj;
        return photo.id.equals(this.id);
    }
}
