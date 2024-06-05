package com.example.hairsalon.activity.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.hairsalon.adapter.GridAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityListProductBinding;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductSearchActivity extends AppCompatActivity {

    ActivityListProductBinding binding;
    GridAdapter gridAdapter;
    ArrayList<ProductItem> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> productItemList = new ArrayList<>();
    String searchQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchQuery = getIntent().getStringExtra("searchQuery");
        if (searchQuery != null) {
            performSearch(searchQuery);
        } else ApiService.apiService.getProductItem().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        productItemList = responseData.getData();
                        for (Map<String, Object> productItem : productItemList) {
                            String id = (String) productItem.get("id");
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
                        gridAdapter = new GridAdapter(ListProductSearchActivity.this, dataArrayList);
                        Log.i("productItemList", dataArrayList.toString());
                        binding.gridView.setAdapter(gridAdapter);
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

        if (dataArrayList.isEmpty()) {
            binding.emptyTextView.setVisibility(View.VISIBLE);
            binding.labelName.setText("Kết quả tìm kiếm của: " + searchQuery);
        }

        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ProductItem clickedProduct = dataArrayList.get(i);
                Log.i("clicked", clickedProduct.toString());
                Intent intent = new Intent(ListProductSearchActivity.this, DetailProductActivity.class);
                intent.putExtra("productItemId", clickedProduct.getId());
                intent.putExtra("detailName", clickedProduct.getProductItemName());
                intent.putExtra("detailPrice", clickedProduct.getPrice());
                intent.putExtra("detailDescription", clickedProduct.getDescription());
                intent.putExtra("imageUrl", clickedProduct.getImageUrl().toString());
                startActivity(intent);
            }
        });
    }

    private void performSearch(String query) {
        // Gọi API search với query
        ApiService.apiService.searchProductItemByName(query).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        binding.emptyTextView.setVisibility(View.GONE);
                        binding.labelName.setText("Kết quả tìm kiếm của: " + query);
                        productItemList = responseData.getData();
                        for (Map<String, Object> productItem : productItemList) {
                            String id = (String) productItem.get("id");
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
                        gridAdapter = new GridAdapter(ListProductSearchActivity.this, dataArrayList);
                        Log.i("productItemList", dataArrayList.toString());
                        binding.gridView.setAdapter(gridAdapter);
                    } else {
                        Log.e("Error", "Unexpected status");
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
