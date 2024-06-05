package com.example.hairsalon.activity.appointment;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.activity.cart.CartActivity;
import com.example.hairsalon.activity.order.PayActivity;
import com.example.hairsalon.adapter.HairServiceAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityAppointmentBinding;
import com.example.hairsalon.model.HairService;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.ResponseServiceData;
import com.example.hairsalon.model.Salon;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class AppointmentActivity extends AppCompatActivity {

    private ActivityAppointmentBinding binding;
    private int selectedSalonId = 1;
    int selectedServiceId;
    List<Map<String, Object>> salonList;
    List<Map<String, Object>> hairServiceList;
    private int selectedStylistId = 1;

    String[] salonIds;
    String[] salonNames;
    String[] serviceIds;
    String[] serviceNames;

    String[] stylistIds;
    String[] stylistNames;

    String serviceHairId = "";
    String customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getString("userId", "");

        Intent intent = getIntent();
        if (intent != null) {
            serviceHairId =  intent.getStringExtra("serviceHairId");
            String serviceName = intent.getStringExtra("serviceName");
//            selectedServiceId = serviceHairId;
            binding.tvServiceName.setVisibility(View.VISIBLE);
            binding.tvServiceName.setText(serviceName);
            binding.spinnerService.setVisibility(View.INVISIBLE);
        }

        ApiService.apiService.getAllSalons().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        salonList = responseData.getData();
                        salonIds = new String[salonList.size()];
                        salonNames = new String[salonList.size()];
                        int index = 0;
                        for (Map<String, Object> salon : salonList) {
                            String id = ((String) salon.get("id"));
                            salonIds[index] = id;
                            String salonName = (String) salon.get("name");
                            salonNames[index] = salonName;
                            index++;
                        }
                        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(AppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, salonNames);
                        binding.spinnerSalon.setAdapter(salonAdapter);
                    } else {
                        Log.e("Error", "No salon data found in response");
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

        ApiService.apiService.getAllHairService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        List<Map<String, Object>> hairServiceList = responseData.getData();
                        serviceIds = new String[hairServiceList.size()];
                        serviceNames = new String[hairServiceList.size()];
                        int index = 0;
                        for (Map<String, Object> service : hairServiceList) {
                            String id = ((String) service.get("id"));
                            serviceIds[index] = id;
                            String serviceName = (String) service.get("serviceName");
                            serviceNames[index] = serviceName;
                            index++;
                        }

                        // Tạo ArrayAdapter từ mảng serviceNames và gắn nó vào Spinner
                        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(AppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, serviceNames);
                        binding.spinnerService.setAdapter(serviceAdapter);
                    } else {
                        Log.e("Error", "No hair service data found in response"); // Hiển thị thông báo nếu không có dữ liệu dịch vụ tóc
                    }
                } else {
                    Log.e("Error", "API call failed with error code: " + response.code()); // Hiển thị thông báo nếu cuộc gọi API không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage()); // Hiển thị thông báo nếu cuộc gọi API thất bại
            }
        });

        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của dịch vụ được chọn
                selectedServiceId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });



// Xử lý sự kiện khi người dùng chọn salon từ Spinner
        binding.spinnerSalon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của salon được chọn
                int selectedSalonId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });


        ApiService.apiService.getAllEmployees().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        List<Map<String, Object>> stylistList = responseData.getData();
                        stylistIds = new String[stylistList.size()];
                        stylistNames = new String[stylistList.size()];
                        int index = 0;
                        for (Map<String, Object> service : stylistList) {
                            String id = ((String) service.get("id"));
                            stylistIds[index] = id;
                            String userName = (String) service.get("fullName");
                            stylistNames[index] = userName;
                            index++;
                        }

                        // Tạo ArrayAdapter từ mảng serviceNames và gắn nó vào Spinner
                        ArrayAdapter<String> stylistAdapter = new ArrayAdapter<>(AppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, stylistNames);
                        binding.spinnerStylist.setAdapter(stylistAdapter);
                    } else {
                        Log.e("Error", "No hair service data found in response"); // Hiển thị thông báo nếu không có dữ liệu dịch vụ tóc
                    }
                } else {
                    Log.e("Error", "API call failed with error code: " + response.code()); // Hiển thị thông báo nếu cuộc gọi API không thành công
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage()); // Hiển thị thông báo nếu cuộc gọi API thất bại
            }
        });

        binding.spinnerStylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của dịch vụ được chọn
                selectedStylistId = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        // Xử lý sự kiện khi người dùng chọn giờ
        binding.textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        binding.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Xử lý sự kiện khi người dùng xác nhận đặt lịch
        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    // Hiển thị dialog chọn ngày
    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = String.format(Locale.getDefault(), "%d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);
                        binding.textViewDate.setText(dateString);
                    }
                },
                year, month, day);
        datePickerDialog.show();
    }

    // Hiển thị dialog chọn giờ
    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        binding.textViewTime.setText(timeString);
                    }
                },
                hour, minute, true);
        timePickerDialog.show();
    }

    // Gửi yêu cầu đặt lịch
    private void makeAnAppointment() {
        String apiUrl = Constant.baseUrl + "appointment/add";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("customerId", customerId); // Thay đổi customerId tùy theo người dùng hiện tại
            requestBody.put("serviceId", serviceHairId); // Sử dụng ID của dịch vụ được chọn
            requestBody.put("salonId", salonIds[selectedSalonId]); // Sử dụng ID của salon được chọn
            requestBody.put("appointmentDate", binding.textViewDate.getText().toString()); // Sử dụng ngày được chọn
            requestBody.put("appointmentTime", binding.textViewTime.getText().toString() + ":00"); // Sử dụng giờ được chọn
            requestBody.put("userId", stylistIds[selectedStylistId]); // Thay đổi userId tùy theo người dùng hiện tại

            final String requestBodyString = requestBody.toString();

            Log.i("request body", requestBodyString);

            StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Xử lý phản hồi thành công từ máy chủ
                            Toast.makeText(getApplicationContext(), "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Xử lý lỗi khi gửi yêu cầu
                            Log.e("appointment", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Đã xảy ra lỗi khi đặt lịch", Toast.LENGTH_SHORT).show();
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

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đặt lịch");
        builder.setMessage("Bạn có chắc chắn muốn đặt lịch này không?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeAnAppointment();
                Intent intent = new Intent(AppointmentActivity.this, AppointmentHistoryActivity.class);
                startActivity(intent);
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
