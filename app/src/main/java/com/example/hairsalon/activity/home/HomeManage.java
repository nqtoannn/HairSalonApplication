package com.example.hairsalon.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.hairsalon.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeManage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manage);
        bottomNavigationView = findViewById(R.id.bottomNavBarMn);
        frameLayout = findViewById(R.id.frameLayoutMn);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if( itemID == R.id.navShopMn){
                    loadFragment(new HomeFragment(),false);
                } else if (itemID == R.id.navServiceMn) {
                    loadFragment(new ShopFragment(), false);
                } else if (itemID == R.id.navStaffMn) {
                    loadFragment(new BookingFragment(), false);
                } else if (itemID == R.id.navRevenueMn) {
                    loadFragment(new ExploreFragment(), false);
                } else {
                    loadFragment(new AccountFragment(), false);
                }
                return true;
            }
        });
        loadFragment(new HomeFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(isAppInitialized){
            fragmentTransaction.add(R.id.frameLayoutMn,fragment);
        }
        else {
            fragmentTransaction.replace(R.id.frameLayoutMn, fragment);
        }
        fragmentTransaction.commit();
    }
}