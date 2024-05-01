package com.example.hairsalon.activity.product;

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
import com.example.hairsalon.R;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityUpdateProductItemBinding;
import com.example.hairsalon.utils.RealPathUtil;
import com.example.hairsalon.utils.Utils;

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

public class UpdateProductItemActivity extends AppCompatActivity {

    ActivityUpdateProductItemBinding binding;

    String path;

    RequestQueue requestQueue;
    Integer productItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProductItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("productItemName");
            double price = intent.getDoubleExtra("detailPrice", 0.0);
            String description = intent.getStringExtra("description");
            String imageUrl = intent.getStringExtra("imageUrl");
            productItemId = intent.getIntExtra("productItemId", 0);
            Integer quantity = intent.getIntExtra("quantity", 0);
            binding.nameProduct.setText(name);
            binding.description.setText(description);;
            binding.price.setText(String.valueOf(price));
            binding.quantityOfProduct.setText(String.valueOf(quantity));

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
        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Create product item
                JSONObject requestBody = updateProductItem(productItemId);

                String requestBodyString = requestBody.toString();
                String apiUrl = Constant.baseUrl + "management/productItem/update";

                StringRequest request = new StringRequest(Request.Method.PUT, apiUrl,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    Toast.makeText(getApplicationContext(), "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                    int data = jsonResponse.getInt("data");
                                    if (TextUtils.isEmpty(path)) {
                                        Toast.makeText(UpdateProductItemActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(UpdateProductItemActivity.this, ListProductItem.class);
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
                                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi cập nhật sản phẩm", Toast.LENGTH_SHORT).show();
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
                Volley.newRequestQueue(UpdateProductItemActivity.this).add(request);
            }
        });

        binding.updateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(productItemId);
            }
        });

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
                        UpdateProductItemActivity.this,
                        new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        1
                );
            }
        });
    }

    private JSONObject updateProductItem(Integer productItemId) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("productItemId", productItemId);
            requestBody.put("productItemName", binding.nameProduct.getText().toString()); // Lấy giá trị từ EditText
            String priceString = binding.price.getText().toString(); // Lấy chuỗi từ EditText
            double price = Double.parseDouble(priceString); // Chuyển đổi chuỗi sang double
            requestBody.put("price", price); // Sử dụng giá trị double trong JSONObject
            requestBody.put("warrantyTime", 18);
            int quantity = Integer.parseInt(binding.quantityOfProduct.getText().toString());
            requestBody.put("quantity", quantity);
            requestBody.put("status", "NEW");
            requestBody.put("description", binding.description.getText().toString());
            requestBody.put("imageUrl", "");
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

        Call<Void> call = ApiService.apiService.uploadFile(body, namePath, productItemId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                Toast.makeText(UpdateProductItemActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateProductItemActivity.this, ListProductItem.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateProductItemActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                Log.e("Failure", t.toString());
            }
        });
    }

    private void updateProductItemStatus(Integer productItemId) {
        String apiUrl = Constant.baseUrl + "management/productItem/updateStatus";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("productItemId", productItemId); // Thay đổi customerId tùy theo người dùng hiện tại

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
                            Log.e("update status", error.getMessage());
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

    private void showConfirmationDialog(Integer productItemId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận hành động");
        builder.setMessage("Bạn có chắc chắn muốn ngừng kinh doanh mặt hàng này không?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateProductItemStatus(productItemId);
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
            Context context = UpdateProductItemActivity.this;
            path = RealPathUtil.getRealPath(context, uri);
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            binding.imageview.setImageBitmap(bitmap);
        }
    }

}