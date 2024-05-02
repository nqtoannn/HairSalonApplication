package com.example.hairsalon.activity.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.hairsalon.R;
import com.example.hairsalon.databinding.ActivityOrderDetailBinding;
import com.example.hairsalon.model.User;

public class OrderDetail extends AppCompatActivity {

    ActivityOrderDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            String nameProduct = intent.getStringExtra("nameProduct");
            double price = intent.getDoubleExtra("price", 0.0);
            int quantity = intent.getIntExtra("quantity", 0);
            String address = intent.getStringExtra("address");
            String imageUrl = intent.getStringExtra("imageUrl");

        }



    }
}