package com.thelegacycoder.theshorthornapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aditya on 021, 21 Apr, 2017.
 */

public class Article implements Parcelable {
    String title, description, author, imageLink;

    public Article(String title, String description, String author, String imageLink) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.imageLink = imageLink;
    }

    public Article() {
    }

    public Article(Parcel parcel) {
        String data[] = new String[4];
        parcel.readStringArray(data);
        this.title = data[0];
        this.description = data[1];
        this.author = data[2];
        this.imageLink = data[3];
    }


    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getImageLink() {
        return imageLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{title, description, author, imageLink});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
