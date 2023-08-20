package com.kinshu.kinstagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinshu.kinstagram.Home.HomeActivity;
import com.kinshu.kinstagram.R;
import com.kinshu.kinstagram.Utils.BottomNavigationViewHelper;
import com.kinshu.kinstagram.Utils.SectionsStatePagerAdaptor;

import java.util.ArrayList;

public class AccountSettingActivity extends AppCompatActivity {
    private Context mContext;
    private SectionsStatePagerAdaptor pagerAdaptor;
    private ViewPager mViewPager;
    private static final int ACTIVITY_NUM = 4;

    private RelativeLayout relativeLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        mContext = AccountSettingActivity.this;
        mViewPager = findViewById(R.id.container);
        relativeLayout = findViewById(R.id.relLayout1);
        setupSettingsList();
        setupBottomNavigationVeiw();
        setupFragment();
        getIncomingIntent();

//           Todo: setup back arrow for navigating back to "ProfileActivity"
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getIncomingIntent(){
        Intent intent = getIntent();
        if (intent.hasExtra(getString(R.string.calling_activity))){
            setViewPager(pagerAdaptor.getFragmentNumber(getString(R.string.edit_profile_fragment)));
        }
    }

    private void setViewPager(int fragmentNumber){
        relativeLayout.setVisibility(View.GONE);
        mViewPager.setAdapter(pagerAdaptor);
        mViewPager.setCurrentItem(fragmentNumber);
    }
    private void setupFragment(){
        pagerAdaptor = new SectionsStatePagerAdaptor(getSupportFragmentManager());
        pagerAdaptor.addFragment(new EditProfileFragment(), getString(R.string.edit_profile));   //fragment 0
        pagerAdaptor.addFragment(new SignOutFragment(), getString(R.string.sign_out));          // fragment 1

    }
    private void setupSettingsList(){
        ListView listView = findViewById(R.id.lvAccountSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile));                  //fragment 0
        options.add(getString(R.string.sign_out));                      //fragment 1

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setViewPager(position);
            }
        });
        
    }
    public void setupBottomNavigationVeiw(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationVeiwBar);
        BottomNavigationViewHelper.enableNavigation(this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

}
