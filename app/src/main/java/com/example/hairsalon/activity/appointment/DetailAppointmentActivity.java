package com.example.hairsalon.activity.appointment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.R;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityDetailAppointmentBinding;
import com.example.hairsalon.databinding.ActivityDetailProductBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailAppointmentActivity extends AppCompatActivity {

    ActivityDetailAppointmentBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        if (intent != null) {
            // Lấy dữ liệu từ intent và hiển thị lên giao diện
            String salonName = intent.getStringExtra("salonName");
            String serviceName = intent.getStringExtra("serviceName");
            String time = intent.getStringExtra("time");
            String date = intent.getStringExtra("date");
            time = time + ", ngày " + date;
            String address = intent.getStringExtra("address");
            String stylist = intent.getStringExtra("stylist");

            // Hiển thị dữ liệu lên giao diện
            binding.tvNameSalon.setText("Tên salon: " + salonName);
            binding.tvNameService.setText("Tên dịch vụ: " + serviceName);
            binding.tvTime.setText("Thời gian: " + time);
            binding.tvAddress.setText("Địa chỉ: " + address);
            binding.tvStylist.setText("Stylist: " + stylist);
        }

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }



    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đặt lịch");
        builder.setMessage("Bạn có chắc chắn muốn hủy lịch này không?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelAnAppointment();
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

    private void cancelAnAppointment() {
        String apiUrl = Constant.baseUrl + "appointments/update-status";
        Intent intent = getIntent();
        Integer id = intent.getIntExtra("id", 0);
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("appointmentId", id); // Thay đổi customerId tùy theo người dùng hiện tại
            requestBody.put("statusCode", 3); // Sử dụng ID của dịch vụ được chọn
            StringRequest request = new StringRequest(Request.Method.PUT, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            setResult(RESULT_OK);
                            Toast.makeText(getApplicationContext(), "Hủy đặt lịch thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("cancel",  error.getMessage());
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi hủy đặt lịch", Toast.LENGTH_SHORT).show();
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
        }
            catch (JSONException e) {
                e.printStackTrace();
                Log.e("cancel",  e.getMessage());
                Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi tạo yêu cầu", Toast.LENGTH_SHORT).show();
            }
    }



}