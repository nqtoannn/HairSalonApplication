package com.example.hairsalon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hairsalon.R;
import com.example.hairsalon.model.Appointment;

import java.util.List;

public class AppointmentAdapter extends ArrayAdapter<Appointment> {

    private Context context;
    private List<Appointment> appointments;

    public AppointmentAdapter(Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.appointment_info_item, parent, false);
            holder = new ViewHolder();
            holder.tvNameService = convertView.findViewById(R.id.tvNameService);
            holder.tvTime = convertView.findViewById(R.id.tvTime);
            holder.tvAddress = convertView.findViewById(R.id.tvAddress);
            holder.tvStylist = convertView.findViewById(R.id.tvStylist);
            holder.tvUpcoming = convertView.findViewById(R.id.tvUpcoming);
            holder.tvNameSalon = convertView.findViewById(R.id.tvNameSalon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Appointment appointment = appointments.get(position);
        holder.tvNameSalon.setText("Tên salon: " + appointment.getSalonName());
        holder.tvNameService.setText("Tên dịch vụ: " + appointment.getServiceName());
        holder.tvTime.setText("Thời gian: " + appointment.getAppointmentTime() + ", ngày " + appointment.getAppointmentDate());
        holder.tvAddress.setText("Địa chỉ: " + appointment.getSalonAddress());
        holder.tvStylist.setText("Stylist: " + appointment.getUserName());

        // Kiểm tra xem item đầu tiên có phải không và thiết lập text và background tương ứng
        if (position == 0) {
            holder.tvUpcoming.setVisibility(View.VISIBLE);
            convertView.setBackgroundResource(R.drawable.first_item_background);
        } else {
            holder.tvUpcoming.setVisibility(View.GONE);
            convertView.setBackgroundResource(R.drawable.border_shadow);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvNameService, tvTime, tvAddress, tvStylist, tvUpcoming, tvNameSalon;
    }
}
