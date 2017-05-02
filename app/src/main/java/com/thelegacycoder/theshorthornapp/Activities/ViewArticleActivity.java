package com.thelegacycoder.theshorthornapp.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Models.Article;
import com.thelegacycoder.theshorthornapp.R;

public class ViewArticleActivity extends AppCompatActivity {


    private Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_article);

        article = getIntent().getParcelableExtra("article");

        if (article != null) {
            ((TextView) findViewById(R.id.article_title)).setText(article.getTitle());

            ((WebView) findViewById(R.id.article_description)).loadData("<html><body><p align=\"justify\">" + article.getDescription() + "</p></body></html>", "text/html", "UTF-8");
            AppController.getInstance().getStorageReference().child("articles").child(article.getImageLink()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(ViewArticleActivity.this).load(uri).placeholder(R.drawable.loading).fit().centerInside().into((ImageView) findViewById(R.id.article_image));
                }
            });
        }


    }
}
