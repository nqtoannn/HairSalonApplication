package com.example.hairsalon.activity.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.auth.Login;
import com.example.hairsalon.activity.manage.UserProfileFragment;
import com.example.hairsalon.activity.statistics.DoanhThu;
import com.example.hairsalon.activity.statistics.SanPham;
import com.example.hairsalon.databinding.FragmentAccountBinding;

public class AccountManageFragment extends Fragment {

    private FragmentAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String customerId = sharedPreferences.getString("userId", "");
        binding.username.setText("ADMIN");
        binding.userId.setText("Id:" + customerId);
        binding.btnCustomerProfile.setText("Thống kê doanh thu");
        binding.btnBookingHistory.setText("Quản lý dịch vụ");
        binding.btnOrderHistory.setText("Quản lý đơn hàng");
        binding.btnSupport.setText("So sánh doanh thu");
        binding.btnCustomerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), DoanhThu.class);
                startActivity(intent);
            }
        });

        binding.btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), SanPham.class);
                startActivity(intent);
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
