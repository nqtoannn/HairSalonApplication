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
import com.example.hairsalon.constants.Constant;
import com.example.hairsalon.databinding.FragmentBookingBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BookingFragment extends Fragment {
    private FragmentBookingBinding binding;
    private int selectedSalonId = 1;
    int selectedServiceId = 1;
    private int selectedStylistId = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize Spinner for salon with titles
        final int[] salonIds = {1, 2, 3};
        final String[] salonNames = {"Salon A", "Salon B", "Salon C"};
        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, salonNames);
        binding.spinnerSalon.setAdapter(salonAdapter);

        // Handle event when user selects salon from spinner
        binding.spinnerSalon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSalonId = salonIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });

        // Initialize Spinner for service with titles
        final int[] serviceIds = {1, 2, 3};
        final String[] serviceNames = {"Service A", "Service B", "Service C"};
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceNames);
        binding.spinnerService.setAdapter(serviceAdapter);

        // Handle event when user selects service from spinner
        binding.spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedServiceId = serviceIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
            }
        });

        // Initialize Spinner for stylist with titles
        final int[] stylistIds = {1, 2, 3};
        final String[] stylistNames = {"Stylist A", "Stylist B", "Stylist C"};
        ArrayAdapter<String> stylistAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, stylistNames);
        binding.spinnerStylist.setAdapter(stylistAdapter);

        // Handle event when user selects stylist from spinner
        binding.spinnerStylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStylistId = stylistIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when no item is selected
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
