package com.example.hairsalon.activity.servicehair;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.activity.appointment.AppointmentActivity;
import com.example.hairsalon.adapter.ReviewAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityDetailServiceHairBinding;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.Review;
import com.example.hairsalon.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class DetailServiceHairActivity extends AppCompatActivity {

    ActivityDetailServiceHairBinding binding;

    private String serviceHairId, customerId;
    private String name, imageUrl;
    private Double price;
    RequestQueue requestQueue;
    ArrayList<Review> dataArrayList = new ArrayList<>();
    private List<Map<String, Object>> reviewItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailServiceHairBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        customerId = sharedPreferences.getString("userId", "");
        Intent intent = getIntent();
        serviceHairId = intent.getStringExtra("serviceHairId");
        name = intent.getStringExtra("detailName");
        price = intent.getDoubleExtra("detailPrice", 0.0);
        String description = intent.getStringExtra("detailDescription");
        imageUrl = intent.getStringExtra("imageUrl");
        // checkCanReview(serviceHairId, customerId);
        binding.textProductName.setText(name);
        binding.textProductDescription.setText(description);
        binding.textProductPrice.setText(Utils.formatPrice(price));
        // fetchReviews(serviceHairId);

        // Load image using Volley
        ImageRequest imageRequest = new ImageRequest(
                imageUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        binding.imageServiceHair.setImageBitmap(response);
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
                        Log.e("ImageRequest", "Error loading image: " + error.getMessage());
                    }
                }
        );
        requestQueue.add(imageRequest);

        // Set onClickListener for the "Make Appointment" button
        binding.btnMakeApm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailServiceHairActivity.this, AppointmentActivity.class);
                intent.putExtra("serviceHairId", serviceHairId);
                intent.putExtra("serviceName", name);
                startActivity(intent);
            }
        });

//        binding.btnAddReview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAddReviewDialog();
//            }
//        });

    }

//    private void fetchReviews(String serviceHairId) {
//        ApiService.apiService.findAllReviewByServiceId(serviceHairId).enqueue(new Callback<ResponseData>() {
//            @Override
//            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
//                if (response.isSuccessful()) {
//                    ResponseData responseData = response.body();
//                    if (responseData != null && responseData.getStatus().equals("OK")) {
//                        // Xóa dữ liệu trước đó trong dataArrayList
//                        dataArrayList.clear();
//                        reviewItemList = responseData.getData();
//                        for (Map<String, Object> reviewMap : reviewItemList) {
//                            double reviewServiceIdDouble = (double) reviewMap.get("reviewServiceId");
//                            String id = String.valueOf(Math.round(reviewServiceIdDouble));
//                            String customerName = (String) reviewMap.get("customerName");
//                            String comment = (String) reviewMap.get("comment");
//                            double doubleRating = (double) reviewMap.get("rating");
//                            int rating = (int) Math.round(doubleRating);
//                            String reviewDate = (String) reviewMap.get("reviewDate");
//                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault());
//                            String formattedDate = null;
//                            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm yyyy-MM-dd", Locale.getDefault());
//
//                            try {
//                                Date date = inputFormat.parse(reviewDate);
//                                formattedDate = outputFormat.format(date);
//
//                                System.out.println(formattedDate); // In ra: 13:53 2024-05-12
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//
//                            Review review = new Review(id, customerName, comment, rating, formattedDate);
//                            dataArrayList.add(review);
//                        }
//                        Collections.reverse(dataArrayList);
//                        ReviewAdapter reviewAdapter = new ReviewAdapter(DetailServiceHairActivity.this, dataArrayList);
//                        binding.listviewComments.setAdapter(reviewAdapter);
//                        int itemHeight = 320; // Độ cao của mỗi item
//                        int totalHeight = itemHeight * dataArrayList.size(); // Tích của độ cao và số lượng item
//                        ViewGroup.LayoutParams params = binding.listviewComments.getLayoutParams();
//                        params.height = totalHeight;
//                        binding.listviewComments.setLayoutParams(params);
//                    } else {
//                        Log.e("Error", "No product data found in response");
//                    }
//                } else {
//                    Log.e("Error", "API call failed with error code: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseData> call, Throwable t) {
//                Log.e("Error", "API call failed: " + t.getMessage());
//            }
//        });
//    }
//
//    private void showAddReviewDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.add_review_dialog, null);
//        builder.setView(dialogView);
//        builder.setTitle("Thêm đánh giá");
//
//        // Xử lý các thành phần trong dialog
//        RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
//        EditText commentEditText = dialogView.findViewById(R.id.editText_comment);
//
//        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Integer rating = (int) ratingBar.getRating();
//                String comment = commentEditText.getText().toString();
//
//                JSONObject requestBody = addAReview(rating, comment);
//
//                String requestBodyString = requestBody.toString();
//                String apiUrl = Constant.baseUrl + "customer/addReviewService";
//
//                StringRequest request = new StringRequest(Request.Method.POST, apiUrl, new com.android.volley.Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            Toast.makeText(getApplicationContext(), "Thêm bình luận thành công", Toast.LENGTH_SHORT).show();
//                            fetchReviews(serviceHairId);
//                            checkCanReview(serviceHairId, customerId);
//                        } catch (JSONException e) {
//                            Log.e("Loi", e.getMessage());
//                            throw new RuntimeException(e);
//                        }
//                    }
//                }, new com.android.volley.Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi thêm bình luận", Toast.LENGTH_SHORT).show();
//                    }
//                }) {
//                    @Override
//                    public byte[] getBody() {
//                        return requestBodyString.getBytes();
//                    }
//
//                    @Override
//                    public Map<String, String> getHeaders() {
//                        // Thiết lập tiêu đề yêu cầu
//                        Map<String, String> headers = new HashMap<>();
//                        headers.put("Content-Type", "application/json");
//                        return headers;
//                    }
//                };
//                // Thêm yêu cầu vào hàng đợi RequestQueue của Volley
//                Volley.newRequestQueue(DetailServiceHairActivity.this).add(request);
//            }
//        });
//
//        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // Đóng dialog khi nhấn nút "Hủy"
//            }
//        });
//
//        builder.show();
//    }

