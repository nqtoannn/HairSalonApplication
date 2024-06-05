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

import com.example.hairsalon.activity.servicehair.AddServiceHairActivity;
import com.example.hairsalon.adapter.ListServiceHairAdapter;
import com.example.hairsalon.api.ApiService;

import com.example.hairsalon.databinding.FragmentServiceManageBinding;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.ServiceHair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ServiceManageFragment extends Fragment {

    private FragmentServiceManageBinding binding;

    private ArrayList<ServiceHair> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> serviceHairList = new ArrayList<>();

    private ListServiceHairAdapter serviceHairAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentServiceManageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up click listener for add button
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start AddServiceHairActivity
                Intent intent = new Intent(requireActivity(), AddServiceHairActivity.class);
                startActivity(intent);
            }
        });

        // Fetch and display service hairs
        getAllServiceHairs();
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
                            String id = (String) serviceHair.get("id");
                            String serviceName = (String) serviceHair.get("serviceName");
                            double price = (double) serviceHair.get("price");
                            String imageUrl = (String) serviceHair.get("url");
                            String description = (String) serviceHair.get("description");
                            ServiceHair serviceHairAdd = new ServiceHair(id, serviceName, description, imageUrl, price);
                            dataArrayList.add(serviceHairAdd);
                        }
                        serviceHairAdapter = new ListServiceHairAdapter(requireActivity(), dataArrayList);
                        binding.gridView.setAdapter(serviceHairAdapter);
                    } else {
                        Log.e("Error", "No service hair data found in response");
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