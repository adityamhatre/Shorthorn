package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.thelegacycoder.theshorthornapp.Activities.ViewArticleActivity;
import com.thelegacycoder.theshorthornapp.Adapters.ArticleAdapter;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.Models.Article;
import com.thelegacycoder.theshorthornapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArticleAdapter articleAdapter;
    private RecyclerView recyclerView;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        inflater.inflate(R.layout.fragment_home, wrapper, true);
        return wrapper;
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //((TextView) view.findViewById(R.id.text)).setText(mParam1);

        //showFileChooser();

        if (AppController.getInstance().isLoggedIn()) {
            Toast.makeText(getActivity(), mParam1, Toast.LENGTH_SHORT).show();
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            final ArrayList<Article> articles = new ArrayList<>();


            AppController.getInstance().getDatabase().getReference("articles").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    articles.clear();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        articles.add(postSnapshot.getValue(Article.class));
                    }

                    Collections.reverse(articles);
                    articleAdapter = new ArticleAdapter(getActivity(), articles, new ArticleAdapter.ClickHandler() {
                        @Override
                        public void onReportClick(Article article, int position) {
                            position = articles.size() - position;
                            final int finalPosition = position;
                            AppController.getInstance().getDatabase().getReference("reportedArticles").child("article" + position).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int reportCount;
                                    if (dataSnapshot.getValue() != null) {
                                        reportCount = dataSnapshot.getValue(Integer.class);
                                    } else reportCount = 0;
                                    reportCount++;
                                    AppController.getInstance().getDatabase().getReference("reportedArticles").child("article" + finalPosition).setValue(reportCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Reported article", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }


                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onShareClick(View view, Article article, int position) {
                            view.findViewById(R.id.share_button).setVisibility(View.GONE);
                            view.findViewById(R.id.like_button).setVisibility(View.GONE);
                            view.findViewById(R.id.report_button).setVisibility(View.GONE);

                            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
                            Canvas canvas = new Canvas(returnedBitmap);
                            Drawable bgDrawable = view.getBackground();
                            if (bgDrawable != null)
                                bgDrawable.draw(canvas);
                            else
                                canvas.drawColor(Color.WHITE);
                            view.draw(canvas);

                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            returnedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), returnedBitmap, null, null);
                            Uri uri = Uri.parse(path);

                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            shareIntent.setType("text/plain");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.setType("image/*");
                            getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
                            view.findViewById(R.id.share_button).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.like_button).setVisibility(View.VISIBLE);
                            view.findViewById(R.id.report_button).setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLikeClick(Button likeButton, Article article, int position, boolean liked) {
                            position = articles.size() - position;
                            if (liked) {
                                AppController.getInstance().getDatabase().getReference("users").child(AppController.getInstance().getmAuth().getCurrentUser().getUid()).child("likes").child("article" + position).setValue(true);
                            } else {
                                AppController.getInstance().getDatabase().getReference("users").child(AppController.getInstance().getmAuth().getCurrentUser().getUid()).child("likes").child("article" + position).setValue(null);
                                if (articleAdapter != null) {
                                    articleAdapter.notifyDataSetChanged();
                                }

                            }
                        }

                        @Override
                        public void onItemClick(View view, int position) {
                            startActivity(new Intent(getActivity(), ViewArticleActivity.class));
                        }
                    });
                    recyclerView.setAdapter(articleAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {
            view.setBackgroundColor(getActivity().getResources().getColor(R.color.colorAccent));
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppController.getInstance().getStorageReference().child("articles").child("article2").putFile(data.getData()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                System.out.println("uploaded");
//                articleAdapter.notifyDataSetChanged();
            }
        });

    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
