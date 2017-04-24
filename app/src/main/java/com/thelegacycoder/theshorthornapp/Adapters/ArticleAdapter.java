package com.thelegacycoder.theshorthornapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    public ArticleAdapter(Context context, ArrayList<Article> articles, ClickHandler clickHandler) {
        this.articles = articles;
        this.context = context;
        this.clickHandler = clickHandler;
    }

    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.article_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleAdapter.ViewHolder holder, int position) {
        holder.title.setText(articles.get(position).getTitle());
//        holder.description.setText(articles.get(position).getDescription());
        holder.author.setText(articles.get(position).getAuthor());
        //(articles.get(position).getImageLink());
    }

    @Override
    public int getItemCount() {
        System.out.println(articles.size());
        return articles.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, author;
        ImageView image;
        Button reportButton, shareButton, likeButton;

        ViewHolder(final View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            // description = (TextView) itemView.findViewById(R.id.description);
            author = (TextView) itemView.findViewById(R.id.author);
            image = (ImageView) itemView.findViewById(R.id.image);

            reportButton = (Button) itemView.findViewById(R.id.report_button);
            likeButton = (Button) itemView.findViewById(R.id.like_button);
            shareButton = (Button) itemView.findViewById(R.id.share_button);

            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onReportClick(articles.get(getAdapterPosition()));
                }
            });
            shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onShareClick(itemView, articles.get(getAdapterPosition()));
                }
            });
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickHandler.onLikeClick((Button) view, articles.get(getAdapterPosition()));
                }
            });

        }

    }

    public interface ClickHandler {
        void onReportClick(Article article);

        void onShareClick(View view, Article article);

        void onLikeClick(Button likeButton, Article article);
    }
}
