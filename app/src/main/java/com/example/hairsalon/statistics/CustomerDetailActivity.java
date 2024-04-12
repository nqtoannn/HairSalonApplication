package com.example.hairsalon.statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hairsalon.R;
import com.example.hairsalon.databinding.ActivityCustomerDetailBinding;
import com.example.hairsalon.databinding.ActivityDetailedBinding;
import com.example.hairsalon.model.Customer;

public class CustomerDetailActivity extends AppCompatActivity {

    ActivityCustomerDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Kiểm tra Intent extras
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("customer")) {
            // Nhận thông tin khách hàng từ Intent
            Customer customer = (Customer) intent.getSerializableExtra("customer");
            // Hiển thị thông tin khách hàng trong giao diện
            binding.textName.setText("Name: " + customer.getName());
            binding.textAge.setText("Age: " + customer.getAge());
            binding.textId.setText("ID: " + customer.getId());
            binding.textGender.setText("Gender: " + (customer.isGender() ? "Male" : "Female"));
            binding.textAddress.setText("Address: " + customer.getAddress());
            binding.textEmail.setText("Email: " + customer.getEmail());
            binding.textPhone.setText("Phone: " + customer.getPhone());
        } else {
            // Xử lý trường hợp không có thông tin khách hàng
            //Toast.makeText(this, "No customer information found", Toast.LENGTH_SHORT).show();
            // Hoặc chuyển hướng trở lại màn hình trước đó
            finish();
        }
    }
}
