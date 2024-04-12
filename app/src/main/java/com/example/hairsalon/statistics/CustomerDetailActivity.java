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

        // Nhận thông tin khách hàng từ Intent
        Intent intent = this.getIntent();
        Customer customer = (Customer) intent.getSerializableExtra("customer");
        // Hiển thị thông tin khách hàng trong giao diện
        TextView nameTextView = findViewById(R.id.text_name);
        TextView ageTextView = findViewById(R.id.text_age);
        TextView idTextView = findViewById(R.id.text_id);
        TextView genderTextView = findViewById(R.id.text_gender);
        TextView addressTextView = findViewById(R.id.text_address);
        TextView emailTextView = findViewById(R.id.text_email);
        TextView phoneTextView = findViewById(R.id.text_phone);

        // Thiết lập giá trị cho các TextView
        nameTextView.setText("Name: " + customer.getName());
        ageTextView.setText("Age: " + customer.getAge());
        idTextView.setText("ID: " + customer.getId());
        genderTextView.setText("Gender: " + (customer.isGender() ? "Male" : "Female"));
        addressTextView.setText("Address: " + customer.getAddress());
        emailTextView.setText("Email: " + customer.getEmail());
        phoneTextView.setText("Phone: " + customer.getPhone());

    }
}