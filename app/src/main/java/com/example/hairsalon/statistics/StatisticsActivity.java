package com.example.hairsalon.statistics;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hairsalon.R;
import com.example.hairsalon.databinding.ActivityStatisticsBinding;

import java.util.Calendar;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity {

    private ActivityStatisticsBinding binding;
    private int selectedStatisticsId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStatisticsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo Spinner cho thống kê với tiêu đề
        final int[] salonIds = {1, 2}; // ID của các salon
        final String[] salonNames = {"Doanh thu", "Sản phẩm"}; // Tên hiển thị của các loại thống kê
        ArrayAdapter<String> salonAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, salonNames);
        binding.spinnerStatistics.setAdapter(salonAdapter);

        // Xử lý sự kiện khi người dùng chọn loại thống kê từ spinner
        binding.spinnerStatistics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Lưu ID của thống kế được chọn
                selectedStatisticsId = salonIds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Xử lý khi không có mục nào được chọn
            }
        });

        // Xử lý sự kiện khi người dùng chọn ngày bắt đầu
        binding.textViewDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogStart();
            }
        });

        // Xử lý sự kiện khi người dùng chọn ngày kết thúc
        binding.textViewDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialogEnd();
            }
        });

        //Xử lý hiển thị biểu đồ
        Button btnStatistics = findViewById(R.id.btnStatistics);
        Intent intentToDoanhThu = new Intent(StatisticsActivity.this, DoanhThu.class);
        Intent intentToSanPham = new Intent(StatisticsActivity.this, SanPham.class);
        btnStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStatisticsId == 1)
                {
                    startActivity(intentToDoanhThu);
                }
                else
                    startActivity(intentToSanPham);
            }
        });
    }
    // Hiển thị dialog chọn ngày bắt đầu
    private void showDatePickerDialogStart() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        //int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = String.format(Locale.getDefault(), "%d-%02d", year, monthOfYear + 1);
                        binding.textViewDateStart.setText(dateString);
                    }
                },
                year, month, 0);
        datePickerDialog.show();
    }

    // Hiển thị dialog chọn ngày kết thúc
    private void showDatePickerDialogEnd() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        //int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String dateString = String.format(Locale.getDefault(), "%d-%02d", year, monthOfYear + 1);
                        binding.textViewDateEnd.setText(dateString);
                    }
                },
                year, month,0);
        datePickerDialog.show();
    }



}