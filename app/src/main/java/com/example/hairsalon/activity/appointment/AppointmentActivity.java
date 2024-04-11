package com.example.hairsalon.activity.appointment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import com.example.hairsalon.activity.pay.PayActivity;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.ActivityAppointmentBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppointmentActivity extends AppCompatActivity {

    private ActivityAppointmentBinding binding;
    private int selectedSalonId = 1;
    private int selectedServiceId = 1;
    private int selectedStylistId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Spinner cho salon với tiêu đề
        final int[] salonIds = {1, 2, 3}; // ID của các salon
        final String[] salonNames = {"Salon A", "Salon B", "Salon C"}; // Tên hiển thị của các salon
        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, salonNames);
        binding.spinnerSalon.setAdapter(salonAdapter);

        // Xử lý sự kiện khi người dùng chọn salon từ spinner
        binding.spinnerSalon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của salon được chọn
                selectedSalonId = salonIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        // Khởi tạo Spinner cho dịch vụ với tiêu đề
        final int[] serviceIds =  { 1, 2, 3};  // ID của các dịch vụ
        final String[] serviceNames = {"Dịch vụ A", "Dịch vụ B", "Dịch vụ C"}; // Tên hiển thị của các dịch vụ
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, serviceNames);
        binding.spinnerService.setAdapter(serviceAdapter);

        // Xử lý sự kiện khi người dùng chọn dịch vụ từ spinner
        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của dịch vụ được chọn
                selectedServiceId = serviceIds[position];
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
