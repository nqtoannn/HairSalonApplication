package com.example.hairsalon.activity.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hairsalon.activity.appointment.AppointmentHistoryActivity;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.FragmentBookingBinding;
import com.example.hairsalon.model.ResponseData;

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

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private int selectedSalonId = 1;
    int selectedServiceId = 1;
    List<Map<String, Object>> salonList;
    List<Map<String, Object>> hairServiceList;
    private int selectedStylistId = 1;

    int[] serviceIds = null;
    String[] serviceNames = {};

    int[] salonIds;
    String[] salonNames;

     int[] stylistIds;
     String[] stylistNames;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ApiService.apiService.getAllSalons().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        salonList = responseData.getData();
                        salonIds = new int[salonList.size()];
                        salonNames = new String[salonList.size()];
                        int index = 0;
                        for (Map<String, Object> salon : salonList) {
                            Integer id = ((Double) salon.get("id")).intValue();
                            salonIds[index] = id;
                            String salonName = (String) salon.get("salonName");
                            salonNames[index] = salonName;
                            index++;
                        }

                        // Tạo ArrayAdapter từ mảng salonNames và gắn nó vào Spinner
                        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, salonNames);
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

// Xử lý sự kiện khi người dùng chọn salon từ Spinner
        binding.spinnerSalon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của salon được chọn
                int selectedSalonId = salonIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });



        // Initialize Spinner for service with titles

        ApiService.apiService.getAllHairService().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        List<Map<String, Object>> hairServiceList = responseData.getData();
                        serviceIds = new int[hairServiceList.size()];
                        serviceNames = new String[hairServiceList.size()];
                        int index = 0;
                        for (Map<String, Object> service : hairServiceList) {
                            Integer id = ((Double) service.get("id")).intValue();
                            serviceIds[index] = id;
                            String serviceName = (String) service.get("serviceName");
                            serviceNames[index] = serviceName;
                            index++;
                        }

                        // Tạo ArrayAdapter từ mảng serviceNames và gắn nó vào Spinner
                        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceNames);
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
                selectedServiceId = serviceIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });


        // Initialize Spinner for stylist with titles

        ApiService.apiService.getAllEmployees().enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, retrofit2.Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        List<Map<String, Object>> stylistList = responseData.getData();
                        stylistIds = new int[stylistList.size()];
                        stylistNames = new String[stylistList.size()];
                        int index = 0;
                        for (Map<String, Object> service : stylistList) {
                            Integer id = ((Double) service.get("id")).intValue();
                            stylistIds[index] = id;
                            String userName = (String) service.get("userName");
                            stylistNames[index] = userName;
                            index++;
                        }

                        // Tạo ArrayAdapter từ mảng serviceNames và gắn nó vào Spinner
                        ArrayAdapter<String> stylistAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, stylistNames);
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
                selectedStylistId = stylistIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });


        // Handle event when user clicks on date
        binding.textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // Handle event when user clicks on time
        binding.textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // Handle event when user confirms booking
        binding.buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
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

    private void showTimePickerDialog() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
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

    private void makeAnAppointment() {
        String apiUrl = Constant.baseUrl + "appointments/makeApm";
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("customerId", 1);
            requestBody.put("serviceId", selectedServiceId);
            requestBody.put("salonId", selectedSalonId);
            requestBody.put("appointmentDate", binding.textViewDate.getText().toString());
            requestBody.put("appointmentTime", binding.textViewTime.getText().toString() + ":00");
            requestBody.put("userId", selectedStylistId);

            final String requestBodyString = requestBody.toString();

            Log.i("request body", requestBodyString);

            StringRequest request = new StringRequest(Request.Method.POST, apiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(requireContext(), "Đặt lịch thành công", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("appointment", error.getMessage());
                            Toast.makeText(requireContext(), "Đã xảy ra lỗi khi đặt lịch", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public byte[] getBody() {
                    return requestBodyString.getBytes();
                }

                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };

            Volley.newRequestQueue(requireContext()).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Đã xảy ra lỗi khi tạo yêu cầu", Toast.LENGTH_SHORT).show();
        }
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận đặt lịch");
        builder.setMessage("Bạn có chắc chắn muốn đặt lịch này không?");
        builder.setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeAnAppointment();
                Intent intent = new Intent(requireContext(), AppointmentHistoryActivity.class);
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
