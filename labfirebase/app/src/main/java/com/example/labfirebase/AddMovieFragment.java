package com.example.labfirebase;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddMovieFragment extends Fragment {
    private DatabaseReference mDatabaseReference;
    private TextInputEditText movieName;
    private TextInputEditText movieLogo;
    private RatingBar mRatingBar;
    private Button bSubmit;
    View v;
    private StorageReference mStorageRef;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_add_movie,container,false);
        movieName = (TextInputEditText) v.findViewById(R.id.tiet_movie_name);
        movieLogo = (TextInputEditText) v.findViewById(R.id.tiet_movie_logo);
        bSubmit = (Button) v.findViewById(R.id.b_submit);
        mRatingBar = (RatingBar) v.findViewById(R.id.rating_bar);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        bSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEmpty(movieName) && !isEmpty(movieName)){
                    myNewMovie("53", movieName.getText().toString().trim(),movieLogo.getText().toString(),mRatingBar.getRating());
                }else{
                    if(isEmpty(movieName)){
                        Toast.makeText(getContext(), "Please enter a movie name!", Toast.LENGTH_SHORT).show();
                    }else if(isEmpty(movieLogo)){
                        Toast.makeText(getContext(), "Please specify a url for the logo", Toast.LENGTH_SHORT).show();
                    }
                }
                //to remove current fragment
                getActivity().onBackPressed();
            }
        });
        return v;

    }


    private void myNewMovie(String userId, String movieName, String moviePoster, float rating) {
        //Creating a movie object with user defined variables
        Movie movie = new Movie(UUID.randomUUID().toString(),movieName,moviePoster,rating);
        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("users").child(userId).child("movies").child(movie.getId()).setValue(movie);
    }
    //check if edittext is empty
    private boolean isEmpty(TextInputEditText textInputEditText) {
        if (textInputEditText.getText().toString().trim().length() > 0)
            return false;
        return true;
    }

    public void subirImagen(){
        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }
}

