package com.kinshu.kinstagram.Utils;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinshu.kinstagram.R;
import com.kinshu.kinstagram.Home.HomeActivity;
import com.kinshu.kinstagram.Likes.LikesActivity;
import com.kinshu.kinstagram.Profile.ProfileActivity;
import com.kinshu.kinstagram.Search.SearchActivity;
import com.kinshu.kinstagram.Share.ShareActivity;

public class BottomNavigationViewHelper {

    Context context;
    public static void enableNavigation(Context context, BottomNavigationView view){

        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id =  item.getItemId();


                    if (id == R.id.ic_house) {
                        Intent intent1 = new Intent(context, HomeActivity.class);//ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                    }

                    else if (id == R.id.ic_search) {
                        Intent intent2 = new Intent(context, SearchActivity.class);//ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                    }

                    else if (id == R.id.ic_circle) {
                        Intent intent3 = new Intent(context, ShareActivity.class);//ACTIVITY_NUM = 2
                        context.startActivity(intent3);
                    }

                    else if (id == R.id.ic_alert) {
                        Intent intent4 = new Intent(context, LikesActivity.class);//ACTIVITY_NUM = 3
                        context.startActivity(intent4);
                    }

                    else if (id == R.id.ic_android) {
                        Intent intent5 = new Intent(context, ProfileActivity.class);//ACTIVITY_NUM = 4
                        context.startActivity(intent5);
                    }
                return false;
            }
        });
    }

}
