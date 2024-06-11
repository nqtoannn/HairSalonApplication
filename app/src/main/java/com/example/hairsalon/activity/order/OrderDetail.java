package com.example.hairsalon.activity.order;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityOrderDetailBinding;
import com.example.hairsalon.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderDetail extends AppCompatActivity {

    ActivityOrderDetailBinding binding;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            String nameProduct = intent.getStringExtra("nameProduct");
            String orderStatus = intent.getStringExtra("status");
            double price = intent.getDoubleExtra("price", 0.0);
            int quantity = intent.getIntExtra("quantity", 0);
            String address = intent.getStringExtra("address");
            String imageUrl = intent.getStringExtra("imageUrl");
            String orderId = intent.getStringExtra("orderId");
            binding.nameProduct.setText("Tên sản phẩm: " + nameProduct);
            binding.price.setText("Giá: "+ price);
            binding.quantity.setText("Số lượng: " + 1);
            binding.address.setText("Địa chỉ: "+ address);
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

            if (orderStatus.equals("PROCESSING")) {
                binding.buttonCancel.setVisibility(View.VISIBLE);

                binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showConfirmationDialog(orderId);
                    }
                });
            }
            else {
                binding.buttonCancel.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void updateStatusOrder(String orderId) {
        String apiUrl = Constant.baseUrl + "order";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", orderId);
            requestBody.put("orderStatus", "REJECTED");

            final String requestBodyString = requestBody.toString();

            Log.i("request body", requestBodyString);

            StringRequest request = new StringRequest(Request.Method.PUT, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getApplicationContext(), "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi khi gửi yêu cầu
                            Log.e("update status", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi hủy đơn hàng", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }) {
                @Override
                public byte[] getBody() {
                    // Trả về mảng byte của requestBodyString
                    return requestBodyString.getBytes();
                }

                @Override
                public Map<String, String> getHeaders() {
                    // Thiết lập tiêu đề yêu cầu
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            // Thêm yêu cầu vào hàng đợi RequestQueue của Volley
            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi tạo yêu cầu", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đặt hàng");
        builder.setMessage("Xác nhận đặt đơn hàng này?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateStatusOrder(orderId);
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