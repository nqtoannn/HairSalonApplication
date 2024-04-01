package com.example.hairsalon.activity.pay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.adapter.CartItemAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityPayBinding;
import com.example.hairsalon.model.CartItem;
import com.example.hairsalon.model.ResponseData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import com.android.volley.Response;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.adapter.GridAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityDetailProductBinding;
import com.example.hairsalon.model.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;


public class PayActivity extends AppCompatActivity {

    ActivityPayBinding binding;

    CartItemAdapter cartItemAdapter;
    ArrayList<Map<String, Object>> cartItemList = new ArrayList<>();

    ArrayList<CartItem> dataArrayList = new ArrayList<>();

    private Double totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getCartItems();
        setupSpinners();
        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrder();
            }
        });
    }

    private void getCartItems() {
        ApiService.apiService.getAllCartItemsByCartId(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        cartItemList = (ArrayList<Map<String, Object>>) responseData.getData();
                        totalPrice = 0.0;
                        for (Map<String, Object> cartItem : cartItemList) {
                            totalPrice +=  (double) cartItem.get("quantity") *  (double) cartItem.get("price");
                            Integer id = ((Number) cartItem.get("id")).intValue();
                            String productItemName = (String) cartItem.get("productItemName");
                            double price = (double) cartItem.get("price");
                            Integer quantity = ((Number) Objects.requireNonNull(cartItem.get("quantity"))).intValue();
                            String imageUrl = (String) cartItem.get("imageUrl");
                            totalPrice += quantity * price;
                            CartItem cartItemAdded = new CartItem(id, productItemName, quantity, imageUrl, price);
                            dataArrayList.add(cartItemAdded);
                        }
                        binding.totalPrice.setText(String.valueOf(totalPrice));
                        Collections.reverse(dataArrayList);
                        cartItemAdapter = new CartItemAdapter(PayActivity.this, dataArrayList);
                        Log.i("productItemList", dataArrayList.toString());
                        binding.listViewProducts.setAdapter(cartItemAdapter);
                    } else {
                        Log.e("Error", "No cart item data found in response");
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

    private void showCartItems() {

    }

    private void setupSpinners() {
        String[] paymentMethods = {"Tiền mặt", "Thẻ tín dụng", "Chuyển khoản ngân hàng"};
        String[] deliveryMethods = {"Giao hàng nhanh", "Giao hàng tiết kiệm"};

        ArrayAdapter<String> paymentAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        ArrayAdapter<String> deliveryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deliveryMethods);

        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerPaymentMethod.setAdapter(paymentAdapter);
        binding.spinnerDeliveryMethod.setAdapter(deliveryAdapter);
    }

    private void createOrder() {
        String apiUrl = Constant.baseUrl + "customer/order";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("customerId", 1);
            requestBody.put("payId", 1);
            requestBody.put("totalPrice", totalPrice);
            requestBody.put("orderDate", "2023-10-10");
            JSONArray orderItemListJson = new JSONArray();
            for (Map<String, Object> cartItem : cartItemList) {
                JSONObject orderItemJson = new JSONObject();
                orderItemJson.put("productItemId", cartItem.get("productItemId"));
                orderItemJson.put("price", cartItem.get("price"));
                orderItemJson.put("quantity", cartItem.get("quantity"));
                orderItemListJson.put(orderItemJson);
            }
            requestBody.put("orderItemList", orderItemListJson);
            StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            deleteAllCartItems();
                            Toast.makeText(getApplicationContext(), "Đơn hàng của bạn đã được thanh toán", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("Error response", String.valueOf(error));
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thanh toán đơn hàng", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public byte[] getBody() {
                    return requestBody.toString().getBytes();
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteAllCartItems() {
        ApiService.apiService.deleteAllCartItemsByCartId(1).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(PayActivity.this, SuccessPay.class);
                    startActivity(intent);
                } else {
                    Log.e("Error", "API call failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }



}
