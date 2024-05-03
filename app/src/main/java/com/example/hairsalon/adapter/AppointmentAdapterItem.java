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
import java.util.Map;

public class AppointmentAdapterItem extends ArrayAdapter<Appointment> {

    private Context context;
    List<Appointment> appointments;

    public AppointmentAdapterItem(Context context, List<Appointment> appointments) {
        super(context, 0, appointments);
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);
            holder = new ViewHolder();
            holder.tvNameServiceIt = convertView.findViewById(R.id.tvNameServiceIt);
            holder.tvTimeIt = convertView.findViewById(R.id.tvTimeIt);
            holder.tvStylistIt = convertView.findViewById(R.id.tvStylistIt);
            holder.tvUpcomingIt = convertView.findViewById(R.id.tvUpcomingIt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Appointment appointment = appointments.get(position);

        holder.tvNameServiceIt.setText("Tên dịch vụ: " + appointment.getServiceName());
        holder.tvTimeIt.setText("Thời gian: " + appointment.getAppointmentTime() + ", ngày " + appointment.getAppointmentDate());
        holder.tvStylistIt.setText("Tên khách hàng: "+appointment.getCustomerName());

        // Kiểm tra xem item đầu tiên có phải không và thiết lập text và background tương ứng
        if (position == 0) {
            holder.tvUpcomingIt.setVisibility(View.VISIBLE);
            convertView.setBackgroundResource(R.drawable.first_item_background);
        } else {
            holder.tvUpcomingIt.setVisibility(View.GONE);
            convertView.setBackgroundResource(R.drawable.border_shadow);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tvNameServiceIt, tvTimeIt, tvStylistIt, tvUpcomingIt;
    }
}
