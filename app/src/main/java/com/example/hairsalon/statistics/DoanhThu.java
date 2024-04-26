package com.example.hairsalon.statistics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hairsalon.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

public class DoanhThu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doanh_thu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BarChart barChart = findViewById(R.id.barChart);

        // Chuẩn bị dữ liệu doanh thu cho từng tháng trong năm (đây là một ví dụ, bạn cần thay đổi để phù hợp với dữ liệu thực tế)
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 1000f)); // Tháng 1
        entries.add(new BarEntry(2, 1350f)); // Tháng 2
        entries.add(new BarEntry(3, 1350f)); // Tháng 3
        entries.add(new BarEntry(4, 1500f)); // Tháng 4
        entries.add(new BarEntry(5, 1260f)); // Tháng 5
        entries.add(new BarEntry(6, 1457f)); // Tháng 6
        entries.add(new BarEntry(7, 1199f)); // Tháng 7
        entries.add(new BarEntry(8, 900f)); // Tháng 8
        entries.add(new BarEntry(9, 1234f)); // Tháng 9
        entries.add(new BarEntry(10, 1754f)); // Tháng 10
        entries.add(new BarEntry(11, 1222f)); // Tháng 11
        entries.add(new BarEntry(12, 1567f)); // Tháng 12
        // Thêm dữ liệu cho các tháng khác tương tự

        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu của năm 2023");

        // Thiết lập màu sắc cho biểu đồ cột
        dataSet.setColor(Color.RED);

        // Tạo dữ liệu cho biểu đồ cột
        BarData data = new BarData(dataSet);
        barChart.setData(data);

        // Thiết lập các thuộc tính khác cho biểu đồ cột (như nhãn trục X, trục Y, v.v.)

        // ...

        // Hiển thị biểu đồ cột
        barChart.invalidate();
    }
}