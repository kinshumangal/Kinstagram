package com.kinshu.kinstagram.Share;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kinshu.kinstagram.R;
import com.kinshu.kinstagram.Utils.FirebaseMethods;
import com.kinshu.kinstagram.Utils.UniversalImageLoader;

public class NextActivity extends AppCompatActivity {

    private static final String TAG = "NextActivity";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    private String mAppend = "file:/";
    private int imageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        firebaseMethods = new FirebaseMethods(NextActivity.this);

        setupFirebaseAuth();


        ImageView backArrow = findViewById(R.id.ivBackArrow);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity.");
                finish();
            }
        });

        TextView share = findViewById(R.id.tvShare);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to the final share screen.");

                //uplaod the image to firebase



            }
        });
        setImage();

    }

    /**
     * get the image url from the incoming intent and display the chosen image
     */
    private void setImage(){

        Intent intent = getIntent();
        ImageView image = findViewById(R.id.imageShare);
        UniversalImageLoader.setImage(intent.getStringExtra(getString(R.string.string_share)), image, null, mAppend);


    }

     /*
    ------------------------------------------------------------------------
    ---------------------------Firebase setup-------------------------------
    ------------------------------------------------------------------------
     */


    /**
     * Setup firebase auth object
     */
    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();
        Log.d(TAG, "onDataChange: imageCount "+imageCount);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    //User sign in
                    Log.d("Kinshu", "onAuthStateChanged:signed in: "+user.getUid());
                }
                else {
                    //User is signout
                    Log.d("Kinshu", "onAuthStateChanged:signed out");

                }
            }
        };

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                imageCount = firebaseMethods.getImageCount(snapshot);
                Log.d(TAG, "onDataChange: imageCount "+imageCount);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
//        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}