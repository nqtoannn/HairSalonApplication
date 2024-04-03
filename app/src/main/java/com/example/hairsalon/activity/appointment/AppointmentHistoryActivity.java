package com.example.hairsalon.activity.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.hairsalon.R;
import com.example.hairsalon.adapter.AppointmentAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.ActivityAppointmentHistoryBinding;
import com.example.hairsalon.model.Appointment;
import com.example.hairsalon.model.ResponseData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentHistoryActivity extends AppCompatActivity {

    ActivityAppointmentHistoryBinding binding;

    ArrayList<Appointment> dataArrayList = new ArrayList<>();

    AppointmentAdapter appointmentAdapter;
    private List<Map<String, Object>> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getAllAppointments();
    }

    private void getAllAppointments() {
        ApiService.apiService.getAllAppointmentByCustomerId(1).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    Log.i("appointment", "Thành công");
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        appointmentList = responseData.getData();
                        for (Map<String, Object> appointment : appointmentList) {
                            Integer id = ((Number) appointment.get("id")).intValue();
                            String customerName = (String) appointment.get("customerName");
                            String userName = (String) appointment.get("userName");
                            String appointmentDate = (String) appointment.get("appointmentDate");
                            String appointmentTime = (String) appointment.get("appointmentTime");
                            String status = (String) appointment.get("status");
                            String salonName = (String) appointment.get("salonName");
                            String serviceName = (String) appointment.get("serviceName");
                            String salonAddress = (String) appointment.get("salonAddress");
                            Appointment appointmentAdded = new Appointment(id, customerName, appointmentTime, appointmentDate,
                                    serviceName, status, userName, salonAddress, salonName);
                            dataArrayList.add(appointmentAdded);
                        }
                        Collections.reverse(dataArrayList);
                        appointmentAdapter = new AppointmentAdapter(AppointmentHistoryActivity.this, dataArrayList);
                        binding.listViewAppointments.setAdapter(appointmentAdapter);
                    } else {
                        Log.e("appointment", "No product data found in response");
                    }
                } else {
                    Log.e("appointment", "API call failed with error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("appointment", "API call failed: " + t.getMessage());
            }
        });
    }
}
