package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.alexvasilkov.foldablelayout.shading.GlanceFoldShading;
import com.thelegacycoder.theshorthornapp.Adapters.ArticleAdapter;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.Models.Article;
import com.thelegacycoder.theshorthornapp.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View listTouchInterceptor;
    private View detailsLayout;
    private UnfoldableView unfoldableView;

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

        unfoldableView = (UnfoldableView) view.findViewById(R.id.unfoldable_view);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ArrayList<Article> articles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            articles.add(new Article("title" + i, "description" + i, "author" + i, "imageLink" + i));
        }
        recyclerView.setAdapter(new ArticleAdapter(getActivity(), articles, unfoldableView, new ArticleAdapter.ClickHandler() {
            @Override
            public void onClick(View view, Article article) {

                //::TODO  idk why this is not working...try kar..
                // TODO also @link {ArticleAdapter.java} madhe onclicklistener ahe..tithe pan bagh..tithe jar child element la click event dila tar animating hote...but root view la kahich nahi hot..maybe override touchintercept thingy...also border la click kela of article la tar crash hoto...urghhhh this is so buggy ....want to try in simple ListView or challenge accept karnar and recycleview madhech karun dakhavnar ?
                //TODO janne ke liye dekhiye aaj ya kal ka din.
                System.out.println("clicked");
                openDetails(view, article);
            }
        }));


        listTouchInterceptor = view.findViewById(R.id.touch_interceptor_view);
        listTouchInterceptor.setClickable(false);

        detailsLayout = view.findViewById(R.id.details_layout);
        detailsLayout.setVisibility(View.INVISIBLE);


        Bitmap glance = BitmapFactory.decodeResource(getResources(), R.drawable.unfold_glance);
        unfoldableView.setFoldShading(new GlanceFoldShading(glance));

        unfoldableView.setOnFoldingListener(new UnfoldableView.SimpleFoldingListener() {
            @Override
            public void onUnfolding(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
                detailsLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUnfolded(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
            }

            @Override
            public void onFoldingBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(true);
            }

            @Override
            public void onFoldedBack(UnfoldableView unfoldableView) {
                listTouchInterceptor.setClickable(false);
                detailsLayout.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void openDetails(View coverView, Article article) {
        final ImageView image = (ImageView) detailsLayout.findViewById(R.id.details_image);
        final TextView title = (TextView) detailsLayout.findViewById(R.id.title);
        final TextView description = (TextView) detailsLayout.findViewById(R.id.description);
        title.setText(article.getTitle());
        description.setText(article.getDescription());
        unfoldableView.unfold(coverView, detailsLayout);
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
