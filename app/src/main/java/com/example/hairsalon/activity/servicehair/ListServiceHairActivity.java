package com.example.hairsalon.activity.servicehair;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.product.ListProductItem;
import com.example.hairsalon.adapter.ListProductItemAdapter;
import com.example.hairsalon.adapter.ListServiceHairAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityListServiceHairBinding;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.ServiceHair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListServiceHairActivity extends AppCompatActivity {

    ActivityListServiceHairBinding binding;

    ArrayList<ServiceHair> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> serviceHairList = new ArrayList<>();

    ListServiceHairAdapter serviceHairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListServiceHairBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListServiceHairActivity.this, AddServiceHairActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getAllServiceHairs() {
        ApiService.apiService.getAllServiceHairs().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        dataArrayList.clear();
                        serviceHairList = responseData.getData();
                        for (Map<String, Object> serviceHair : serviceHairList) {
                            Integer id = ((Number) serviceHair.get("id")).intValue();
                            String serviceName = (String) serviceHair.get("serviceName");
                            double price = (double) serviceHair.get("price");
                            String imageUrl = (String) serviceHair.get("url");
                            String description = (String) serviceHair.get("description");
                            ServiceHair serviceHairAdd = new ServiceHair(id, serviceName, description,imageUrl ,price);
                            dataArrayList.add(serviceHairAdd);
                        }
                        serviceHairAdapter = new ListServiceHairAdapter(ListServiceHairActivity.this, dataArrayList);
                        Log.i("serviceHairList", dataArrayList.toString());
                        binding.gridView.setAdapter(serviceHairAdapter);
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
        getAllServiceHairs();
        super.onResume();
    }


}