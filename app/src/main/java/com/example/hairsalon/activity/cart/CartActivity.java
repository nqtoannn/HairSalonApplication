package com.example.hairsalon.activity.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.order.PayActivity;
import com.example.hairsalon.adapter.CartItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityCartBinding;
import com.example.hairsalon.model.CartItem;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding binding;
    CartItemAdapter cartItemAdapter;
    ArrayList<CartItem> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> cartItemList = new ArrayList<>();
    Integer customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getInt("userId", -1);

        getAllCartItem(); // Bắt đầu lấy dữ liệu
        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dataArrayList.size() == 0) {
                    Toast.makeText(CartActivity.this, "Bạn chưa có sản phẩm nào trong giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(CartActivity.this, PayActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void getAllCartItem() {
        ApiService.apiService.getAllCartItemsByCartId(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    processCartItems(response.body()); // Gọi phương thức xử lý dữ liệu khi nhận được dữ liệu từ máy chủ
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

    private void processCartItems(ResponseData responseData) {
        Double totalPrice = 0.0;
        if (responseData != null && responseData.getStatus().equals("OK")) {
            cartItemList = responseData.getData();
            for (Map<String, Object> cartItem : cartItemList) {
                Integer id = ((Number) cartItem.get("id")).intValue();
                String productItemName = (String) cartItem.get("productItemName");
                double price = (double) cartItem.get("price");
                Integer quantity = ((Number) Objects.requireNonNull(cartItem.get("quantity"))).intValue();
                String imageUrl = (String) cartItem.get("imageUrl");
                totalPrice += quantity * price;
                CartItem cartItemAdded = new CartItem(id, productItemName, quantity, imageUrl, price);
                dataArrayList.add(cartItemAdded);
            }
            Collections.reverse(dataArrayList);
            cartItemAdapter = new CartItemAdapter(CartActivity.this, dataArrayList);
            binding.listViewCart.setAdapter(cartItemAdapter);
            binding.totalPrice.setText(Utils.formatPrice(totalPrice));
            if (dataArrayList.size() == 0) {
                binding.payButton.setEnabled(false);
            } else {
                binding.payButton.setEnabled(true);
                binding.payButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
            }
        } else {
            Log.e("Error", "No product data found in response");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BackTo", "Back");
        // getAllCartItem();
    }
}
