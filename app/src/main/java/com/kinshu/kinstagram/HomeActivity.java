package com.kinshu.kinstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinshu.kinstagram.R;
import com.kinshu.kinstagram.Utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {

    private static final int ACTIVITY_NUM = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupBottomNavigationVeiw();

        Toast.makeText(this, "HomeActivity", Toast.LENGTH_SHORT).show();
    }


    /**
     * BottomnavigationView setup
     */
    public void setupBottomNavigationVeiw(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationVeiwBar);
        BottomNavigationViewHelper.enableNavigation(HomeActivity.this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}