package com.example.hairsalon.activity.manage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.hairsalon.activity.product.AddProductItemActivity;
import com.example.hairsalon.adapter.ListProductItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentShopMnBinding;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopManageFragment extends Fragment {
    private FragmentShopMnBinding binding;

    private ArrayList<ProductItem> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> productItemList = new ArrayList<>();

    private ListProductItemAdapter productItemAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentShopMnBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call method to fetch product items
        getAllProductItem();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), AddProductItemActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllProductItem() {
        ApiService.apiService.getProductItem().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        // Clear previous data
                        dataArrayList.clear();

                        productItemList = responseData.getData();
                        for (Map<String, Object> productItem : productItemList) {
                            String id = (String) productItem.get("id");
                            String productItemName = (String) productItem.get("productItemName");
                            double price = (double) productItem.get("price");
                            Integer quantityInStock = ((Number) Objects.requireNonNull(productItem.get("quantity"))).intValue();
                            String imageUrl = (String) productItem.get("imageUrl");
                            Integer warrantyTime = ((Number) Objects.requireNonNull(productItem.get("warrantyTime"))).intValue();
                            String status = (String) productItem.get("status");
                            String description = (String) productItem.get("description");
                            ProductItem productItemAdded = new ProductItem(id, productItemName, price, quantityInStock, warrantyTime, status, imageUrl, description);
                            dataArrayList.add(productItemAdded);
                        }
                        productItemAdapter = new ListProductItemAdapter(requireActivity(), dataArrayList);
                        binding.gridView.setAdapter(productItemAdapter);
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
    }
}