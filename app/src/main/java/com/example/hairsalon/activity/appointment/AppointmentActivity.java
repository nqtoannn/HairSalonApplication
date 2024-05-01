package com.example.hairsalon.activity.appointment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
    int selectedServiceId = 1;
    private int selectedStylistId = 1;
    List<Map<String, Object>> salonList;
    List<Map<String, Object>> hairServiceList;
    List<Map<String, Object>> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        List<Integer> salonIds =new ArrayList<>(); // ID của các salon
         List<String> salonNames = new ArrayList<>(); //Tên các salon
        ApiService.apiService.getAllSalons().enqueue(new Callback<ResponseData>(){
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if(responseData != null && responseData.getStatus().equals("OK")){
                        salonList = responseData.getData();
                        for (Map<String, Object> salon:salonList)
                        {
                            Integer id = (Integer) salon.get("id");
                            salonIds.add(id);
                            String salonName = (String) salon.get("salonName");
                            salonNames.add(salonName);
                        }
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
        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, salonNames);
        binding.spinnerSalon.setAdapter(salonAdapter);
        binding.spinnerSalon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của salon được chọn
                selectedSalonId = salonIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        List<Integer> serviceIds = new ArrayList<>();
        List<String> serviceNames = new ArrayList<>();
        ApiService.apiService.getAllHairService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        hairServiceList = responseData.getData();
                        for (Map<String, Object> service:hairServiceList)
                        {
                            Integer id = (Integer) service.get("id");
                            serviceIds.add(id);
                            String serviceName = (String) service.get("serviceName");
                            serviceNames.add(serviceName);
                        }
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
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceNames);
        binding.spinnerService.setAdapter(serviceAdapter);

        // Xử lý sự kiện khi người dùng chọn dịch vụ từ spinner
        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của dịch vụ được chọn
                selectedServiceId = serviceIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        // Khởi tạo Spinner cho stylist với tiêu đề
        final int[] stylistIds =  {1, 2, 3}; // ID của các stylist
        final String[] stylistNames = {"Stylist A", "Stylist B", "Stylist C"}; // Tên hiển thị của các stylist
        ArrayAdapter<String> stylistAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, stylistNames);
        binding.spinnerStylist.setAdapter(stylistAdapter);

        // Xử lý sự kiện khi người dùng chọn stylist từ spinner
        binding.spinnerStylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của stylist được chọn
                selectedStylistId = stylistIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        // Xử lý sự kiện khi người dùng chọn ngày
        binding.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Xử lý sự kiện khi người dùng chọn giờ
        binding.textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
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
        String apiUrl = Constant.baseUrl + "appointments/makeApm";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("customerId", 1); // Thay đổi customerId tùy theo người dùng hiện tại
            requestBody.put("serviceId", selectedServiceId); // Sử dụng ID của dịch vụ được chọn
            requestBody.put("salonId", selectedSalonId); // Sử dụng ID của salon được chọn
            requestBody.put("appointmentDate", binding.textViewDate.getText().toString()); // Sử dụng ngày được chọn
            requestBody.put("appointmentTime", binding.textViewTime.getText().toString() + ":00"); // Sử dụng giờ được chọn
            requestBody.put("userId", selectedStylistId); // Thay đổi userId tùy theo người dùng hiện tại

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
