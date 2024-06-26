package com.example.hairsalon.activity.manage;

import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.home.AccountFragment;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentUserProfileBinding;
import com.example.hairsalon.model.ResponseData;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileUpdateFragment extends Fragment {
    private FragmentUserProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer customerId = sharedPreferences.getInt("userId", -1);

        ApiService.apiService.getCustomerById(customerId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        Map<String, Object> customer = responseData.getData().get(0);
                        binding.textAddress.setText(customer.get("address").toString());
                        binding.textUserName.setText(customer.get("userName").toString());
                        binding.textPhoneNumber.setText(customer.get("phoneNumber").toString());
                        binding.textFullName.setText(customer.get("fullName").toString());
                    }
                } else {
                    Toast.makeText(requireContext(), "Thay đổi trạng thái tài khoản không thành công!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("id", customerId);
                jsonObject.addProperty("fullName", binding.textFullName.getText().toString());
                jsonObject.addProperty("address", binding.textAddress.getText().toString());
                jsonObject.addProperty("phoneNumber", binding.textPhoneNumber.getText().toString());
                jsonObject.addProperty("userName", binding.textUserName.getText().toString());

                ApiService.apiService.updateUserProfile(jsonObject).enqueue(new Callback<ResponseData>() {
                    @Override
                    public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();
                            loadAccountFragment(); // Load AccountFragment sau khi cập nhật thành công
                        } else {
                            Toast.makeText(requireContext(), "Cập nhật thông tin không thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseData> call, Throwable t) {
                        Log.e("Error", "API call failed: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void loadAccountFragment() {
        // Tạo một instance mới của AccountFragment
        AccountFragment fragment = new AccountFragment();

        // Lấy ra FragmentManager từ Fragment hiện tại
        FragmentManager fragmentManager = getParentFragmentManager();

        // Thay thế Fragment hiện tại bằng AccountFragment
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
