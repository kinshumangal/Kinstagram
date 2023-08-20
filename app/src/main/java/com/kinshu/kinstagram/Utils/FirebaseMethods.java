package com.kinshu.kinstagram.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kinshu.kinstagram.Models.User;
import com.kinshu.kinstagram.Models.UserAccountSettings;
import com.kinshu.kinstagram.R;


public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private Context context;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userID;


    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        this.context = context;
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUserNameExists(String username, DataSnapshot dataSnapshot){
        User user = new User();

        for (DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
            Log.d(TAG, "datasnapshot: "+ ds);

            user.setUsername(ds.getValue(User.class).getUsername());
            Log.d(TAG, "datasnapshot: "+ user.getUsername());
            if (StringManipulation.expendUsername(user.getUsername()).equals(username)){
                Log.d(TAG, "Found a match: "+user.getUsername());
                return true;
            }
        }

        return false;
    }

    /**
     * Register a new email and password to Firebase Authentication
     * @param email
     * @param password
     * @param userName
     */
    public void registerNewEmail(final String email, String password, final String userName){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(context, "Failed to Authenticate...", Toast.LENGTH_SHORT).show();

                        }
                        else if (task.isSuccessful()){
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, userID);
                            sendVerificationEmail();

//                            Toast.makeText(context, "Authentication Successful...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void sendVerificationEmail(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                            }else {
                                Toast.makeText(context, "couldn't send verification email.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    /**
     * Add information to the users node
     * Add information to the user_account_settings node
     * @param email
     * @param userName
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String userName, String description, String website, String profile_photo){
        User user = new User(userID, 1, email, StringManipulation.condenseUsername(userName));
        myRef.child(context.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description, userName, 0, 0 , 0,
                profile_photo, userName, website
        );

        myRef.child(context.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }




}