//
//    private JSONObject addAReview(Integer rating, String comment) {
//        JSONObject requestBody = new JSONObject();
//        try {
//            requestBody.put("serviceId", serviceHairId); // Lấy giá trị từ EditText
//            requestBody.put("rating", rating); // Sử dụng giá trị double trong JSONObject
//            requestBody.put("comment", comment);
//            requestBody.put("customerId", customerId);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return requestBody;
//    }

//
//    private void checkCanReview(String serviceId, String customerId) {
//        String apiUrl = Constant.baseUrl + "customer/review/findToReview/" + serviceId + "/" + customerId;
//
//        StringRequest request = new StringRequest(Request.Method.GET, apiUrl, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonResponse = new JSONObject(response);
//                    boolean canReview = jsonResponse.getBoolean("data");
//                    handleCanReview(canReview);
//                } catch (JSONException e) {
//                    Log.e("Error", "JSON parsing error: " + e.getMessage());
//                    Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi xử lý dữ liệu từ máy chủ.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Error", "API request failed: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi kết nối tới máy chủ.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        Volley.newRequestQueue(this).add(request);
//    }

//    private void handleCanReview(boolean canReview) {
//
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) binding.listviewComments.getLayoutParams();
//
//
//        if (canReview) {
//            binding.btnAddReview.setVisibility(View.VISIBLE);
//        } else {
//            binding.btnAddReview.setVisibility(View.INVISIBLE);
//        }
//
//        if (binding.btnAddReview.getVisibility() == View.INVISIBLE) {
//            // Nếu btn_add_review không hiển thị, thiết lập quy tắc layout cho listViewComments
//            layoutParams.addRule(RelativeLayout.BELOW, binding.textCommentLabel.getId());
//        } else {
//            // Nếu btn_add_review hiển thị, giữ nguyên quy tắc layout cho listViewComments
//            layoutParams.addRule(RelativeLayout.BELOW, binding.btnAddReview.getId());
//        }
//
//        binding.listviewComments.setLayoutParams(layoutParams);
//    }


}
