package com.thelegacycoder.theshorthornapp.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Models.Article;
import com.thelegacycoder.theshorthornapp.R;

import java.util.ArrayList;

/**
 * Created by Aditya on 021, 21 Apr, 2017.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    private ArrayList<Article> articles;
    private Context context;
    private ClickHandler clickHandler;

    private ArrayList<Integer> likesList;
    private String MODE;

    public ArticleAdapter(Context context, ArrayList<Article> articles, ClickHandler clickHandler, String MODE) {
        this.articles = articles;
        this.context = context;
        this.MODE = MODE;
        this.clickHandler = clickHandler;
        likesList = new ArrayList<>();
        AppController.getInstance().getDatabase().getReference("users").child(AppController.getInstance().getmAuth().getCurrentUser().getUid()).child("likes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                likesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()
                        ) {
                    likesList.add(Integer.parseInt(snapshot.getKey().replace("article", "")));
                }
                ArticleAdapter.this.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DB ERROR", databaseError.toString());
            }
        });

    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.article_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return articles.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, author;
        ImageView image;
        Button reportButton, shareButton, likeButton, requestToDeleteButton, publishButton, deleteButton, editordeleteButton;

        ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            // description = (TextView) itemView.findViewById(R.id.description);
            author = (TextView) itemView.findViewById(R.id.author);
            image = (ImageView) itemView.findViewById(R.id.image);

            reportButton = (Button) itemView.findViewById(R.id.report_button);
            likeButton = (Button) itemView.findViewById(R.id.like_button);
            shareButton = (Button) itemView.findViewById(R.id.share_button);
            requestToDeleteButton = (Button) itemView.findViewById(R.id.request_delete_button);
            publishButton = (Button) itemView.findViewById(R.id.publish_button);
            deleteButton = (Button) itemView.findViewById(R.id.delete_button);
            editordeleteButton = (Button) itemView.findViewById(R.id.editor_delete_button);

            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onReportClick(articles.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onShareClick(itemView, articles.get(getAdapterPosition()), getAdapterPosition());
                }
            });
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onLikeClick((Button) view, articles.get(getAdapterPosition()), getAdapterPosition(), ((Button) (view)).getText().toString().equalsIgnoreCase("like"));
                }
            });
            requestToDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onRequestToDeleteClick(articles.get(getAdapterPosition()));
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onDeleteClick(articles.get(getAdapterPosition()));

                }
            });
            editordeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onRequestDelete(articles.get(getAdapterPosition()));
                }
            });
            publishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onPublishClick(articles.get(getAdapterPosition()));
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onItemClick(view, articles.get(getAdapterPosition()), getAdapterPosition());
                }
            });

        }

        void bind(int position) {
            Article article = articles.get(position);
            title.setText(article.getTitle());
            author.setText(article.getAuthor());
            if (article.getImageLink() != null) {
                //System.out.println("article" + (articles.size() - position));
                System.out.println("AIL: " + article.getImageLink());
                AppController.getInstance().getStorageReference().child("articles").child(article.getImageLink()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(context).load(uri).placeholder(R.drawable.loading).fit().centerInside().into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println(e.getMessage());
                        image.setImageResource(R.drawable.loading);
                    }
                });

            }

            if (likesList.contains(articles.size() - position)) {
                System.out.println("changing to unlike");
                likeButton.setText("Unlike");
                likeButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else {
                likeButton.setTextColor(context.getResources().getColor(android.R.color.black));
                System.out.println("changing to like");
                likeButton.setText("Like");
            }

            System.out.println(MODE);
            switch (MODE.toLowerCase()) {
                case "writer":
                    likeButton.setVisibility(View.INVISIBLE);
                    reportButton.setVisibility(View.INVISIBLE);
                    shareButton.setVisibility(View.INVISIBLE);
                    requestToDeleteButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    publishButton.setVisibility(View.INVISIBLE);
                    editordeleteButton.setVisibility(View.INVISIBLE);
                    break;
                case "reader":
                    likeButton.setVisibility(View.VISIBLE);
                    reportButton.setVisibility(View.VISIBLE);
                    shareButton.setVisibility(View.VISIBLE);
                    requestToDeleteButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    publishButton.setVisibility(View.INVISIBLE);
                    editordeleteButton.setVisibility(View.INVISIBLE);
                    break;
                case "editor-publish":
                    likeButton.setVisibility(View.INVISIBLE);
                    reportButton.setVisibility(View.INVISIBLE);
                    shareButton.setVisibility(View.INVISIBLE);
                    requestToDeleteButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);
                    publishButton.setVisibility(View.VISIBLE);
                    editordeleteButton.setVisibility(View.INVISIBLE);
                    break;
                case "editor-delete":
                    likeButton.setVisibility(View.INVISIBLE);
                    reportButton.setVisibility(View.INVISIBLE);
                    shareButton.setVisibility(View.INVISIBLE);
                    requestToDeleteButton.setVisibility(View.INVISIBLE);
                    publishButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);
                    editordeleteButton.setVisibility(View.VISIBLE);
                    break;

            }

        }
    }

    public interface ClickHandler {
        void onReportClick(Article article, int position);

        void onShareClick(View view, Article article, int position);

        void onLikeClick(Button likeButton, Article article, int position, boolean liked);

        void onItemClick(View view, Article article, int position);

        void onRequestToDeleteClick(Article article);//writer

        void onPublishClick(Article article);//admin...editor pushlish

        void onDeleteClick(Article article);//admin...editor requested articles

        void onRequestDelete(Article article);//admin
    }
}
