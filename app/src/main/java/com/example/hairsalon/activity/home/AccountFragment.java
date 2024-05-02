package com.example.hairsalon.activity.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.auth.Login;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentAccountBinding;
import com.example.hairsalon.model.ResponseData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer customerId = sharedPreferences.getInt("userId", -1);

        ApiService.apiService.getCustomerById(customerId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        Map<String, Object> customer = responseData.getData().get(0);
                        binding.username.setText(customer.get("fullName").toString());
                        binding.userId.setText("Mã khách hàng: "+ customerId.toString());
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

        binding.btnLogOutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return binding.getRoot();
    }

    private void logout() {
        // Xóa tất cả dữ liệu của người dùng đã đăng nhập
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Điều hướng sang màn hình đăng nhập
        Intent intent = new Intent(requireActivity(), Login.class);
        startActivity(intent);
        requireActivity().finish(); // Kết thúc Activity hiện tại sau khi đăng xuất
    }
}
