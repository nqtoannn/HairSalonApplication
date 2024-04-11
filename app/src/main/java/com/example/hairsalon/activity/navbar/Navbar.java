package com.example.hairsalon.activity.navbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.home.AccountFragment;
import com.example.hairsalon.activity.home.BookingFragment;
import com.example.hairsalon.activity.home.ExploreFragment;
import com.example.hairsalon.activity.home.HomeFragment;
import com.example.hairsalon.activity.home.ShopFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Navbar extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navbar);
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        frameLayout = findViewById(R.id.frameLayout);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if( itemID == R.id.navHome){
                    loadFragment(new HomeFragment(),false);
                } else if (itemID == R.id.navShop) {
                    loadFragment(new ShopFragment(), false);
                } else if (itemID == R.id.navBooking) {
                    loadFragment(new BookingFragment(), false);
                } else if (itemID == R.id.navExplore) {
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
            fragmentTransaction.add(R.id.frameLayout,fragment);
        }
        else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }
}