package com.example.hairsalon.activity.manage;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.product.AddProductItemActivity;
import com.example.hairsalon.adapter.ListProductItemAdapter;
import com.example.hairsalon.adapter.UserAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentEmployeeManageBinding;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmployeeManageFragment extends Fragment {
    private FragmentEmployeeManageBinding binding;
    private ArrayList<User> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> employeeList = new ArrayList<>();
    private UserAdapter userAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEmployeeManageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call method to fetch product items
        getAllEmployee();
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddEmployeeFragment fragment = new AddEmployeeFragment();
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frameLayoutMn, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
            }
        });
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserAdapter adapter = (UserAdapter) parent.getAdapter();
                User selectedUser = adapter.getItem(position);

                if (selectedUser != null) {
                    int userId = selectedUser.getId();
                    EmployeeInfoFragment fragment = EmployeeInfoFragment.newInstance(userId);
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frameLayoutMn, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });
    }

    private void getAllEmployee() {
        ApiService.apiService.getAllEmployees().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        // Clear previous data
                        dataArrayList.clear();

                        employeeList = responseData.getData();
                        for (Map<String, Object> employee : employeeList) {
                            Integer id = ((Number) employee.get("id")).intValue();
                            String email = (String) employee.get("email");
                            String userName = (String) employee.get("userName");
                            String fullName = (String) employee.get("fullName");
                            String phoneNumber = (String) employee.get("phoneNumber");
                            String address = (String) employee.get("address");
                            String status = (String) employee.get("status");
                            User user = new User(id,userName,email,"","EMPLOYEE",fullName,phoneNumber,address,status);
                            dataArrayList.add(user);
                        }
                        userAdapter = new UserAdapter(requireContext(),dataArrayList);
                        binding.gridView.setAdapter(userAdapter);
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