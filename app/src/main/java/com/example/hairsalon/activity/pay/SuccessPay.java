package com.example.hairsalon.activity.pay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.activity.product.DetailProductActivity;
import com.example.hairsalon.activity.product.ListProductActivity;
import com.example.hairsalon.databinding.ActivityPayBinding;
import com.example.hairsalon.databinding.ActivitySuccessPayBinding;

public class SuccessPay extends AppCompatActivity {

    ActivitySuccessPayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SuccessPay.this, ListProductActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}