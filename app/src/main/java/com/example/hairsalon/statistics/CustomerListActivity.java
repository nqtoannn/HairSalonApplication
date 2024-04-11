package com.example.hairsalon.statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.hairsalon.R;
import com.example.hairsalon.adapter.CustomerAdapter;
import com.example.hairsalon.model.Customer;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        ListView customerListView = findViewById(R.id.list_customer);
        // Tạo danh sách khách hàng
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer("John Doe", 25, "051102930594", true, "HCM", "john.doe@example.com", "123-456-7890"));
        customers.add(new Customer("Jane Smith", 26, "014832804", false, "QN", "jane.smith@example.com", "987-654-3210"));
        customers.add(new Customer("David Nguyen", 30, "9212404528304", true, "HN", "david.nguyen@example.com", "555-123-4567"));

        // Tạo adapter và thiết lập cho ListView
        CustomerAdapter adapter = new CustomerAdapter(this, (ArrayList<Customer>) customers);
        customerListView.setAdapter(adapter);
    }
}