package com.example.hairsalon.activity.manage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.hairsalon.R;
import com.example.hairsalon.adapter.UserAdapter;
import com.example.hairsalon.api.ApiService;
import com.example.hairsalon.databinding.FragmentAddEmployeeBinding;
import com.example.hairsalon.databinding.FragmentEmployeeManageBinding;
import com.example.hairsalon.model.ResponseAuthData;
import com.example.hairsalon.model.ResponseData;
import com.example.hairsalon.model.User;
import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddEmployeeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEmployeeFragment extends Fragment {
    private FragmentAddEmployeeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddEmployeeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("fullName", binding.textFullName.getText().toString());
                jsonObject.addProperty("email", binding.textEmail.getText().toString());
                jsonObject.addProperty("address", binding.textAddress.getText().toString());
                jsonObject.addProperty("phoneNumber", binding.textPhoneNumber.getText().toString());
                ApiService.apiService.addNewEmployee(jsonObject).enqueue(new Callback<ResponseAuthData>() {
                    @Override
                    public void onResponse(Call<ResponseAuthData> call, Response<ResponseAuthData> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(),"Tạo mới tài khoản nhân viên thành công!",Toast.LENGTH_SHORT);
                            EmployeeManageFragment fragment = new EmployeeManageFragment();
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frameLayoutMn, fragment);
                            transaction.addToBackStack(null);
                            transaction.commit();
                        } else {
                            Toast.makeText(requireContext(),"Tạo mới tài khoản nhân viên không thành công!",Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseAuthData> call, Throwable t) {
                        Log.e("Error", "API call failed: " + t.getMessage());
                    }
                });

            }
        });
    }
}