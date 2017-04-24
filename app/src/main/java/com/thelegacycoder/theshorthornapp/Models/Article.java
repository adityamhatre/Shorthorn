package com.thelegacycoder.theshorthornapp.Models;

/**
 * Created by Aditya on 021, 21 Apr, 2017.
 */

public class Article {
    String title, description, author, imageLink;

    public Article(String title, String description, String author, String imageLink) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.imageLink = imageLink;
    }

    public Article() {
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
}
