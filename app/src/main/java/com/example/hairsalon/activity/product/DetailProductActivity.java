package com.example.hairsalon.activity.product;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.hairsalon.activity.order.PayActivity;
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

import retrofit2.Call;
import retrofit2.Callback;

public class DetailProductActivity extends AppCompatActivity {

    ActivityDetailProductBinding binding;
    RequestQueue requestQueue;

    private Integer productItemId;
    private String name, imageUrl;
    private Double price;
    private List<Map<String, Object>> cartItemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getAllCartItemsAndUpdateCount();
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("detailName");
            price = intent.getDoubleExtra("detailPrice", 0.0);
            String description = intent.getStringExtra("detailDescription");
            imageUrl = intent.getStringExtra("imageUrl");
            productItemId = intent.getIntExtra("productItemId", 0);
            binding.textProductName.setText(name);
            binding.textProductDescription.setText(description);
            String priceDisplay = String.valueOf(price);
            String formattedPrice = String.format(getString(R.string.price_placeholder), priceDisplay);
            binding.textProductPrice.setText(formattedPrice);
            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }

            ImageRequest imageRequest = new ImageRequest(
                    imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            binding.imageProduct.setImageBitmap(response);
                        }
                    },
                    0,
                    0,
                    ImageView.ScaleType.CENTER_CROP,
                    Bitmap.Config.RGB_565,
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error if necessary
                        }
                    }
            );
            requestQueue.add(imageRequest);
        }

        binding.buttonAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCart();
            }
        });
        binding.iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        binding.buttonBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailProductActivity.this, PayActivity.class);
                intent.putExtra("productItemId", productItemId);
                intent.putExtra("detailName", name);
                intent.putExtra("detailPrice", price); // Chuyển giá thành String
                intent.putExtra("imageUrl", imageUrl);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("BackTo", "Back");
        getAllCartItemsAndUpdateCount();
    }


    private void getAllCartItemsAndUpdateCount() {
        Log.i("Called Api", "Called");
        ApiService.apiService.getAllCartItemsByCartId(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        cartItemList = responseData.getData();
                        Integer totalNumber = 0;
                        for (Map<String, Object> cartItemList : cartItemList) {
                            totalNumber +=  ((Number) cartItemList.get("quantity")).intValue();
                        }
                        updateCartCount(totalNumber);
                    } else {
                        updateCartCount(0);
                        Log.e("Error", "No cart item data found in response");
                    }
                } else {
                    updateCartCount(0);
                    Log.e("Error", "API call failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                updateCartCount(0);
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }

    private void addToCart() {
        Intent intent = getIntent();
        if (productItemId == 0) {
            productItemId = intent.getIntExtra("productItemId", 0);
        }
        String apiUrl = Constant.baseUrl + "customer/addToCart";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("cart_id", 1);
            requestBody.put("product_item_id", productItemId);
            requestBody.put("quantity", 1);

            StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            getAllCartItemsAndUpdateCount(); // After successfully adding to cart, update cart count
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi tạo yêu cầu", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCartCount(Integer cartItemCount) {
        Log.i("Updating", String.valueOf(cartItemCount));
        binding.textCartCount.setText(String.valueOf(cartItemCount));
    }
}
