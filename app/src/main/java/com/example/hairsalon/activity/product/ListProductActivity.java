package com.example.hairsalon.activity.product;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.example.hairsalon.adapter.ListAdapter;
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

public class
ListProductActivity extends AppCompatActivity {

    ActivityListProductBinding binding;
    ListAdapter listAdapter;
    ArrayList<ProductItem> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>>  productItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ApiService.apiService.getProductItem().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        productItemList = responseData.getData(); // Lấy danh sách sản phẩm từ ResponseData
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
                        listAdapter = new ListAdapter(ListProductActivity.this, dataArrayList);
                        Log.i("productItemList", dataArrayList.toString());
                        binding.listview.setAdapter(listAdapter);
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

        binding.listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Lấy ra sản phẩm tương ứng với vị trí i trong danh sách
                ProductItem clickedProduct = dataArrayList.get(i);
                Log.i("clicked", clickedProduct.toString());

                // Tạo intent để chuyển sang DetailedActivity và gửi dữ liệu sản phẩm
                Intent intent = new Intent(ListProductActivity.this, DetailedActivity.class);
                intent.putExtra("detailName", clickedProduct.getProductItemName());
                intent.putExtra("detailPrice", clickedProduct.getPrice().toString()); // Chuyển giá thành String
                intent.putExtra("detailDescription", clickedProduct.getDescription());
                intent.putExtra("quantityInStock", clickedProduct.getQuantityInStock().toString());
                startActivity(intent);
            }
        });
    }
}
