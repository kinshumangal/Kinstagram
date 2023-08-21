package com.kinshu.kinstagram.Share;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.kinshu.kinstagram.Home.CamreaFragment;
import com.kinshu.kinstagram.Home.HomeFragment;
import com.kinshu.kinstagram.Home.MessagesFragment;
import com.kinshu.kinstagram.R;
import com.kinshu.kinstagram.Utils.BottomNavigationViewHelper;
import com.kinshu.kinstagram.Utils.Permissions;
import com.kinshu.kinstagram.Utils.SectionsPagerAdaptor;

public class ShareActivity extends AppCompatActivity {
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager viewPager;

    private static final String TAG = "ShareActivity";

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started.");

        if(checkPermissionsArray(Permissions.PERMISSIONS)){
            setupViewPager();
        }else{
            verifyPermissions(Permissions.PERMISSIONS);
        }


//        setupBottomNavigationVeiw();
//        Toast.makeText(this, "Share Activity", Toast.LENGTH_SHORT).show();
    }


//    /**
//     *      Responsible for adding 3 tabs: Camera, Home, Messages
//     */
//    public void setupViewPager(){
//        SectionsPagerAdaptor adaptor = new SectionsPagerAdaptor(getSupportFragmentManager());
//        adaptor.addFragment(new CamreaFragment());
//        adaptor.addFragment(new HomeFragment());
//        adaptor.addFragment(new MessagesFragment());
//        ViewPager viewPager = findViewById(R.id.container);
//        viewPager.setAdapter(adaptor);
//
//        TabLayout tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
//    }
    private void setupViewPager(){
        SectionsPagerAdaptor adapter =  new SectionsPagerAdaptor(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));

    }
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission is it has been verified
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }



    /**
     * BottomnavigationView setup
     */
    public void setupBottomNavigationVeiw(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationVeiwBar);
        BottomNavigationViewHelper.enableNavigation(ShareActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}