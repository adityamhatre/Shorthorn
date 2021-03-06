package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.content.DialogInterface;
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
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
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

import static com.google.android.gms.internal.zzs.TAG;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String MODE = "MODE";


    private View filterView;
    private AlertDialog filterDialog;

    private String mParam1;
    private String mParam2;
    public static int articleSize;

    private OnFragmentInteractionListener mListener;
    private ArticleAdapter articleAdapter;
    private RecyclerView articleRecyclerView;
    private ArrayList<Article> articles, tempArticles, filteredArticles;
    private ArrayList<String> categories, selectedCategories;
    private ArticleAdapter.ClickHandler itemsClickHandler;

    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            MODE = mParam1 = getArguments().getString(ARG_PARAM1);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout wrapper = new LinearLayout(getActivity()); // for example
        inflater.inflate(R.layout.fragment_home, wrapper, true);
        setHasOptionsMenu(true);
        return wrapper;
        //return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //((TextView) view.findViewById(R.id.text)).setText(mParam1);

        //showFileChooser();


        if (AppController.getInstance().isLoggedIn()) {
            //Toast.makeText(getActivity(), mParam1, Toast.LENGTH_SHORT).show();
            articleRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            articleRecyclerView.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            articleRecyclerView.setLayoutManager(llm);

            articles = new ArrayList<>();
            tempArticles = new ArrayList<>();
            filteredArticles = new ArrayList<>();
            categories = new ArrayList<>();
            selectedCategories = new ArrayList<>();

            itemsClickHandler = new ArticleAdapter.ClickHandler() {
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
                public void onItemClick(View view, Article article, int position) {
                    startActivity(new Intent(getActivity(), ViewArticleActivity.class).putExtra("article", article).putExtra("MODE", MODE));
                }

                @Override
                public void onRequestToDeleteClick(final Article article) {
                    AppController.getInstance().getDatabase().getReference().child("toBeDeletedArticles").push().setValue(article).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            articles.remove(article);
                            articleAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "Request to delete !!!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

                @Override
                public void onPublishClick(final Article article) {
                    AppController.getInstance().getDatabase().getReference().child("pendingArticles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()
                                    ) {
                                Article iterationArticle = snapshot.getValue(Article.class);
                                if (iterationArticle.getIdentifier() == article.getIdentifier()) {

                                    System.out.println(article.getIdentifier());
                                    AppController.getInstance().getDatabase().getReference().child("pendingArticles").child(snapshot.getKey()).setValue(null);
                                    AppController.getInstance().getDatabase().getReference().child("articles").push().setValue(article);
                                    articles.remove(article);
                                    articleAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getMessage());
                        }
                    });

                    Toast.makeText(getActivity(), "Publish !!!!", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onDeleteClick(final Article article) {
                    AppController.getInstance().getDatabase().getReference().child("pendingArticles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (final DataSnapshot snapshot : dataSnapshot.getChildren()
                                    ) {
                                Article iterationArticle = snapshot.getValue(Article.class);
                                if (iterationArticle.getIdentifier() == article.getIdentifier()) {
                                    AppController.getInstance().getDatabase().getReference("pendingArticles").child(snapshot.getKey()).setValue(null);
                                    AppController.getInstance().getStorageReference().child("articles").child("article" + article.getIdentifier()).delete();
                                    articles.remove(article);
                                    articleAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "Delete !!!!", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            System.out.println(databaseError.getMessage());
                        }
                    });


                }

                @Override
                public void onRequestDelete(final Article article) {
                    System.out.println(article.getIdentifier());
                    AppController.getInstance().getDatabase().getReference().child("toBeDeletedArticles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()
                                    ) {
                                Article iterationArticle = snapshot.getValue(Article.class);
                                System.out.println("inside: " + iterationArticle.getIdentifier());
                                if (iterationArticle.getIdentifier() == article.getIdentifier()) {
                                    System.out.println("inside: " + snapshot.getKey());
                                    AppController.getInstance().getDatabase().getReference().child("toBeDeletedArticles").child(snapshot.getKey()).setValue(null);
                                    AppController.getInstance().getStorageReference().child("articles").child("article" + article.getIdentifier()).delete();

                                    articles.remove(article);
                                    articleAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    AppController.getInstance().getDatabase().getReference().child("articles").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()
                                    ) {
                                Article iterationArticle = snapshot.getValue(Article.class);
                                if (iterationArticle.getIdentifier() == article.getIdentifier()) {
                                    AppController.getInstance().getDatabase().getReference().child("articles").child(snapshot.getKey()).setValue(null);
                                    AppController.getInstance().getStorageReference().child("articles").child("article" + article.getIdentifier()).delete();
                                    articles.remove(article);
                                    articleAdapter.notifyDataSetChanged();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    Toast.makeText(getActivity(), "Delete !!!!", Toast.LENGTH_SHORT).show();
                }
            };


            switch (MODE) {

                case "reader":
                    articles.clear();

                    articleAdapter = new ArticleAdapter(getActivity(), articles, itemsClickHandler, MODE);

                    AppController.getInstance().getDatabase().getReference("articles").orderByChild("identifier").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child added---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child added key---------" + dataSnapshot.getKey());
                            if (dataSnapshot.hasChild("author") && dataSnapshot.hasChild("title") && dataSnapshot.hasChild("description") && dataSnapshot.hasChild("imageLink")) {

                                //:TODO inefficient operation here...need to think how to optimize this..
                                //:TODO maybe use a new data structure

                                tempArticles.addAll(articles);
                                articles.clear();
                                articles.add(dataSnapshot.getValue(Article.class));
                                /*articles.get(articles.size() - 1).setIdentifier(Integer.parseInt(dataSnapshot.getKey().replace("article", "")));*/
                                articles.addAll(tempArticles);
                                articleSize = articles.size() + 1;
                                tempArticles.clear();
                                articleAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child changed---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child changed key---------\n" + dataSnapshot.getKey());
                            Article changedArticle = dataSnapshot.getValue(Article.class);
                            /*changedArticle.setIdentifier(Integer.parseInt(dataSnapshot.getKey().replace("article", "")));*/

                            int replaceIndex = 0;
                            for (Article iterationArticle : articles) {
                                if (iterationArticle.getIdentifier() == (changedArticle.getIdentifier())) {
                                    /*iterationArticle.setIdentifier(changedArticle.getIdentifier());*/
                                    articles.set(replaceIndex, changedArticle);
                                    break;
                                }
                                replaceIndex++;
                            }
                            articleAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            System.out.println("\n----child removed---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child removed key---------\n" + dataSnapshot.getKey());
                            for (Article iterationArticle : articles) {
                                if (iterationArticle.getIdentifier() == dataSnapshot.child("identifier").getValue(Integer.class)) {
                                    articles.remove(iterationArticle);
                                    break;
                                }
                            }
                            articleAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child moved---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child moved---------\n" + dataSnapshot.getKey());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("DB ERROR", databaseError.toException().toString());
                        }
                    });
                    break;
                case "writer":
                    articles.clear();
                    Query query = AppController.getInstance().getDatabase().getReference("articles").orderByChild("author").equalTo(AppController.getInstance().getUser().getName());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                articles.add(dataSnapshot.getValue(Article.class));
                            }
                            articleAdapter = new ArticleAdapter(getActivity(), articles, itemsClickHandler, MODE);

                            articleRecyclerView.setAdapter(articleAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        }
                    });
                    break;
                case "editor-publish":
                    articles.clear();

                    articleAdapter = new ArticleAdapter(getActivity(), articles, itemsClickHandler, MODE);

                    AppController.getInstance().getDatabase().getReference("pendingArticles").orderByChild("identifier").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child added---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child added key---------" + dataSnapshot.getKey());
                            if (dataSnapshot.hasChild("author") && dataSnapshot.hasChild("title") && dataSnapshot.hasChild("description") && dataSnapshot.hasChild("imageLink")) {

                                //:TODO inefficient operation here...need to think how to optimize this..
                                //:TODO maybe use a new data structure

                                tempArticles.addAll(articles);
                                articles.clear();
                                articles.add(dataSnapshot.getValue(Article.class));
                           /*     articles.get(articles.size() - 1).setIdentifier(Integer.parseInt(dataSnapshot.getKey().replace("article", "")));*/
                                articles.addAll(tempArticles);
                                articleSize = articles.size() + 1;
                                tempArticles.clear();
                                articleAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child changed---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child changed key---------\n" + dataSnapshot.getKey());
                            Article changedArticle = dataSnapshot.getValue(Article.class);
                            /*changedArticle.setIdentifier(Integer.parseInt(dataSnapshot.getKey().replace("article", "")));*/

                            int replaceIndex = 0;
                            for (Article iterationArticle : articles) {
                                if (iterationArticle.getIdentifier() == (changedArticle.getIdentifier())) {
                                 /*   iterationArticle.setIdentifier(changedArticle.getIdentifier());*/
                                    articles.set(replaceIndex, changedArticle);
                                    break;
                                }
                                replaceIndex++;
                            }
                            articleAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            System.out.println("\n----child removed---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child removed key---------\n" + dataSnapshot.getKey());
                            for (Article iterationArticle : articles) {
                                if (iterationArticle.getIdentifier() == dataSnapshot.child("identifier").getValue(Integer.class)) {
                                    articles.remove(iterationArticle);
                                    break;
                                }
                            }

                            articleAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child moved---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child moved---------\n" + dataSnapshot.getKey());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("DB ERROR", databaseError.toException().toString());
                        }
                    });
                    break;
                case "editor-delete":
                    articles.clear();

                    articleAdapter = new ArticleAdapter(getActivity(), articles, itemsClickHandler, MODE);

                    AppController.getInstance().getDatabase().getReference("toBeDeletedArticles").orderByChild("identifier").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child added---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child added key---------" + dataSnapshot.getKey());
                            if (dataSnapshot.hasChild("author") && dataSnapshot.hasChild("title") && dataSnapshot.hasChild("description") && dataSnapshot.hasChild("imageLink")) {

                                //:TODO inefficient operation here...need to think how to optimize this..
                                //:TODO maybe use a new data structure

                                tempArticles.addAll(articles);
                                articles.clear();
                                articles.add(dataSnapshot.getValue(Article.class));
                           /*     articles.get(articles.size() - 1).setIdentifier(Integer.parseInt(dataSnapshot.getKey().replace("article", "")));*/
                                articles.addAll(tempArticles);
                                articleSize = articles.size() + 1;
                                tempArticles.clear();
                                articleAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child changed---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child changed key---------\n" + dataSnapshot.getKey());
                            Article changedArticle = dataSnapshot.getValue(Article.class);
                            /*changedArticle.setIdentifier(Integer.parseInt(dataSnapshot.getKey().replace("article", "")));*/

                            int replaceIndex = 0;
                            for (Article iterationArticle : articles) {
                                if (iterationArticle.getIdentifier() == (changedArticle.getIdentifier())) {
                                 /*   iterationArticle.setIdentifier(changedArticle.getIdentifier());*/
                                    articles.set(replaceIndex, changedArticle);
                                    break;
                                }
                                replaceIndex++;
                            }
                            articleAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {
                            System.out.println("\n----child removed---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child removed key---------\n" + dataSnapshot.getKey());
                            for (Article iterationArticle : articles) {
                                if (iterationArticle.getIdentifier() == dataSnapshot.child("identifier").getValue(Integer.class)) {
                                    articles.remove(iterationArticle);
                                    break;
                                }
                            }

                            articleAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                            System.out.println("\n----child moved---------\n" + dataSnapshot.toString());
                            System.out.println("\n----child moved---------\n" + dataSnapshot.getKey());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("DB ERROR", databaseError.toException().toString());
                        }
                    });
                    break;
            }

            articleRecyclerView.setAdapter(articleAdapter);
            AppController.getInstance().getDatabase().getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    categories.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        categories.add(snapshot.getValue(String.class));
                    }
                    filterView = getActivity().getLayoutInflater().inflate(R.layout.filter_dialog, null);
                    final ListView categoryListView = (ListView) filterView.findViewById(R.id.category_list);
                    categoryListView.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_multiple_choice, categories));
                    categoryListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

                    filterDialog = new AlertDialog.Builder(getContext()).setView(filterView).setNegativeButton("Clear Filters", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            for (int j = 0; j < categoryListView.getCount(); j++) {
                                categoryListView.setItemChecked(j, false);
                            }

                            articleAdapter = new ArticleAdapter(getActivity(), articles, itemsClickHandler, MODE);
                            articleRecyclerView.setAdapter(articleAdapter);
                        }
                    }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int ii) {
                            ListView categoryListView = (ListView) filterView.findViewById(R.id.category_list);
                            selectedCategories.clear();
                            int len = categoryListView.getCount();
                            SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                            for (int i = 0; i < len; i++)
                                if (checked.get(i)) {
                                    String item = categories.get(i);
  /* do whatever you want with the checked item */
                                    selectedCategories.add(item);
                                }
                            System.out.println(selectedCategories.toString());
                            filterDialog.cancel();
                            if (selectedCategories.size() > 0) {
                                filteredArticles.clear();
                                for (int i = 0; i < articles.size(); i++) {
                                    System.out.println(articles.get(i).getCategory());
                                    if (selectedCategories.contains(articles.get(i).getCategory())) {
                                        filteredArticles.add(articles.get(i));
                                    }
                                }

                                articleAdapter = new ArticleAdapter(getActivity(), filteredArticles, itemsClickHandler, MODE);
                                articleRecyclerView.setAdapter(articleAdapter);
                            } else {
                                articleAdapter = new ArticleAdapter(getActivity(), articles, itemsClickHandler, MODE);
                                articleRecyclerView.setAdapter(articleAdapter);
                            }

                        }
                    }).create();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("DB ERROR", databaseError.toException().toString());
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
            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (AppController.getInstance().isLoggedIn()) {
            menu.add("Filter").setIcon(android.R.drawable.ic_menu_preferences).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    filterDialog.show();
                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
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
