package com.example.hairsalon.statistics;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;


import com.example.hairsalon.activity.statistics.CustomerDetailActivity;
import com.example.hairsalon.adapter.UserAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityCustomerListBinding;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerListActivity extends AppCompatActivity {
    ActivityCustomerListBinding binding;
    UserAdapter userAdapter;
    ArrayList<User> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomerListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ApiService.apiService.getAllCustomner().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    // Lấy danh sách khách hàng từ kết quả trả về
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        userList = responseData.getData();// Lấy danh sách khách hàng từ ResponseData
                        for (Map<String, Object> user : userList) {
                            String id = ((String) user.get("id"));
                            String userName = (String) user.get("userName");
                            String email = (String) user.get("email");
                            String password = (String) user.get("password");
                            String role = (String) user.get("role");
                            String fullName = (String) user.get("fullname");
                            String phoneNumber = (String) user.get("phoneNumber");
                            String address = (String) user.get("address");
                            String status = (String) user.get("status");
                            User userAdded = new User( id, userName, email, password, role,fullName,phoneNumber,address,status);
                            dataArrayList.add(userAdded);
                        }
                    }
                    userAdapter = new UserAdapter(CustomerListActivity.this, dataArrayList);
                    Log.e("userList", dataArrayList.toString());
                    binding.listCustomer.setAdapter(userAdapter);
                } else {
                    // Xử lý lỗi khi gọi API không thành công
                    Log.e("API Error", "Failed to fetch customers: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
                Log.e("API Error", "Failed to fetch customers: " + t.getMessage());
                t.printStackTrace();
            }
        });

        //Xử lý khi nhấp vào một khách hàng
        binding.listCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User selectedUser = dataArrayList.get(position);
                Log.i("clicked", selectedUser.toString());
                // Tạo Intent để chuyển sang màn hình thông tin chi tiết
                Intent intent = new Intent(CustomerListActivity.this, CustomerDetailActivity.class);
                // Truyền thông tin của khách hàng qua Intent
                //intent.putExtra("user", selectedUser);
                startActivity(intent); // Chuyển sang màn hình thông tin chi tiết
            }
        });
    }
}