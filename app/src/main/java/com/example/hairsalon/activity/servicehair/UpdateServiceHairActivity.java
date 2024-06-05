package com.example.hairsalon.activity.servicehair;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityUpdateServiceHairBinding;
import com.example.hairsalon.utils.RealPathUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UpdateServiceHairActivity extends AppCompatActivity {

    ActivityUpdateServiceHairBinding binding;

    RequestQueue requestQueue;

    String path;

    String serviceHairId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateServiceHairBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            serviceHairId = intent.getStringExtra("serviceHairId");
            String name = intent.getStringExtra("serviceName");
            double price = intent.getDoubleExtra("detailPrice", 0.0);
            String description = intent.getStringExtra("description");
            String imageUrl = intent.getStringExtra("imageUrl");
            binding.nameService.setText(name);
            binding.description.setText(description);;
            binding.price.setText(String.valueOf(price));

            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(this);
            }
            ImageRequest imageRequest = new ImageRequest(
                    imageUrl,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            binding.imageview.setImageBitmap(response);
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
        binding.updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(serviceHairId);
            }
        });
        clickListeners();
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JSONObject requestBody = updateServiceHair(serviceHairId);

                String requestBodyString = requestBody.toString();
                String apiUrl = Constant.baseUrl + "management/serviceHair/update";

                StringRequest request = new StringRequest(Request.Method.PUT, apiUrl,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Toast.makeText(getApplicationContext(), "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
                                    int data = jsonResponse.getInt("data");
                                    if (TextUtils.isEmpty(path)) {
                                        Toast.makeText(UpdateServiceHairActivity.this, "Thành công - Không cập nhật ảnh", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UpdateServiceHairActivity.this, ListServiceHairActivity.class);
                                        startActivity(intent);
                                    }
                                    else uploadFile(String.valueOf(data));
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        },
                        new com.android.volley.Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("appointment", error.getMessage());
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi cập nhật dịch vụ", Toast.LENGTH_SHORT).show();
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
                Volley.newRequestQueue(UpdateServiceHairActivity.this).add(request);
            }
        });

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
                        UpdateServiceHairActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        1
                );
            }
        });
    }

    private JSONObject updateServiceHair(String id) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("id", id);
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

    private void uploadFile(String productItemId) {
        File file = new File(path);
        if (!file.exists()) {
            Log.i("not exist", "Khong ton tai file");
            return;
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        String namePath = "hihi";

        Call<Void> call = ApiService.apiService.uploadServiceFile(body, namePath, productItemId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Toast.makeText(UpdateServiceHairActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateServiceHairActivity.this, ListServiceHairActivity.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateServiceHairActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.toString());
            }
        });
    }

    private void updateServiceHairStatus(String serviceHairId) {
        String apiUrl = Constant.baseUrl + "management/serviceHair/updateStatus";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("id", serviceHairId);

            final String requestBodyString = requestBody.toString();

            Log.i("request body", requestBodyString);

            StringRequest request = new StringRequest(Request.Method.PUT, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Xử lý phản hồi thành công từ máy chủ
                            Toast.makeText(getApplicationContext(), "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi khi gửi yêu cầu
                            Log.e("update status", Objects.requireNonNull(error.getMessage()));
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi cập nhật trạng thái", Toast.LENGTH_SHORT).show();
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

    private void showConfirmationDialog(String productItemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận hành động");
        builder.setMessage("Bạn có chắc chắn muốn ngừng kinh doanh dịch vụ này không?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateServiceHairStatus(productItemId);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Context context = UpdateServiceHairActivity.this;
            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            binding.imageview.setImageBitmap(bitmap);
        }
    }

}