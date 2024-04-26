package com.example.hairsalon.activity.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
    private int selectedServiceId = 1;
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

        // Khởi tạo Spinner cho salon với tiêu đề
        final int[] salonIds = {1, 2, 3}; // ID của các salon
        final String[] salonNames = {"Salon A", "Salon B", "Salon C"}; // Tên hiển thị của các salon
        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, salonNames);
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
        final int[] serviceIds = {1, 2, 3};  // ID của các dịch vụ
        final String[] serviceNames = {"Dịch vụ A", "Dịch vụ B", "Dịch vụ C"}; // Tên hiển thị của các dịch vụ
        ArrayAdapter<String> serviceAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, serviceNames);
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
        final int[] stylistIds = {1, 2, 3}; // ID của các stylist
        final String[] stylistNames = {"Stylist A", "Stylist B", "Stylist C"}; // Tên hiển thị của các stylist
        ArrayAdapter<String> stylistAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, stylistNames);
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

    // Hiển thị dialog chọn giờ
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

            // Gửi yêu cầu đặt lịch
            // Code xử lý phản hồi từ máy chủ

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
                // Code xử lý khi người dùng xác nhận đặt lịch
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
