package com.example.hairsalon.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.adapter.CartItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityCartBinding;
import com.example.hairsalon.databinding.ActivityPayBinding;
import com.example.hairsalon.model.CartItem;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity {

    ActivityPayBinding binding;

    CartItemAdapter cartItemAdapter;
    ArrayList<CartItem> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> cartItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
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
                        cartItemAdapter = new CartItemAdapter(PayActivity.this, dataArrayList);
                        Log.i("productItemList", dataArrayList.toString());
                        binding.listViewProducts.setAdapter(cartItemAdapter);
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

        Spinner spinnerPaymentMethod = findViewById(R.id.spinner_payment_method);
        Spinner spinnerDeliveryMethod = findViewById(R.id.spinner_delivery_method);

        String[] paymentMethods = {"Tiền mặt", "Thẻ tín dụng", "Chuyển khoản ngân hàng"};
        String[] deliveryMethods = {"Giao hàng nhanh", "Giao hàng tiết kiệm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        ArrayAdapter<String> deliveryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deliveryMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(adapter);
        spinnerDeliveryMethod.setAdapter(deliveryAdapter);

        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lấy phương thức thanh toán được chọn
                String selectedPaymentMethod = paymentMethods[position];

                // Xử lý logic khi người dùng chọn phương thức thanh toán
                Toast.makeText(PayActivity.this, "Phương thức thanh toán: " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có lựa chọn nào được chọn
            }
        });
    }
}
