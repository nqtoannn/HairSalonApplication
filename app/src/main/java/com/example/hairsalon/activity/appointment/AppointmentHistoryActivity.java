package com.example.hairsalon.activity.appointment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

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
    String customerId;


    private List<Map<String, Object>> appointmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppointmentHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE);
        customerId = sharedPreferences.getString("userId", "");
        getAllAppointments();
        binding.listViewAppointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment clickedAppointment = dataArrayList.get(i);
                Log.i("clicked", clickedAppointment.getSalonName());
                // Tạo intent để chuyển sang DetailAppointmentActivity và gửi dữ liệu cuộc hẹn
                Intent intent = new Intent(AppointmentHistoryActivity.this, DetailAppointmentActivity.class);
                // Gửi dữ liệu cuộc hẹn qua intent
                intent.putExtra("salonName", clickedAppointment.getSalonName());
                intent.putExtra("serviceName", clickedAppointment.getServiceName());
                intent.putExtra("time", clickedAppointment.getAppointmentTime());
                intent.putExtra("date", clickedAppointment.getAppointmentDate());
                intent.putExtra("address", clickedAppointment.getSalonAddress());
                intent.putExtra("stylist", clickedAppointment.getUserName());
                intent.putExtra("id", clickedAppointment.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllAppointments();
    }

    private void getAllAppointments() {
        ApiService.apiService.getAllAppointmentByCustomerId(customerId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    Log.i("appointment", "Thành công");
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        dataArrayList.clear();
                        appointmentList = responseData.getData();
                        for (Map<String, Object> appointment : appointmentList) {
                            String id = (String) appointment.get("appointmentId");
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
                        appointmentAdapter = new AppointmentAdapter(AppointmentHistoryActivity.this, dataArrayList);
                        binding.listViewAppointments.setAdapter(appointmentAdapter);
                        appointmentAdapter.notifyDataSetChanged();
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
