package com.example.hairsalon.activity.home;

import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hairsalon.R;
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
    List<Map<String, Object>> productItemList;
    List<HairService> hairServiceList;
    RecyclerView recyclerView;
    RecyclerView recyclerViewService;
    TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        recyclerView = binding.recyclerViewProducts;
        recyclerViewService = binding.recyclerViewService;
        textView = binding.txtUsername;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        int spanCount = 2; // Số cột bạn muốn hiển thị
        int orientation = LinearLayoutManager.HORIZONTAL; // Hướng hiển thị
        boolean reverseLayout = false; // Không đảo ngược layout

        GridLayoutManager layoutManagerService = new GridLayoutManager(requireContext(), spanCount, RecyclerView.HORIZONTAL, reverseLayout);
        recyclerViewService.setLayoutManager(layoutManagerService);
        ApiService.apiService.getProductItem().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        productItemList = responseData.getData();
                        ProductItemAdapter adapter = new ProductItemAdapter(productItemList);
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
        ApiService.apiService.getAllHairService().enqueue(new Callback<ResponseServiceData>() {
            @Override
            public void onResponse(Call<ResponseServiceData> call, Response<ResponseServiceData> response) {
                if (response.isSuccessful()) {
                    ResponseServiceData responseServiceData = response.body();
                    if (responseServiceData != null && responseServiceData.getStatus().equals("OK")) {
                        hairServiceList = responseServiceData.getData().getHairService();
                        HairServiceAdapter adapter = new HairServiceAdapter(requireContext(),hairServiceList); // Tạo adapter mới với danh sách dịch vụ tóc
                        recyclerViewService.setAdapter(adapter); // Đặt adapter cho RecyclerView
                    } else {
                        Log.e("Error", "No hair service data found in response"); // Hiển thị thông báo nếu không có dữ liệu dịch vụ tóc
                    }
                } else {
                    Log.e("Error", "API service call failed with error code: " + response.code()); // Hiển thị thông báo nếu cuộc gọi API không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseServiceData> call, Throwable t) {
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

    }
}
