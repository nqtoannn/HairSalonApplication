package com.example.hairsalon.activity.employee;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.activity.appointment.AppointmentHistoryActivity;
import com.example.hairsalon.activity.appointment.DetailAppointmentActivity;
import com.example.hairsalon.adapter.AppointmentAdapterItem;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentBookingListBinding;
import com.example.hairsalon.model.Appointment;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BookingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BookingListFragment extends Fragment {

    private FragmentBookingListBinding binding;
    List<Map<String, Object>> appointmentList;
    ArrayList<Appointment> dataArrayList = new ArrayList<>();
    public BookingListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        Integer employeeId = sharedPreferences.getInt("userId", 1);
        binding.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Appointment clickedAppointment = dataArrayList.get(i);
                Log.i("clicked", clickedAppointment.getSalonName());
                // Tạo intent để chuyển sang DetailAppointmentActivity và gửi dữ liệu cuộc hẹn
                Intent intent = new Intent(getActivity(), DetailAppointmentActivity.class);
                // Gửi dữ liệu cuộc hẹn qua intent
                intent.putExtra("salonName", clickedAppointment.getSalonName());
                intent.putExtra("serviceName", clickedAppointment.getServiceName());
                intent.putExtra("time", clickedAppointment.getAppointmentTime());
                intent.putExtra("date", clickedAppointment.getAppointmentDate());
                intent.putExtra("address", clickedAppointment.getSalonAddress());
                intent.putExtra("stylist", clickedAppointment.getUserName());
                intent.putExtra("id", clickedAppointment.getId());
                intent.putExtra("isEmployee", "Yes");
                intent.putExtra("status", clickedAppointment.getStatus());
                startActivity(intent);
            }
        });
        ApiService.apiService.getAllAppointmentByEmployeeId(employeeId).enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                if (response.isSuccessful()) {
                    ResponseData responseData = response.body();
                    if (responseData != null && responseData.getStatus().equals("OK")) {
                        appointmentList = responseData.getData();
                        dataArrayList.clear();
                        for (Map<String, Object> appointment : appointmentList) {
                            Integer id = ((Number) appointment.get("id")).intValue();
                            String customerName = (String) appointment.get("customerName");
                            String userName = (String) appointment.get("userName");
                            String appointmentDate = (String) appointment.get("appointmentDate");
                            String appointmentTime = (String) appointment.get("appointmentTime");
                            String status = (String) appointment.get("status");
                            Log.e("Status", status);
                            String salonName = (String) appointment.get("salonName");
                            String serviceName = (String) appointment.get("serviceName");
                            String salonAddress = (String) appointment.get("salonAddress");
                            Appointment appointmentAdded = new Appointment(id, customerName, appointmentTime, appointmentDate,
                                    serviceName, status, userName, salonAddress, salonName);
                            dataArrayList.add(appointmentAdded);
                        }
                        AppointmentAdapterItem adapter = new AppointmentAdapterItem(requireContext(),dataArrayList);
                        binding.gridView.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(requireContext(), "Có lỗi khi nhận lịch hẹn. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }
}