package com.thelegacycoder.theshorthornapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aditya on 021, 21 Apr, 2017.
 */

public class Article implements Parcelable {
    String title, description, author, imageLink, category;
    int identifier;

    public Article(int identifier, String title, String description, String author, String imageLink, String category) {
        this.identifier = identifier;
        this.title = title;
        this.description = description;
        this.author = author;
        this.imageLink = imageLink;
        this.category = category;
    }

    public Article() {
    }

    public Article(Parcel parcel) {
        String data[] = new String[6];
        parcel.readStringArray(data);
        this.title = data[0];
        this.description = data[1];
        this.author = data[2];
        this.imageLink = data[3];
        this.identifier = Integer.parseInt(data[4]);
        this.category = data[5];
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


    public String getCategory() {
        return category;
    }

    public int getIdentifier() {
        return identifier;
    }

    public void setIdentifier(int identifier) {
        this.identifier = identifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{getTitle(), getDescription(), getAuthor(), getImageLink(), ""+getIdentifier(), getCategory()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };


    @Override
    public boolean equals(Object obj) {
        if (obj != null) {
            if (obj instanceof Article) {
                Article article = (Article) obj;
                return (article.getIdentifier() ==  (identifier));
            }
        }

        return false;
    }

}
