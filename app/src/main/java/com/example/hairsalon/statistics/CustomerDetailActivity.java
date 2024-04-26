package com.example.hairsalon.statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hairsalon.R;
import com.example.hairsalon.databinding.ActivityCustomerDetailBinding;
import com.example.hairsalon.databinding.ActivityDetailedBinding;
import com.example.hairsalon.model.Customer;
import com.example.hairsalon.model.User;

public class CustomerDetailActivity extends AppCompatActivity {

    ActivityCustomerDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Kiểm tra Intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("user")) {
            // Nhận thông tin khách hàng từ Intent
            User user = (User) intent.getSerializableExtra("user");
            // Hiển thị thông tin khách hàng trong giao diện
            binding.textName.setText("Name: " + user.getUserName());
            binding.textEmail.setText("Email: " + user.getEmail());
            binding.textRole.setText("Role: " + user.getRole());
        } else {
            // Xử lý trường hợp không có thông tin khách hàng
            //Toast.makeText(this, "No customer information found", Toast.LENGTH_SHORT).show();
            // Hoặc chuyển hướng trở lại màn hình trước đó
            finish();
        }
    }
}
