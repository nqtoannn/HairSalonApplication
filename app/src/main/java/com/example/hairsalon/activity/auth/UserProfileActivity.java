package com.example.hairsalon.activity.auth;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.activity.home.HomeCustomer;
import com.example.hairsalon.activity.home.HomeManage;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivitiUserInformationBinding;
import com.example.hairsalon.model.AuthenticationRequest;
import com.example.hairsalon.model.ResponseAuthData;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.User;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity
{
    private ActivitiUserInformationBinding binding;
    private Button editButton;
    private ProgressBar progressBar;
    private ImageView imgView;
    private User user;
    private  Integer customerId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle saveInstanceState){

        super.onCreate(saveInstanceState);
        binding = ActivitiUserInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetEditableIs(false);
        binding.btnEnableEditing.setActivated(true);
        binding.btnApply.setVisibility(View.GONE);
        binding.btnCancel.setVisibility(View.GONE);
//        Context context = homeFragment.getActivity();
//        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
//        customerId = sharedPreferences.getInt("userId", -1);

        ApiService.apiService.getCustomerByID(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if(response.isSuccessful()){
                    ResponseData responseData = response.body();
                    Log.e("Success", "onResponse: " );
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        Map<String,Object> userInfo = responseData.getData().get(0);
                            Integer id = ((Number) userInfo.get("id")).intValue();
                            String fullName = (String) userInfo.get("fullName");
                            String phoneNumber = (String) userInfo.get("phoneNumber");
                            String add = (String) userInfo.get("address");
                            String status = (String) userInfo.get("status");
                            user = new User(fullName,phoneNumber,add,status);
                          SetValueToBinding(user);

                    } else {
                       Log.e("Error", "No user data found in response"); // Hiển thị thông báo nếu không có dữ liệu dịch vụ tóc
                   }
            } else {
                    Log.e("Error", "API call failed with error code: " + response.code()); // Hiển thị thông báo nếu cuộc gọi API không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage()); // Hiển thị kkmthông báo nếu cuộc gọi API thất bại
            }
        });



        binding.btnEnableEditing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetEditableIs(true);
                binding.btnEnableEditing.setActivated(false);
                binding.btnApply.setActivated(true);
                binding.btnCancel.setActivated(true);
                binding.btnApply.setVisibility(View.VISIBLE);
                binding.btnCancel.setVisibility(View.VISIBLE);
                SetPhoneEditConstrained();
            }
        });
        binding.btnApply.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makeEditRequest();
                SetEditableIs(false);
                binding.btnEnableEditing.setActivated(true);
                binding.btnApply.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.GONE);

            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetValueToBinding(user);
                SetEditableIs(false);
                binding.btnEnableEditing.setActivated(true);
                binding.btnApply.setActivated(false);
                binding.btnCancel.setActivated(false);
                binding.btnApply.setVisibility(View.GONE);
                binding.btnCancel.setVisibility(View.GONE);
            }
        });
    }
    private void makeEditRequest() {
        String apiUrl = Constant.baseUrl + "user/editUserInfor"; // đổi theo BE
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("userId", customerId); // id customer hiện tại
            requestBody.put("fullName", binding.editTextShowFullName);// full name edit
            requestBody.put("phoneNumber", binding.editTextShowPhone);// full name edit
            requestBody.put("address", binding.editTextShowPhone);// full name edit

            final String requestBodyString = requestBody.toString();
            Log.i("request body", requestBodyString);
            StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Xử lý phản hồi thành công từ máy chủ
                            Toast.makeText(getApplicationContext(), "Đổi Info thành công", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi khi gửi yêu cầu
                            Log.e("appointment", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi đổi info", Toast.LENGTH_SHORT).show();
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

    private void  SetPhoneEditConstrained(){
        binding.editTextShowPhone.setEnabled(true);

        EditText editTextPhone = binding.editTextShowPhone;
        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Được gọi sau khi văn bản thay đổi. Kiểm tra định dạng số điện thoại ở đây.
                String phone = s.toString();
                if (!isValidPhoneNumber(phone)) {
                    editTextPhone.setError("Invalid phone number"); // Báo lỗi nếu số điện thoại không hợp lệ
                } else {
                    editTextPhone.setError(null); // Xóa báo lỗi nếu số điện thoại hợp lệ
                }
            }
        });

    }
    private boolean isValidPhoneNumber(String phone) {
        return phone.length() == 10 && phone.matches("\\d+"); // Kiểm tra độ dài là 10 ký tự và chỉ chứa các ký tự số
    }

    private void SetValueToBinding(User user){
        binding.editTextShowFullName.setText(user.getUserName());
        binding.editTextShowAddress.setText(user.getAddress());
        binding.editTextShowPhone.setText(user.getPhone());

    }
    private void SetEditableIs(boolean isEditable){
        binding.editTextShowFullName.setEnabled(isEditable);
        binding.editTextShowPhone.setEnabled(isEditable);
        binding.editTextShowAddress.setEnabled(isEditable);
    }
}
