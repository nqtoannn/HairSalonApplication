package com.example.hairsalon.activity.product;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityAddProductBinding;
import com.example.hairsalon.utils.RealPathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductItemActivity extends AppCompatActivity {

    ActivityAddProductBinding binding;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        clickListeners();
    }

    private void clickListeners() {
        binding.selectImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 10);
            } else {
                ActivityCompat.requestPermissions(
                        AddProductItemActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        1
                );
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(path)) {
                    Toast.makeText(AddProductItemActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create product item
                JSONObject requestBody = createProductItem();

                String requestBodyString = requestBody.toString();
                String apiUrl = Constant.baseUrl + "management/productItem/add";

                StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Toast.makeText(getApplicationContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    int data = jsonResponse.getInt("data");
                                    uploadFile(String.valueOf(data));
                                } catch (JSONException e) {
                                    Log.e("Loi", e.getMessage());
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("appointment", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thêm sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public byte[] getBody() {
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
                Volley.newRequestQueue(AddProductItemActivity.this).add(request);
            }
        }


        );
    }


    private JSONObject createProductItem() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("productItemName", binding.nameProduct.getText().toString()); // Lấy giá trị từ EditText
            String priceString = binding.price.getText().toString(); // Lấy chuỗi từ EditText
            double price = Double.parseDouble(priceString); // Chuyển đổi chuỗi sang double
            requestBody.put("price", price); // Sử dụng giá trị double trong JSONObject
            requestBody.put("productId", 7);
            requestBody.put("warrantyTime", 18);
            requestBody.put("quantity", 100);
            requestBody.put("status", "OK");
            requestBody.put("description", binding.description.getText().toString());
            requestBody.put("imageUrl", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    private void uploadFile(String productItemId) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String namePath = "hihi";

        Call<Void> call = ApiService.apiService.uploadFile(body, namePath, productItemId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(AddProductItemActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddProductItemActivity.this, ListProductItem.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddProductItemActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Context context = AddProductItemActivity.this;
            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            binding.imageview.setImageBitmap(bitmap);
        }
    }
}
