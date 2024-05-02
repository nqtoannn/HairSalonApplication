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
import com.example.hairsalon.activity.manage.CustomerManageFragment;
import com.example.hairsalon.activity.manage.EmployeeManageFragment;
import com.example.hairsalon.activity.manage.RevenueFragment;
import com.example.hairsalon.activity.manage.ServiceManageFragment;
import com.example.hairsalon.activity.manage.ShopManageFragment;
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
                    loadFragment(new ShopManageFragment(),false);
                } else if (itemID == R.id.navServiceMn) {
                    loadFragment(new ServiceManageFragment(), false);
                } else if (itemID == R.id.navStaffMn) {
                    loadFragment(new EmployeeManageFragment(), false);
//                } else if (itemID == R.id.navRevenueMn) {
//                    loadFragment(new RevenueFragment(), false);
                } else if (itemID == R.id.navCustomerMn) {
                    loadFragment(new CustomerManageFragment(), false);
                } else {
                    loadFragment(new AccountFragment(), false);
                }
                return true;
            }
        });
        loadFragment(new ShopManageFragment(), true);
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