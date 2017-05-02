package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.Models.Article;
import com.thelegacycoder.theshorthornapp.R;

import java.util.ArrayList;

import static com.thelegacycoder.theshorthornapp.Fragments.HomeFragment.articleSize;

public class AddArticleFragment extends Fragment {
    ImageView arc_img;
    private int IMAGE_PICKER_SELECT = 999;
    private Uri uri;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner categorySpinner;

    public AddArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddArticleFragment newInstance(String param1, String param2) {
        AddArticleFragment fragment = new AddArticleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText arc_title = (EditText) view.findViewById(R.id.articleTitle);
        arc_img = (ImageView) view.findViewById(R.id.articleImage);
        final EditText arc_body = (EditText) view.findViewById(R.id.articleBody);
        Button btn_publish = (Button) view.findViewById(R.id.Publish);
        categorySpinner = (Spinner) view.findViewById(R.id.category_list);

        arc_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, IMAGE_PICKER_SELECT);
                showFileChooser();
                Toast.makeText(getActivity(), "Image Clicked", Toast.LENGTH_SHORT);

            }
        });


        AppController.getInstance().getDatabase().getReference().child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<String> categoryList = new ArrayList<String>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    categoryList.add(snapshot.getValue(String.class));
                }
                categorySpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryList));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println(databaseError.toString());
            }
        });

        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!arc_title.getText().toString().equals("") && !arc_body.getText().toString().equals("")) {
                    if (uri != null)
                        AppController.getInstance().getStorageReference().child("articles").child("article" + articleSize).putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                System.out.println("uploaded");
                                final Article newArticle = new Article(articleSize, arc_title.getText().toString(), arc_body.getText().toString(),
                                        AppController.getInstance().getUser().getName(), "article" + articleSize, categorySpinner.getSelectedItem().toString());
                                AppController.getInstance().getDatabase().getReference().child("articles").child("article" + articleSize).setValue(newArticle).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getActivity(), "Article added!", Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    }
                                });
                            }
                        });
                    else {
                        final Article newArticle = new Article(articleSize, arc_title.getText().toString(), arc_body.getText().toString(),
                                AppController.getInstance().getUser().getName(), "noImage", categorySpinner.getSelectedItem().toString());
                        AppController.getInstance().getDatabase().getReference().child("articles").child("article" + articleSize).setValue(newArticle).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(), "Article added!", Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();

                            }
                        });
                    }

                }

            }
        });
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        Intent chooser = Intent.createChooser(intent, "Choose a Picture");
        startActivityForResult(chooser, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uri = data.getData();
        Picasso.with(getActivity()).load(uri).into(arc_img);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_article, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
