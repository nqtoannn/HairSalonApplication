package com.example.hairsalon.activity.navbar;

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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int customerId = -1; // Set a default value if the ID is not found
        if (bundle != null && bundle.containsKey("customerId")) {
            customerId = bundle.getInt("customerId");
        }
        int finalCustomerId = customerId;
        Log.d("ID", String.valueOf(finalCustomerId));
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemID = item.getItemId();
                if( itemID == R.id.navHome){
                    loadFragment(new HomeFragment(),false, finalCustomerId);
                } else if (itemID == R.id.navShop) {
                    loadFragment(new ShopFragment(), false, finalCustomerId);
                } else if (itemID == R.id.navBooking) {
                    loadFragment(new BookingFragment(), false, finalCustomerId);
                } else if (itemID == R.id.navExplore) {
                    loadFragment(new ExploreFragment(), false, finalCustomerId);
                } else {
                    loadFragment(new AccountFragment(), false, finalCustomerId);
                }
                return true;
            }
        });
        loadFragment(new HomeFragment(), true, finalCustomerId);
    }

    private void loadFragment(Fragment fragment, boolean isAppInitialized, int customerId) {
        Intent intent = new Intent();
        intent.putExtra("customerId", customerId);
        fragment.setArguments(new Bundle(intent.getExtras()));
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