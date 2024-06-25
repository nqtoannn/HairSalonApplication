package com.example.hairsalon.activity.employee;

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

public class HomeEmployee extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_employee);
        bottomNavigationView = findViewById(R.id.bottomNavBarEmployee);
        frameLayout = findViewById(R.id.frameLayoutEmployee);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if( itemID == R.id.navScheduleEm){
                    loadFragment(new BookingListFragment(),false);
                } else if (itemID == R.id.navHistoryEm) {
                    loadFragment(new BookingHistoryFragment(), false);
                } else {
                    loadFragment(new AccountEmployeeFragment(), false);
                }
                return true;
            }
        });
        loadFragment(new BookingListFragment(), true);
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
            fragmentTransaction.add(R.id.frameLayoutEmployee,fragment);
        }
        else {
            fragmentTransaction.replace(R.id.frameLayoutEmployee, fragment);
        }
        fragmentTransaction.commit();
    }
}