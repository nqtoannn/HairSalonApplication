package com.example.hairsalon.activity.order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import com.android.volley.Response;

import android.content.Intent;

import java.util.Objects;


public class PayActivity extends AppCompatActivity {

    ActivityPayBinding binding;

    CartItemAdapter cartItemAdapter;
    ArrayList<Map<String, Object>> cartItemList = new ArrayList<>();

    ArrayList<CartItem> dataArrayList = new ArrayList<>();
    Double price = 0.0;
    Integer productItemId;
    private Double totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent.getStringExtra("detailName") != null) {
            binding.linearLayout.setVisibility(View.VISIBLE);
            binding.listViewProducts.setVisibility(View.GONE);
            String name = intent.getStringExtra("detailName");
            price = intent.getDoubleExtra("detailPrice", 0);
            String imageUrl = intent.getStringExtra("imageUrl");
            productItemId = intent.getIntExtra("productItemId", 0);
            binding.productName.setText(name);
            binding.productPrice.setText(String.valueOf(price));
            binding.productQuantity.setText("1");
            binding.xSeparator.setVisibility(View.VISIBLE);
            binding.productQuantity.setVisibility(View.VISIBLE);
            binding.totalPrice.setText(String.valueOf(price));
            RequestQueue requestQueue = Volley.newRequestQueue(PayActivity.this);
            ImageRequest imageRequest = new ImageRequest(
                    imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            // Gán hình ảnh vào ImageView
                            binding.productImage.setImageBitmap(response);
                        }
                    },
                    0,
                    0,
                    ImageView.ScaleType.CENTER_INSIDE,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý khi có lỗi xảy ra
                            Log.e("Volley", "Failed to load image", error);
                        }
                    }
            );
            requestQueue.add(imageRequest);

        } else {
            binding.linearLayout.setVisibility(View.GONE);
            binding.listViewProducts.setVisibility(View.VISIBLE);
            getCartItems();
        }

        setupSpinners();
        binding.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
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

                            Integer id = ((Number) cartItem.get("id")).intValue();
                            String productItemName = (String) cartItem.get("productItemName");
                            Integer quantity = ((Number) Objects.requireNonNull(cartItem.get("quantity"))).intValue();
                            String imageUrl = (String) cartItem.get("imageUrl");
                            double priceProduct = (double) cartItem.get("price");
                            totalPrice +=  quantity * priceProduct;

                            CartItem cartItemAdded = new CartItem(id, productItemName, quantity, imageUrl, priceProduct);
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
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            requestBody.put("orderDate", currentDate);
            JSONArray orderItemListJson = new JSONArray();
            if (price != 0.0) {
                requestBody.put("totalPrice", price);
                JSONObject orderItemJson = new JSONObject();
                orderItemJson.put("productItemId", productItemId);
                orderItemJson.put("price", price);
                orderItemJson.put("quantity", 1);
                orderItemListJson.put(orderItemJson);
                requestBody.put("orderItemList", orderItemListJson);
            }
            else {
                for (Map<String, Object> cartItem : cartItemList) {
                    JSONObject orderItemJson = new JSONObject();
                    orderItemJson.put("productItemId", cartItem.get("productItemId"));
                    orderItemJson.put("price", cartItem.get("price"));
                    orderItemJson.put("quantity", cartItem.get("quantity"));
                    orderItemListJson.put(orderItemJson);
                }
                requestBody.put("totalPrice", totalPrice);
                requestBody.put("orderItemList", orderItemListJson);
                deleteAllCartItems();
            }

            StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Intent intent = new Intent(PayActivity.this, SuccessPay.class);
                            startActivity(intent);
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

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đặt hàng");
        builder.setMessage("Xác nhận đặt đơn hàng này?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createOrder();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}