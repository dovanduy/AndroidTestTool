package com.android.test.aidl;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by elvis on 2016/10/13.
 * 音乐信息
 */

public class Music implements Parcelable {

    String musicName;//歌名
    String author;//作者
    int  time;//时长（秒）
    String albumName;//专辑
    String saleDate;//发售日期
    String url;//网络连接
    String filePath;//本地文件地址


    protected Music(Parcel in) {
        musicName = in.readString();
        author = in.readString();
        time = in.readInt();
        albumName = in.readString();
        saleDate = in.readString();
        url = in.readString();
        filePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(musicName);
        dest.writeString(author);
        dest.writeInt(time);
        dest.writeString(albumName);
        dest.writeString(saleDate);
        dest.writeString(url);
        dest.writeString(filePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Music> CREATOR = new Creator<Music>() {
        @Override
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        @Override
        public Music[] newArray(int size) {
            return new Music[size];
        }
    };


    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
