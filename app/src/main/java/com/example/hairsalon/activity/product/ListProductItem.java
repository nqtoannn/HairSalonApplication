package com.example.hairsalon.activity.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.servicehair.AddServiceHairActivity;
import com.example.hairsalon.activity.servicehair.ListServiceHairActivity;
import com.example.hairsalon.adapter.ListProductItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityListProductItemBinding;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductItem extends AppCompatActivity {

    ActivityListProductItemBinding binding;

    ArrayList<ProductItem> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> productItemList = new ArrayList<>();

    ListProductItemAdapter productItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Gọi hàm để lấy dữ liệu sản phẩm từ API
        getAllProductItem();

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListProductItem.this, AddProductItemActivity.class);
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
                        // Xóa dữ liệu trước đó trong dataArrayList
                        dataArrayList.clear();
                        productItemList = responseData.getData();
                        for (Map<String, Object> productItem : productItemList) {
                            Integer id = ((Number) productItem.get("id")).intValue();
                            String productItemName = (String) productItem.get("productItemName");
                            double price = (double) productItem.get("price");
                            Integer quantityInStock = ((Number) Objects.requireNonNull(productItem.get("quantityInStock"))).intValue();
                            String imageUrl = (String) productItem.get("imageUrl");
                            Integer warrantyTime = ((Number) Objects.requireNonNull(productItem.get("warrantyTime"))).intValue();
                            String status = (String) productItem.get("status");
                            String description = (String) productItem.get("description");
                            ProductItem productItemAdded = new ProductItem(id, productItemName, price, quantityInStock, warrantyTime, status, imageUrl, description);
                            dataArrayList.add(productItemAdded);
                        }
                        productItemAdapter = new ListProductItemAdapter(ListProductItem.this, dataArrayList);
                        Log.i("productItemList", dataArrayList.toString());
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

    @Override
    protected void onResume() {
        getAllProductItem();
        super.onResume();
    }
}
