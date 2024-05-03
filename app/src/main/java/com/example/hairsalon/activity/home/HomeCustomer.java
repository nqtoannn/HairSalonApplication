package com.example.hairsalon.activity.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.home.AccountFragment;
import com.example.hairsalon.activity.home.BookingFragment;
import com.example.hairsalon.activity.home.ExploreFragment;
import com.example.hairsalon.activity.home.HomeFragment;
import com.example.hairsalon.activity.home.ShopFragment;
import com.example.hairsalon.activity.shop.HomeShopActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeCustomer extends AppCompatActivity {

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

    public void updateNavbar(Fragment fragment) {
        if (fragment instanceof HomeFragment) {
            bottomNavigationView.setSelectedItemId(R.id.navHome);
            getSupportActionBar().setTitle("Home");
        } else if (fragment instanceof ShopFragment) {
            bottomNavigationView.setSelectedItemId(R.id.navShop);
            getSupportActionBar().setTitle("Shop");
        } else if (fragment instanceof BookingFragment) {
            bottomNavigationView.setSelectedItemId(R.id.navBooking);
            getSupportActionBar().setTitle("Booking");
        } else if (fragment instanceof ExploreFragment) {
            bottomNavigationView.setSelectedItemId(R.id.navExplore);
            getSupportActionBar().setTitle("Explore");
        } else if (fragment instanceof AccountFragment) {
            bottomNavigationView.setSelectedItemId(R.id.navAccount);
            getSupportActionBar().setTitle("Account");
        }
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