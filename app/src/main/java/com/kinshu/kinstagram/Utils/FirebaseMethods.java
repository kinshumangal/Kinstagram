package com.kinshu.kinstagram.Utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kinshu.kinstagram.Home.HomeActivity;
import com.kinshu.kinstagram.Models.Photo;
import com.kinshu.kinstagram.Models.User;
import com.kinshu.kinstagram.Models.UserAccountSettings;
import com.kinshu.kinstagram.Models.UserSettings;
import com.kinshu.kinstagram.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import io.grpc.internal.LogExceptionRunnable;


public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    private Context context;

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private StorageReference mStorageReference;
    private DatabaseReference myRef;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userID;
    private double mPhotoUploadProgress = 0;



    public FirebaseMethods(Context context){
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mStorageReference = FirebaseStorage.getInstance().getReference();
        this.context = context;
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

//    public void uploadNewPhoto(String photoType, String caption, int count, String imgUrl){
//        Log.d(TAG, "uploadNewPhoto: attempt to upload a photo");
//        FilePaths filePaths = new FilePaths();
//        //case1) new photo
//        if(photoType.equals(context.getString(R.string.new_photo))){
//            Log.d(TAG, "uploadNewPhoto: uploading NEW photo.");
//
//            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
//            StorageReference storageReference = mStorageReference
//                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1));
//
//            //convert image url to bitmap
//            Bitmap bm = ImageManager.getBitmap(imgUrl);
//            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);
//
//            UploadTask uploadTask = null;
//            uploadTask = storageReference.putBytes(bytes);
//
//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    Uri firebaseUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();
//
//                    Toast.makeText(context, "photo upload success", Toast.LENGTH_SHORT).show();
//
//                    //add the new photo to 'photos' node and 'user_photos' node
//                    addPhotoToDatabase(caption, String.valueOf(firebaseUrl));
//
//                    //navigate to the main feed so the user can see their photo
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d(TAG, "onFailure: Photo upload failed.");
//                    Toast.makeText(context, "Photo upload failed ", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
//
//                    if(progress - 15 > mPhotoUploadProgress){
//                        Toast.makeText(context, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
//                        mPhotoUploadProgress = progress;
//                    }
//
//                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
//
//
//                }
//            });
//
//        }





    public void uploadNewPhoto(String photoType, final String caption,final int count, final String imgUrl){
        Log.d(TAG, "uploadNewPhoto: attempting to uplaod new photo.");

        FilePaths filePaths = new FilePaths();
        //case1) new photo
        if(photoType.equals(context.getString(R.string.new_photo))){
            Log.d(TAG, "uploadNewPhoto: uploading NEW photo.");

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/photo" + (count + 1));

            //convert image url to bitmap
            Bitmap bm = ImageManager.getBitmap(imgUrl);
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();

                    Toast.makeText(context, "photo upload success", Toast.LENGTH_SHORT).show();

                    //add the new photo to 'photos' node and 'user_photos' node
                    addPhotoToDatabase(caption, firebaseUrl.toString());

                    //navigate to the main feed so the user can see their photo
                    Intent intent = new Intent(context, HomeActivity.class);
                    context.startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Photo upload failed.");
                    Toast.makeText(context, "Photo upload failed ", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if(progress - 15 > mPhotoUploadProgress){
                        Toast.makeText(context, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }

                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                }
            });

        }
        //case new profile photo
        else if(photoType.equals(context.getString(R.string.profile_photo))){
            Log.d(TAG, "uploadNewPhoto: uploading new PROFILE photo");

            String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = mStorageReference
                    .child(filePaths.FIREBASE_IMAGE_STORAGE + "/" + user_id + "/profile_photo");

            //convert image url to bitmap
            Bitmap bm = ImageManager.getBitmap(imgUrl);
            byte[] bytes = ImageManager.getBytesFromBitmap(bm, 100);

            UploadTask uploadTask = null;
            uploadTask = storageReference.putBytes(bytes);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri firebaseUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().getResult();

                    Toast.makeText(context, "photo upload success", Toast.LENGTH_SHORT).show();

                    //insert into 'user_account_settings' node
                    setProfilePhoto(firebaseUrl.toString());


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure: Photo upload failed.");
                    Toast.makeText(context, "Photo upload failed ", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    if (progress - 15 > mPhotoUploadProgress) {
                        Toast.makeText(context, "photo upload progress: " + String.format("%.0f", progress) + "%", Toast.LENGTH_SHORT).show();
                        mPhotoUploadProgress = progress;
                    }

                    Log.d(TAG, "onProgress: upload progress: " + progress + "% done");
                }
            });
        }

    }
    private void setProfilePhoto(String url){
        Log.d(TAG, "setProfilePhoto: setting new profile image: " + url);

        myRef.child(context.getString(R.string.dbname_user_account_settings))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(context.getString(R.string.profile_photo))
                .setValue(url);
    }


    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }

    public void addPhotoToDatabase(String caption, String url){
        Log.d(TAG, "addPhotoToDatabase: Adding photo to databaase");
        String tags = StringManipulation.getTags(caption);
        String newPhotoKey = myRef.child(context.getString(R.string.dbname_photos)).push().getKey();
        Photo photo = new Photo();
        photo.setCaption(caption);
        photo.setDate_created(getTimestamp());
        photo.setImage_path(url);
        photo.setTags(tags);
        photo.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        photo.setPhoto_id(newPhotoKey);

        //insert into database
        myRef.child(context.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser()
                        .getUid()).child(newPhotoKey).setValue(photo);
        myRef.child(context .getString(R.string.dbname_photos)).child(newPhotoKey).setValue(photo);

    }

    public int getImageCount(DataSnapshot dataSnapshot){
        int count = 0;
        for (DataSnapshot ds: dataSnapshot
                .child(context.getString(R.string.dbname_user_photos))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .getChildren()){

            count++;
        }
        return count;
    }

    /**
     * Update 'user_account_settings' node for the current user
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSettings(String displayName, String website, String description, long phoneNumber){

        Log.d(TAG, "updateUserAccountSettings: updating user account settings.");

        if(displayName != null){
            myRef.child(context.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(context.getString(R.string.field_display_name))
                    .setValue(displayName);
        }


        if(website != null) {
            myRef.child(context.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(context.getString(R.string.field_website))
                    .setValue(website);
        }

        if(description != null) {
            myRef.child(context.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(context.getString(R.string.field_description))
                    .setValue(description);
        }

        if(phoneNumber != 0) {
            myRef.child(context.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(context.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);

            myRef.child(context.getString(R.string.dbname_users))
                    .child(userID)
                    .child(context.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);
        }
    }



    /**
     * update the username in the user's and user_account_settings node
     * @param username
     */
    public void updateUsername(String username){

        myRef.child(context.getString(R.string.dbname_users))
                .child(userID)
                .child(context.getString(R.string.field_username))
                .setValue(username);

        myRef.child(context.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(context.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * update the email in the user's node
     * @param email
     */
    public void updateEmail(String email){
        Log.d(TAG, "updateEmail: upadting email to: " + email);

        Toast.makeText(context, "updateEmail: upadting email to: " + email, Toast.LENGTH_SHORT).show();

        myRef.child(context.getString(R.string.dbname_users))
                .child(userID)
                .child(context.getString(R.string.field_email))
                .setValue(email);

    }


//    public boolean checkIfUserNameExists(String username, DataSnapshot dataSnapshot){
//        User user = new User();
//
//        for (DataSnapshot ds: dataSnapshot.child(userID).getChildren()){
//            Log.d(TAG, "datasnapshot: "+ ds);
//
//            user.setUsername(ds.getValue(User.class).getUsername());
//            Log.d(TAG, "datasnapshot: "+ user.getUsername());
//            if (StringManipulation.expendUsername(user.getUsername()).equals(username)){
//                Log.d(TAG, "Found a match: "+user.getUsername());
//                return true;
//            }
//        }
//
//        return false;
//    }

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

    /**
     * retrieve the account settings for the user currently logged in
     * Database: user_account_settings node
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds: dataSnapshot.getChildren()){

            // user_account_settings node
            if (ds.getKey().equals(context.getString(R.string.dbname_user_account_settings))){
                Log.d(TAG, ""+ds.child(userID));

                try {
                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );

                    Log.d(TAG, ""+settings.toString());

                }catch (NullPointerException e){
                    Log.e(TAG, "NullPointerException: "+e.getMessage());
                }

            }

            Log.d(TAG, "User Node "+ getClass());
            // users node
            if (ds.getKey().equals(context.getString(R.string.dbname_users))) {
                Log.d(TAG, "" + ds.child(userID));

//                Toast.makeText(context, "Firebase method", Toast.LENGTH_SHORT).show();

                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );



                Log.d(TAG, ""+user.toString());


            }

        }

        return new UserSettings(user, settings);
    }


}
