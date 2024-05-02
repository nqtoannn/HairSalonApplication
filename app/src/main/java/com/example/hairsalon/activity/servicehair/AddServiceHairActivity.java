package com.example.hairsalon.activity.servicehair;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.home.HomeManage;
import com.example.hairsalon.activity.product.AddProductItemActivity;
import com.example.hairsalon.activity.product.ListProductItem;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityAddServiceHairBinding;
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

public class AddServiceHairActivity extends AppCompatActivity {

    ActivityAddServiceHairBinding binding;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddServiceHairBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        clickListeners();
    }

    private void clickListeners() {
        binding.selectImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 10);
            } else {
                ActivityCompat.requestPermissions(AddServiceHairActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (TextUtils.isEmpty(path)) {
                                                    Toast.makeText(AddServiceHairActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }

                                                // Create product item
                                                JSONObject requestBody = createServiceHair();

                                                String requestBodyString = requestBody.toString();
                                                String apiUrl = Constant.baseUrl + "management/serviceHair/add";

                                                StringRequest request = new StringRequest(Request.Method.POST, apiUrl, new com.android.volley.Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            JSONObject jsonResponse = new JSONObject(response);
                                                            Toast.makeText(getApplicationContext(), "Thêm dịch vụ thành công", Toast.LENGTH_SHORT).show();
                                                            int data = jsonResponse.getInt("data");
                                                            uploadFile(String.valueOf(data));
                                                        } catch (JSONException e) {
                                                            Log.e("Loi", e.getMessage());
                                                            throw new RuntimeException(e);
                                                        }
                                                    }
                                                }, new com.android.volley.Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thêm dịch vụ", Toast.LENGTH_SHORT).show();
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
                                                Volley.newRequestQueue(AddServiceHairActivity.this).add(request);
                                            }
                                        }


        );
    }

    private void uploadFile(String serviceHairId) {
        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String namePath = "serviceHair";

        Call<Void> call = ApiService.apiService.uploadServiceFile(body, namePath, serviceHairId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(AddServiceHairActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddServiceHairActivity.this, HomeManage.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AddServiceHairActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.toString());
            }
        });
    }

    private JSONObject createServiceHair() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("serviceName", binding.nameService.getText().toString()); // Lấy giá trị từ EditText
            String priceString = binding.price.getText().toString(); // Lấy chuỗi từ EditText
            double price = Double.parseDouble(priceString); // Chuyển đổi chuỗi sang double
            requestBody.put("price", price); // Sử dụng giá trị double trong JSONObject
            requestBody.put("description", binding.description.getText().toString());
            requestBody.put("url", "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return requestBody;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Context context = AddServiceHairActivity.this;
            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            binding.imageview.setImageBitmap(bitmap);
        }
    }
}