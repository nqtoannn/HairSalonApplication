package com.example.hairsalon.activity.product;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hairsalon.R;
import com.example.hairsalon.databinding.ActivityDetailedBinding;


public class DetailedActivity extends AppCompatActivity {

    ActivityDetailedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Xử lý sự kiện khi người dùng click vào nút quay lại ở đây
                // Ví dụ: quay lại màn hình trước đó
                finish(); // Hoặc gọi finish() nếu muốn kết thúc Activity
            }
        });

        Intent intent = this.getIntent();
        if (intent != null){
            String name = intent.getStringExtra("detailName");
            String price = intent.getStringExtra("detailPrice");
            String description = intent.getStringExtra("detailDescription");
            String quantityInStock = intent.getStringExtra("quantityInStock");

            binding.detailName.setText(name);
            binding.detailDescription.setText(description);
            binding.detailPrice.setText(price);
            binding.quantityInStock.setText(quantityInStock);
        }
    }
}