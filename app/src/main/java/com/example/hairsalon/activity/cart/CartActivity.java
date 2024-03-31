package com.example.hairsalon.activity.cart;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.PayActivity;
import com.example.hairsalon.activity.product.DetailProductActivity;
import com.example.hairsalon.activity.product.ListProductActivity;
import com.example.hairsalon.adapter.CartItemAdapter;
import com.example.hairsalon.adapter.GridAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityCartBinding;
import com.example.hairsalon.databinding.ActivityListProductBinding;
import com.example.hairsalon.model.CartItem;
import com.example.hairsalon.model.ProductItem;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.Collection;
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
    private List<Map<String, Object>>  cartItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ApiService.apiService.getAllCartItemsByCartId(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    double totalPrice = 0.0;
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        cartItemList = responseData.getData(); // Lấy danh sách sản phẩm từ ResponseData
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
                        Log.i("productItemList", dataArrayList.toString());
                        binding.listViewCart.setAdapter(cartItemAdapter);
                        binding.totalPrice.setText(String.valueOf(totalPrice));
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

        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CartActivity.this, PayActivity.class);
                startActivity(intent);
            }
        });

    }
}