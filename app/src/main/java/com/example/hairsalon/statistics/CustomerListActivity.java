package com.example.hairsalon.statistics;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.hairsalon.R;
import com.example.hairsalon.adapter.CustomerAdapter;
import com.example.hairsalon.databinding.ActivityCustomerDetailBinding;
import com.example.hairsalon.databinding.ActivityDetailedBinding;
import com.example.hairsalon.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {
    ActivityCustomerDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ListView customerListView = findViewById(R.id.list_customer);
        // Tạo danh sách khách hàng
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John Doe", 25, "051102930594", true, "HCM", "john.doe@example.com", "123-456-7890"));
        customers.add(new Customer("Jane Smith", 26, "014832804", false, "QN", "jane.smith@example.com", "987-654-3210"));
        customers.add(new Customer("David Nguyen", 30, "9212404528304", true, "HN", "david.nguyen@example.com", "555-123-4567"));
        // Tạo adapter và thiết lập cho ListView
        CustomerAdapter adapter = new CustomerAdapter(this, (ArrayList<Customer>) customers);
        customerListView.setAdapter(adapter);

        //Xử lý khi nhấp vào một khách hàng
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer selectedCustomer = customers.get(position);
                Log.i("clicked", selectedCustomer.toString());
                // Tạo Intent để chuyển sang màn hình thông tin chi tiết
                Intent intent = new Intent(CustomerListActivity.this, CustomerDetailActivity.class);
                // Truyền thông tin của khách hàng qua Intent
                intent.putExtra("customer", selectedCustomer);
                startActivity(intent); // Chuyển sang màn hình thông tin chi tiết
            }
        });
    }
}