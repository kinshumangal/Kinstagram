package com.kinshu.kinstagram.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kinshu.kinstagram.Models.User;
import com.kinshu.kinstagram.Models.UserAccountSettings;
import com.kinshu.kinstagram.Models.UserSettings;
import com.kinshu.kinstagram.R;
import com.kinshu.kinstagram.Utils.FirebaseMethods;
import com.kinshu.kinstagram.Utils.UniversalImageLoader;
import com.kinshu.kinstagram.dialogs.ConfirmPasswordDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: got the password: "+ password);

        FirebaseUser  user = FirebaseAuth.getInstance().getCurrentUser();


// Get auth credentials from the user for re-authentication. The example below shows
//         email and password credentials but there are multiple possible providers,
//         such as GoogleAuthProvider or FacebookAuthProvider.


        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        //        ///////////////////// Prompt the user to re-provide their sign-in credentials

        mAuth.fetchSignInMethodsForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                Log.d(TAG,""+task.getResult().getSignInMethods().size());
                if (task.getResult().getSignInMethods().size() == 0){
                    // email not existed
                     Log.d(TAG, "User email address updated.");
                     Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
                    //////////////////////the email is available so update it
                    mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User email address updated.");
                                        Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();




                                        /*------------problem on calling updateEmail method--------------*/

                                        firebaseMethods.updateEmail(mEmail.getText().toString());

                                        /*------------problem on calling updateEmail method--------------*/

                                    }
                                }
                            });

                }else {
                    // email existed
                    Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });



//        ///////////////////// Prompt the user to re-provide their sign-in credentials
//        mAuth.getCurrentUser().reauthenticate(credential)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            Log.d(TAG, "User re-authenticated.");
//
//                            ///////////////////////check to see if the email is not already present in the database
//
//                            mAuth.fetchSignInMethodsForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//                                    if (task.isSuccessful()){
//                                        boolean check =!task.getResult().getSignInMethods().isEmpty();
//
//                                        if (!check){
//                                                //////////////////////the email is available so update it
//                                            mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()) {
//                                                                Log.d(TAG, "User email address updated.");
//                                                                Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
////                                                                firebaseMethods.updateEmail(mEmail.getText().toString());
//                                                                }
//                                                            }
//                                                    });
//
//                                        }
//                                        else {
//                                            Toast.makeText(getActivity(),"email alredy exst",Toast.LENGTH_LONG).show();
//                                        }
//                                    }
//                                }
//                            });
//
//
////                            mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
////                                @Override
////                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
////                                    if(task.isSuccessful()){
////                                        try{
////                                            if(task.getResult().getProviders().size() == 1){
////                                                Log.d(TAG, "onComplete: that email is already in use.");
////
////                                            }
////                                            else{
////                                                Log.d(TAG, "onComplete: That email is available.");
////
////                                                //////////////////////the email is available so update it
////                                                mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
////                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
////                                                            @Override
////                                                            public void onComplete(@NonNull Task<Void> task) {
////                                                                if (task.isSuccessful()) {
////                                                                    Log.d(TAG, "User email address updated.");
////                                                                    Toast.makeText(getActivity(), "email updated", Toast.LENGTH_SHORT).show();
////                                                                    firebaseMethods.updateEmail(mEmail.getText().toString());
////                                                                }
////                                                            }
////                                                        });
////                                            }
////                                        }catch (NullPointerException e){
////                                            Log.e(TAG, "onComplete: NullPointerException: "  +e.getMessage() );
////                                        }
////                                    }
////                                }
////                            });
//
//
//
//
//
//
//
//
//                        }else{
//                            Log.d(TAG, "onComplete: re-authentication failed.");
//                        }
//
//                    }
//                });
    }





    private static final String TAG = "EditProfileFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    //EditProfile Fragment widgets
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private String userID;
    private UserSettings mUserSettings;

    private CircleImageView profilePhoto;
//    private ImageView profilePhoto;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        profilePhoto = view.findViewById(R.id.profile_photo);

        mDisplayName = (EditText) view.findViewById(R.id.display_name);
        mUsername = (EditText) view.findViewById(R.id.username);
        mWebsite = (EditText) view.findViewById(R.id.website);
        mDescription = (EditText) view.findViewById(R.id.description);
        mEmail = (EditText) view.findViewById(R.id.email);
        mPhoneNumber = (EditText) view.findViewById(R.id.phone_number);
        mChangeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        firebaseMethods = new FirebaseMethods(getActivity());

        setupFirebaseAuth();

        //back button
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        ImageView checkmark = view.findViewById(R.id.saveChanges);

        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileSettings();
            }
        });
        return view;
    }


    /**
     * Check is @param username already exists in teh database
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: Checking if  " + username + " already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists()){
                    //add the username
                    firebaseMethods.updateUsername(username);
                    Toast.makeText(getActivity(), "saved username.", Toast.LENGTH_SHORT).show();

                }
                for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                    if (singleSnapshot.exists()){
                        Log.d(TAG, "checkIfUsernameExists: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setProfileWidget(UserSettings userSettings){
//        Log.d(TAG, "setting widgets from firebase database "+userSettings.toString());

//        User user = userSettings.getUser();
        mUserSettings = userSettings;

        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), profilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));

    }

    /**
     * Retrieves the data contained in the widgets and submits it to the database
     * Before donig so it chekcs to make sure the username chosen is unqiue
     */
    private void saveProfileSettings(){
        final String displayName = mDisplayName.getText().toString();
        final String username = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());


        {//TODO: case1: if the user made change to their username
            if (!mUserSettings.getUser().getUsername().equals(username)) {
                checkIfUsernameExists(username);
            }
            //TODO: case2: if the user made change to their email
            if (!mUserSettings.getUser().getEmail().equals(email)) {
                // step 1: ReAuthenticate
                // conform password and email
                ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
                dialog.setTargetFragment(EditProfileFragment.this, 1);


                //step 2: check if the email already registered
                //          -'fatchprovideremail(String email)'

                //step 3: change email
                //          -submit the new email to the database and authentication


            }

            /**
             * change the rest of the settings that do not require uniqueness
             */
            if(!mUserSettings.getSettings().getDisplay_name().equals(displayName)){
                //update displayname
                firebaseMethods.updateUserAccountSettings(displayName, null, null, 0);
            }
            if(!mUserSettings.getSettings().getWebsite().equals(website)){
                //update website
                firebaseMethods.updateUserAccountSettings(null, website, null, 0);
            }
            if(!mUserSettings.getSettings().getDescription().equals(description)){
                //update description
                firebaseMethods.updateUserAccountSettings(null, null, description, 0);
            }
            if(mUserSettings.getSettings().getPhoneNumber() != phoneNumber){
                //update phoneNumber
                firebaseMethods.updateUserAccountSettings(null, null, null, phoneNumber);
            }
        }
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
        userID = mAuth.getCurrentUser().getUid();
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

                //retrieve user info from database
                setProfileWidget(firebaseMethods.getUserSettings(snapshot));


                //retrieve images for the user in question


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

 void fetch(){
//        mAuth.fetchSignInMethodsForEmail();
 }


}
