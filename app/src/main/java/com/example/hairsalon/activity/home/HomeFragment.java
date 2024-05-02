package com.example.hairsalon.activity.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.appointment.AppointmentHistoryActivity;
import com.example.hairsalon.activity.auth.Login;
import com.example.hairsalon.adapter.HairServiceAdapter;
import com.example.hairsalon.adapter.ProductItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityHomeShopBinding;
import com.example.hairsalon.databinding.FragmentHomeBinding;
import com.example.hairsalon.model.HairService;
import com.example.hairsalon.model.ResponseServiceData;
import com.example.hairsalon.model.ResponseData;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    List<Map<String, Object>> hairServiceList;
    RecyclerView recyclerView;
    RecyclerView recyclerViewService;
    TextView textView;


    Button btnHistory, btnBooking;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        textView = binding.txtUsername;
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer customerId = sharedPreferences.getInt("userId", -1);
        Log.d("Customer Idddd", String.valueOf(customerId));
        textView.setText(String.valueOf(customerId));
        recyclerViewService = binding.recyclerViewService;
        recyclerView = binding.recyclerViewService1;
        btnBooking = binding.btnHomeBooking;
        btnHistory = binding.btnHomeHistory;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        int spanCount = 2; // Số cột bạn muốn hiển thị
        int orientation = LinearLayoutManager.HORIZONTAL; // Hướng hiển thị
        boolean reverseLayout = false; // Không đảo ngược layout

        GridLayoutManager layoutManagerService = new GridLayoutManager(requireContext(), spanCount, RecyclerView.HORIZONTAL, reverseLayout);
        recyclerViewService.setLayoutManager(layoutManagerService);
        ApiService.apiService.getAllHairService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        hairServiceList = responseData.getData();
                        hairServiceList = responseData.getData();
                        HairServiceAdapter adapter = new HairServiceAdapter(hairServiceList);
                        recyclerView.setAdapter(adapter);
                    } else {
                        Log.e("Error", "No product data found in response");
                    }
                } else {
                    Log.e("Error", "API call failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
        ApiService.apiService.getAllHairService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        hairServiceList = responseData.getData();
                        HairServiceAdapter adapter = new HairServiceAdapter(hairServiceList); // Tạo adapter mới với danh sách dịch vụ tóc
                        recyclerViewService.setAdapter(adapter); // Đặt adapter cho RecyclerView
                    } else {
                        Log.e("Error", "No hair service data found in response"); // Hiển thị thông báo nếu không có dữ liệu dịch vụ tóc
                    }
                } else {
                    Log.e("Error", "API service call failed with error code: " + response.code()); // Hiển thị thông báo nếu cuộc gọi API không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage()); // Hiển thị thông báo nếu cuộc gọi API thất bại
            }
        });
        setControl(view);
        setEvent();
        return view;

    }



    private void setEvent() {

    }

    private void setControl(View view) {
        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, new BookingFragment());
                fragmentTransaction.addToBackStack(null); // Thêm Fragment hiện tại vào back stack
                fragmentTransaction.commit();}
        });
        binding.btnHomeHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), AppointmentHistoryActivity.class);
                startActivity(intent);
            }
        });

    }
}
